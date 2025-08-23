package com.clearskin_ai.userservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private String brand;
    private String productLink;
    private String productImageLink;
    private String productDescription;
    private String skinType;
}
