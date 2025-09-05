package com.clearskin_ai.userservice.service.Impl;

import com.clearskin_ai.userservice.api.dto.*;
import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.entity.AnalysisHistory;
import com.clearskin_ai.userservice.repository.AnalysisHistoryRepository;
import com.clearskin_ai.userservice.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisHistoryRepository analysisHistoryRepository;
    private final RestTemplate restTemplate;

    @Value("${flask.api.url:http://localhost:5000/predict}")
    private String flaskApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Override
    public AnalysisResponseDto analyzeImage(MultipartFile file, Long userId) {
        try {
            validateFile(file);

            // Call Flask ML model
            JSONObject mlResponse = callFlaskApi(file);
            String severity = mlResponse.getString("class");
            double confidence = mlResponse.getDouble("confidence");

            if (!isValidSeverity(severity)) {
                throw new IllegalStateException("Invalid severity returned from ML model: " + severity);
            }

            // Generate diagnosis using Gemini
            String diagnosis = generateGeminiDiagnosis(severity, confidence, userId);

            // Store analysis history
            AnalysisHistory history = new AnalysisHistory();
            history.setUserId(userId);
            history.setSeverity(severity);
            history.setConfidence(confidence);
            history.setDiagnosis(diagnosis);
            history.setAnalysisTime(new Timestamp(System.currentTimeMillis()));
            analysisHistoryRepository.save(history);

            String suggestion = getSuggestion(severity, confidence);

            return new AnalysisResponseDto(severity, confidence, suggestion, diagnosis, history.getAnalysisTime());

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(ApplicationConstants.MISSING_REQUIRED_FIELDS + ": " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze image: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        long MAX_FILE_SIZE = 5 * 1024 * 1024;
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed limit of 5MB. Please upload a smaller image.");
        }
    }

    private JSONObject callFlaskApi(MultipartFile file) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, entity, String.class);

        return new JSONObject(response.getBody());
    }

    private String generateGeminiDiagnosis(String severity, double confidence, Long userId) {
        String prompt;

        if (userId != null) {
            List<AnalysisHistory> previousHistory = analysisHistoryRepository.findByUserId(userId);

            // Filter out null or invalid confidence entries
            List<AnalysisHistory> validHistory = previousHistory.stream()
                    .filter(h -> h.getConfidence() != null && isValidSeverity(h.getSeverity()))
                    .collect(Collectors.toList());

            if (validHistory.isEmpty()) {
                prompt = createFirstTimePrompt(severity, confidence);
            } else {
                prompt = createFollowUpPrompt(severity, confidence, validHistory);
            }
        } else {
            prompt = createFirstTimePrompt(severity, confidence);
        }

        return callGeminiApi(prompt);
    }

    private String createFirstTimePrompt(String severity, double confidence) {
        return String.format(
                "Our model has 4 classes: none, mild, moderate, severe. " +
                        "This face image's class is %s with confidence %.2f. " +
                        "Provide a short prompt to inform the user about their acne condition and warn that neglecting it may lead to possible causes like scarring, infection, or worsening of acne. " +
                        "The message should be concise, slightly alarming, and focus only on the condition and potential consequences.",
                severity, confidence
        );
    }

    private String createFollowUpPrompt(String severity, double confidence, List<AnalysisHistory> previousHistory) {
        String previousStr = previousHistory.stream()
                .map(h -> String.format("%s with confidence %.2f", h.getSeverity(), h.getConfidence()))
                .collect(Collectors.joining(", "));

        return String.format(
                "Our model has 4 classes: none, mild, moderate, severe. Current: class %s, confidence %.2f. Previous: %s. " +
                        "Analyze if the acne condition is improving or worsening based on the current and previous analysis history. " +
                        "Provide a concise diagnosis with recommendations.",
                severity, confidence, previousStr
        );
    }

    private String callGeminiApi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonPayload = "{ \"contents\": [ { \"parts\": [ { \"text\": \""
                + prompt.replace("\"", "\\\"") + "\" } ] } ] }";

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;
        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Gemini API: " + e.getMessage(), e);
        }

        try {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray candidates = jsonResponse.getJSONArray("candidates");
            if (candidates.length() == 0) {
                throw new RuntimeException("No candidates in Gemini response");
            }
            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            return parts.getJSONObject(0).getString("text");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage(), e);
        }
    }


    @Override
    public List<AnalysisHistoryResponseDto> getUserAnalysisHistory(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(ApplicationConstants.INVALID_USER_ID);
        }

        List<AnalysisHistory> historyList = analysisHistoryRepository.findByUserId(userId);

        List<AnalysisHistoryResponseDto> responseList = new ArrayList<>();
        for (AnalysisHistory current : historyList) {

            responseList.add(new AnalysisHistoryResponseDto(
                    current.getHistory_id(),
                    current.getSeverity(),
                    current.getDiagnosis(),
                    current.getAnalysisTime(),
                    current.getConfidence()
            ));
        }

        return responseList;
    }


    @Override
    public AnalysisCountDto getAnalysisCount() {
        long count = analysisHistoryRepository.countAnalyses();
        return new AnalysisCountDto(count);
    }

    @Override
    public List<AnalysisResponseDto> getAnonymousAnalyses() {
        List<AnalysisHistory> anonymousList = analysisHistoryRepository.findAnonymousAnalyses();
        return anonymousList.stream()
                .map(history -> new AnalysisResponseDto(
                        history.getSeverity(),
                        history.getConfidence() != null ? history.getConfidence() : 1.0,
                        getSuggestion(history.getSeverity(), history.getConfidence() != null ? history.getConfidence() : 1.0),
                        history.getDiagnosis(),
                        history.getAnalysisTime()
                ))
                .collect(Collectors.toList());
    }

    private String getSuggestion(String severity, double confidence) {
        String confidenceText = String.format(" (Confidence: %.2f)", confidence);
        return switch (severity.toLowerCase()) {
            case "none" -> "Your skin looks clear! Maintain with gentle cleansing.";
            case "mild" -> "Use salicylic acid cleanser twice daily.";
            case "moderate" -> "Consider benzoyl peroxide and consult a dermatologist.";
            case "severe" -> "Seek immediate dermatologist consultation.";
            default -> "Unknown severity.";
        };
    }

    private boolean isValidSeverity(String severity) {
        return severity != null && List.of("none", "mild", "moderate", "severe").contains(severity.toLowerCase());
    }

    @Override
    public AdminAnalysisPageResponse getAllAnalysisHistoryForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("analysisTime").descending());
        Page<AnalysisHistory> historiesPage = analysisHistoryRepository.findAll(pageable);

        List<AdminAnalysisResponseDto> content = historiesPage.stream()
                .map(history -> new AdminAnalysisResponseDto(
                        history.getHistory_id(),
                        history.getUserId(),
                        history.getSeverity(),
                        history.getConfidence() != null ? history.getConfidence() : 1.0,
                        getSuggestion(history.getSeverity(), history.getConfidence() != null ? history.getConfidence() : 1.0),
                        history.getDiagnosis(),
                        history.getAnalysisTime()
                ))
                .collect(Collectors.toList());

        return new AdminAnalysisPageResponse(
                content,
                historiesPage.getTotalElements(),
                historiesPage.getTotalPages(),
                historiesPage.getNumber(),
                historiesPage.getSize()
        );
    }



    @Override
    public AnalysisResponseDto getAnalysisHistoryById(Long historyId, Long userId) {
        AnalysisHistory history = analysisHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("Analysis history not found with id: " + historyId));

        // Check if user is owner (or admin/staff via security, assumed handled externally)
        if (userId != null && !userId.equals(history.getUserId())) {
            throw new RuntimeException("User not authorized to access this history");
        }

        return new AnalysisResponseDto(
                history.getSeverity(),
                history.getConfidence() != null ? history.getConfidence() : 1.0,
                getSuggestion(history.getSeverity(), history.getConfidence() != null ? history.getConfidence() : 1.0),
                history.getDiagnosis(),
                history.getAnalysisTime()
        );
    }

    @Override
    public void deleteAnalysisHistoryById(Long historyId) {
        if (!analysisHistoryRepository.existsById(historyId)) {
            throw new RuntimeException("Analysis history not found with id: " + historyId);
        }
        analysisHistoryRepository.deleteById(historyId);
    }
}
