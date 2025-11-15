package com.ebazar.ebazarapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ebazar.ebazarapi.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    // Find by unique slug (for SEO-friendly URLs)
    Optional<Brand> findBySlug(String slug);

    // Check duplicates when creating/updating
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlugIgnoreCase(String slug);

    // List only active brands (for dropdowns/filters)
    Page<Brand> findByActiveTrue(Pageable pageable);

    // Search by name (e.g. brand list page with search box)
    Page<Brand> findByActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);
}