package com.clearskin_ai.userservice.api.controller;

import com.clearskin_ai.userservice.api.dto.AnalysisCountDto;
import com.clearskin_ai.userservice.api.dto.AnalysisResponseDto;
import com.clearskin_ai.userservice.service.AnalysisService;
import com.clearskin_ai.userservice.util.EndpointBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<AnalysisResponseDto>> getAnalysisHistory(@RequestParam("userId") Long userId) {
        List<AnalysisResponseDto> history = analysisService.getAnalysisHistory(userId);
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
}