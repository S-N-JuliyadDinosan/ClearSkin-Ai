package com.clearskin_ai.userservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String name;
    private String brand;
    private String productLink;
    private String productImageLink;
    private String productDescription;
    private String skinType;
    private Timestamp addedDate;
}
