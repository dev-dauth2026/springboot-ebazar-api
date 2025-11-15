package com.ebazar.ebazarapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ebazar.ebazarapi.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // For category pages like /categories/electronics
    Optional<Category> findBySlug(String slug);

    // Prevent duplicate categories
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlugIgnoreCase(String slug);

    // List only active categories
    Page<Category> findByActiveTrue(Pageable pageable);

    // Search categories by name
    Page<Category> findByActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);
}