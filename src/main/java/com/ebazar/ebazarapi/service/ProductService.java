package com.ebazar.ebazarapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ebazar.ebazarapi.dto.ProductDto;

public interface ProductService {

    // Public catalogue (with filters)
    Page<ProductDto> listProducts(
            String categorySlug,
            String brandSlug,
            String keyword,
            Pageable pageable
    );

    // Public detail pages
    ProductDto getProductBySlug(String slug);

    // Admin detail by ID
    ProductDto getProductById(Long id);

    // Admin create
    ProductDto createProduct(ProductDto dto);

    // Admin update
    ProductDto updateProduct(Long id, ProductDto dto);

    // Admin change active status
    ProductDto changeActiveStatus(Long id, boolean active);

    // Admin delete
    void deleteProduct(Long id);
}