package com.clearskin_ai.userservice.api.controller;

import com.clearskin_ai.userservice.api.dto.ProductRequestDto;
import com.clearskin_ai.userservice.api.dto.ProductResponseDto;
import com.clearskin_ai.userservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto dto) {
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/skin-type/{skinType}")
    public ResponseEntity<Page<ProductResponseDto>> getProductsBySkinType(
            @PathVariable String skinType,
            Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsBySkinType(skinType, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
                                                            @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
