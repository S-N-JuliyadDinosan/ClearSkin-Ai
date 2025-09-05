package com.clearskin_ai.userservice.api.controller;

import com.clearskin_ai.userservice.api.dto.*;
import com.clearskin_ai.userservice.service.AnalysisService;
import com.clearskin_ai.userservice.util.EndpointBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.USER_BASE_URL)
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/analysis")
    public ResponseEntity<AnalysisResponseDto> analyzeImage(@RequestParam("file") MultipartFile file,
                                                            @RequestParam(value = "userId", required = false) Long userId) {
        AnalysisResponseDto response = analysisService.analyzeImage(file, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/analysis/history")
    public ResponseEntity<List<AnalysisHistoryResponseDto>> getAnalysisHistory(@RequestParam("userId") Long userId) {
        List<AnalysisHistoryResponseDto> history = analysisService.getUserAnalysisHistory(userId);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }


    @GetMapping("/analysis/count")
    public ResponseEntity<AnalysisCountDto> getAnalysisCount() {
        AnalysisCountDto count = analysisService.getAnalysisCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/analysis/anonymous")
    public ResponseEntity<List<AnalysisResponseDto>> getAnonymousAnalyses() {
        List<AnalysisResponseDto> anonymousAnalyses = analysisService.getAnonymousAnalyses();
        return new ResponseEntity<>(anonymousAnalyses, HttpStatus.OK);
    }

    @GetMapping("/analysis/all")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResponseEntity<AdminAnalysisPageResponse> getAllAnalysisHistory(
            @RequestParam int page,
            @RequestParam int size) {

        AdminAnalysisPageResponse response = analysisService.getAllAnalysisHistoryForAdmin(page, size);
        return ResponseEntity.ok(response);
    }


    // 2️⃣ Get analysis history by historyId - admin/staff/user
    @GetMapping("/analysis/{historyId}")
    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    public ResponseEntity<AnalysisResponseDto> getAnalysisHistoryById(@PathVariable Long historyId,
                                                                      @RequestParam(value = "userId", required = false) Long userId) {
        AnalysisResponseDto history = analysisService.getAnalysisHistoryById(historyId, userId);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    // 3️⃣ Delete analysis history by historyId - admin/staff only
    @DeleteMapping("/analysis/{historyId}")
    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN')")
    public ResponseEntity<String> deleteAnalysisHistoryById(@PathVariable Long historyId) {
        analysisService.deleteAnalysisHistoryById(historyId);
        return new ResponseEntity<>("Analysis history deleted successfully", HttpStatus.OK);
    }
}