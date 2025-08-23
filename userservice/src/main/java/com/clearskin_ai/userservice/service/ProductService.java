package com.clearskin_ai.userservice.service;

import com.clearskin_ai.userservice.api.dto.ProductRequestDto;
import com.clearskin_ai.userservice.api.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto dto);
    ProductResponseDto getProductById(Long productId);
    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    Page<ProductResponseDto> getProductsBySkinType(String skinType, Pageable pageable);
    ProductResponseDto updateProduct(Long productId, ProductRequestDto dto);
    void deleteProduct(Long productId);
}
