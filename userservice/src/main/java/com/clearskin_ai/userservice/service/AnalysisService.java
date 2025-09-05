package com.clearskin_ai.userservice.service;

import com.clearskin_ai.userservice.api.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnalysisService {
    AnalysisResponseDto analyzeImage(MultipartFile file, Long userId);
    List<AnalysisHistoryResponseDto> getUserAnalysisHistory(Long userId);
    AnalysisCountDto getAnalysisCount();
    List<AnalysisResponseDto> getAnonymousAnalyses();
    AdminAnalysisPageResponse getAllAnalysisHistoryForAdmin(int page, int size);
    AnalysisResponseDto getAnalysisHistoryById(Long historyId, Long userId);
    void deleteAnalysisHistoryById(Long historyId);
}