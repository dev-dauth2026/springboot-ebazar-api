package com.ebazar.ebazarapi.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ebazar.ebazarapi.dto.CategoryDto;
import com.ebazar.ebazarapi.entity.Category;
import com.ebazar.ebazarapi.exception.DuplicateResourceException;
import com.ebazar.ebazarapi.exception.ResourceNotFoundException;
import com.ebazar.ebazarapi.repository.CategoryRepository;
import com.ebazar.ebazarapi.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryDto> listCategories(String q, Pageable pageable) {

        Page<Category> page;

        if (StringUtils.hasText(q)) {
            page = categoryRepository
                    .findByActiveTrueAndNameContainingIgnoreCase(q.trim(), pageable);
        } else {
            page = categoryRepository.findByActiveTrue(pageable);
        }

        return page.map(this::toDto);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found".formatted(id)
                ));

        return toDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {

        validateUniqueNameAndSlug(dto.getName(), dto.getSlug(), null);

        Category category = new Category();
        category.setName(dto.getName().trim());
        category.setSlug(dto.getSlug().trim());
        category.setDescription(dto.getDescription());
        category.setActive(dto.isActive());

        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found".formatted(id)
                ));

        validateUniqueNameAndSlug(dto.getName(), dto.getSlug(), id);

        existing.setName(dto.getName().trim());
        existing.setSlug(dto.getSlug().trim());
        existing.setDescription(dto.getDescription());
        existing.setActive(dto.isActive());

        Category saved = categoryRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found".formatted(id)
                ));

        categoryRepository.delete(existing);
    }

    @Override
    public CategoryDto changeActiveStatus(Long id, boolean active) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found".formatted(id)
                ));

        existing.setActive(active);
        Category saved = categoryRepository.save(existing);
        return toDto(saved);
    }

    // ---------------- private helpers ----------------

    private void validateUniqueNameAndSlug(String name, String slug, Long currentId) {

        if (name != null) {
            boolean nameExists = categoryRepository.existsByNameIgnoreCase(name.trim());

            if (nameExists) {
                if (currentId == null || isDifferentCategory(currentId, name, true)) {
                    throw new DuplicateResourceException(
                            "Category name '%s' is already in use".formatted(name)
                    );
                }
            }
        }

        if (slug != null) {
            boolean slugExists = categoryRepository.existsBySlugIgnoreCase(slug.trim());

            if (slugExists) {
                if (currentId == null || isDifferentCategory(currentId, slug, false)) {
                    throw new DuplicateResourceException(
                            "Category slug '%s' is already in use".formatted(slug)
                    );
                }
            }
        }
    }

    private boolean isDifferentCategory(Long id, String value, boolean checkName) {
        return categoryRepository.findById(id)
                .map(c -> {
                    if (checkName) {
                        return !c.getName().equalsIgnoreCase(value.trim());
                    } else {
                        return !c.getSlug().equalsIgnoreCase(value.trim());
                    }
                })
                .orElse(true);
    }

    private CategoryDto toDto(Category category) {
        if (category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setActive(category.isActive());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdateAt()); // from BaseEntity (updateAt)

        return dto;
    }
}