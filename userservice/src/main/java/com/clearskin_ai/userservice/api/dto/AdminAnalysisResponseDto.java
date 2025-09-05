package com.clearskin_ai.userservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAnalysisResponseDto {
    private Long historyId;        // Analysis history ID
    private Long userId;           // Optional: show which user did the analysis
    private String severity;
    private double confidence;
    private String suggestion;
    private String diagnosis;
    private Timestamp analysisTime;
}
