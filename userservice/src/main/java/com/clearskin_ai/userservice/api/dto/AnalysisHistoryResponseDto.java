package com.clearskin_ai.userservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisHistoryResponseDto {
    private Long historyId;
    private String severity;
    private String diagnosis;
    private Timestamp analysisTime;
    private double confidence;  // added
}

