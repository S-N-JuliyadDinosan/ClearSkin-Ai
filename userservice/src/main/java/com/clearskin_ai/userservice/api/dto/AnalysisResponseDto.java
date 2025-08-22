package com.clearskin_ai.userservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResponseDto {
    private String severity;
    private String suggestion;
    private Timestamp analysisTime;
}