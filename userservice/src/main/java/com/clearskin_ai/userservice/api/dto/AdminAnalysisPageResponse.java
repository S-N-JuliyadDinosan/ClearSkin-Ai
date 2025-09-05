package com.clearskin_ai.userservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAnalysisPageResponse {
    private List<AdminAnalysisResponseDto> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}
