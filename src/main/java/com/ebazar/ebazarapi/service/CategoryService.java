package com.ebazar.ebazarapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ebazar.ebazarapi.dto.CategoryDto;

public interface CategoryService {

    Page<CategoryDto> listCategories(String q, Pageable pageable);

    CategoryDto getCategoryById(Long id);

    CategoryDto createCategory(CategoryDto dto);

    CategoryDto updateCategory(Long id, CategoryDto dto);

    void deleteCategory(Long id);

    CategoryDto changeActiveStatus(Long id, boolean active);
}