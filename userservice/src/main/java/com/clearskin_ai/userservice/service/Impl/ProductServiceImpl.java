package com.clearskin_ai.userservice.service.impl;

import com.clearskin_ai.userservice.api.dto.ProductRequestDto;
import com.clearskin_ai.userservice.api.dto.ProductResponseDto;
import com.clearskin_ai.userservice.entity.Product;
import com.clearskin_ai.userservice.exception.product.ProductAlreadyExistsException;
import com.clearskin_ai.userservice.exception.product.ProductNotFoundException;
import com.clearskin_ai.userservice.repository.ProductRepository;
import com.clearskin_ai.userservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Optional<Product> existingProduct = productRepository.findByProductLink(dto.getProductLink());
        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException("Product already exists with link: " + dto.getProductLink());
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setProductLink(dto.getProductLink());
        product.setProductImageLink(dto.getProductImageLink());
        product.setProductDescription(dto.getProductDescription());
        product.setSkinType(dto.getSkinType());
        product.setAddedDate(new Timestamp(System.currentTimeMillis()));

        Product saved = productRepository.save(product);
        return mapToDto(saved);
    }


    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        return mapToDto(product);
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDto> dtos = products.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, products.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> getProductsBySkinType(String skinType, Pageable pageable) {
        Page<Product> products = productRepository.findBySkinType(skinType, pageable);
        List<ProductResponseDto> dtos = products.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, products.getTotalElements());
    }

    @Override
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setProductLink(dto.getProductLink());
        product.setProductImageLink(dto.getProductImageLink());
        product.setProductDescription(dto.getProductDescription());
        product.setSkinType(dto.getSkinType());

        Product updated = productRepository.save(product);
        return mapToDto(updated);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        productRepository.delete(product);
    }

    private ProductResponseDto mapToDto(Product product) {
        return new ProductResponseDto(
                product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getProductLink(),
                product.getProductImageLink(),
                product.getProductDescription(),
                product.getSkinType(),
                product.getAddedDate()
        );
    }
}
