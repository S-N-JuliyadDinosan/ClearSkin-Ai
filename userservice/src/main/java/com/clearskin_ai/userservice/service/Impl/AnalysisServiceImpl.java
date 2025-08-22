package com.clearskin_ai.userservice.service.Impl;

import com.clearskin_ai.userservice.api.dto.AnalysisCountDto;
import com.clearskin_ai.userservice.api.dto.AnalysisResponseDto;
import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.entity.AnalysisHistory;
import com.clearskin_ai.userservice.repository.AnalysisHistoryRepository;
import com.clearskin_ai.userservice.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisHistoryRepository analysisHistoryRepository;
    private final RestTemplate restTemplate;

    @Value("${flask.api.url:http://localhost:5000/predict}")
    private String flaskApiUrl;

    @Override
    public AnalysisResponseDto analyzeImage(MultipartFile file, Long userId) {
        try {
            // Validate file
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("Image file is required");
            }

            // Call Flask API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());
            String severity = jsonResponse.getString("class"); // Changed from "severity" to "class"
            double confidence = jsonResponse.getDouble("confidence");

            // Validate severity
            if (!isValidSeverity(severity)) {
                throw new IllegalStateException("Invalid severity returned from ML model: " + severity);
            }

            // Store in AnalysisHistory
            AnalysisHistory history = new AnalysisHistory();
            history.setUserId(userId); // Null for anonymous
            history.setSeverity(severity);
            history.setAnalysisTime(new Timestamp(System.currentTimeMillis()));
            analysisHistoryRepository.save(history);

            // Get suggestion
            String suggestion = getSuggestion(severity, confidence);

            return new AnalysisResponseDto(severity, suggestion, history.getAnalysisTime());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(ApplicationConstants.MISSING_REQUIRED_FIELDS + ": " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze image: " + e.getMessage());
        }
    }

    @Override
    public List<AnalysisResponseDto> getAnalysisHistory(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException(ApplicationConstants.INVALID_USER_ID);
            }
            List<AnalysisHistory> historyList = analysisHistoryRepository.findByUserId(userId);
            return historyList.stream()
                    .map(history -> new AnalysisResponseDto(
                            history.getSeverity(),
                            getSuggestion(history.getSeverity(), 1.0), // Default confidence for history
                            history.getAnalysisTime()))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch analysis history: " + e.getMessage());
        }
    }

    @Override
    public AnalysisCountDto getAnalysisCount() {
        try {
            long count = analysisHistoryRepository.countAnalyses();
            return new AnalysisCountDto(count);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch analysis count: " + e.getMessage());
        }
    }

    @Override
    public List<AnalysisResponseDto> getAnonymousAnalyses() {
        try {
            List<AnalysisHistory> anonymousList = analysisHistoryRepository.findAnonymousAnalyses();
            return anonymousList.stream()
                    .map(history -> new AnalysisResponseDto(
                            history.getSeverity(),
                            getSuggestion(history.getSeverity(), 1.0), // Default confidence for history
                            history.getAnalysisTime()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch anonymous analyses: " + e.getMessage());
        }
    }

    private String getSuggestion(String severity, double confidence) {
        String confidenceText = String.format(" (Confidence: %.2f)", confidence);
        switch (severity.toLowerCase()) {
            case "none":
                return "Your skin looks clear! Maintain with gentle cleansing." + confidenceText;
            case "mild":
                return "Use salicylic acid cleanser twice daily." + confidenceText;
            case "moderate":
                return "Consider benzoyl peroxide and consult a dermatologist." + confidenceText;
            case "severe":
                return "Seek immediate dermatologist consultation." + confidenceText;
            default:
                return "Unknown severity.";
        }
    }

    private boolean isValidSeverity(String severity) {
        return severity != null && List.of("none", "mild", "moderate", "severe").contains(severity.toLowerCase());
    }
}