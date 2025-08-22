package com.clearskin_ai.userservice.service;

import com.clearskin_ai.userservice.api.dto.AnalysisCountDto;
import com.clearskin_ai.userservice.api.dto.AnalysisResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnalysisService {
    AnalysisResponseDto analyzeImage(MultipartFile file, Long userId);
    List<AnalysisResponseDto> getAnalysisHistory(Long userId);
    AnalysisCountDto getAnalysisCount();
    List<AnalysisResponseDto> getAnonymousAnalyses();
}