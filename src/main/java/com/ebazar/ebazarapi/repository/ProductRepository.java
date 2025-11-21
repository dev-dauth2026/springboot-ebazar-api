package com.ebazar.ebazarapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ebazar.ebazarapi.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Detail page: /products/some-product-slug
    Optional<Product> findBySlugAndActiveTrue(String slug);

    // For uniqueness validation
    boolean existsBySku(String sku);
    boolean existsBySlug(String slug);

    // Basic product list (only active)
    Page<Product> findByActiveTrue(Pageable pageable);

    // Filter by category
    Page<Product> findByActiveTrueAndCategory_Slug(String categorySlug, Pageable pageable);

    // Filter by brand
    Page<Product> findByActiveTrueAndBrand_Slug(String brandSlug, Pageable pageable);

    // Simple search by name
    Page<Product> findByActiveTrueAndNameContainingIgnoreCase(String keyword, Pageable pageable);

    // Combination: category + brand + keyword (for search filters)
    Page<Product> findByActiveTrueAndCategory_SlugAndBrand_SlugAndNameContainingIgnoreCase(
            String categorySlug,
            String brandSlug,
            String keyword,
            Pageable pageable
    );

    // NEW: category + keyword
    Page<Product> findByActiveTrueAndCategory_SlugAndNameContainingIgnoreCase(
            String categorySlug,
            String keyword,
            Pageable pageable
    );

    // NEW: brand + keyword
    Page<Product> findByActiveTrueAndBrand_SlugAndNameContainingIgnoreCase(
            String brandSlug,
            String keyword,
            Pageable pageable
    );
}