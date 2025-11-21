package com.ebazar.ebazarapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ebazar.ebazarapi.dto.ProductDto;

public interface ProductService {

    Page<ProductDto> listProducts(String q, Pageable pageable);

    ProductDto getProductById(Long id);

    ProductDto getProductBySlug(String slug);

    ProductDto createProduct(ProductDto dto);

    ProductDto updateProduct(Long id, ProductDto dto);

    void deleteProduct(Long id);

    ProductDto changeActiveStatus(Long id, boolean active);
}