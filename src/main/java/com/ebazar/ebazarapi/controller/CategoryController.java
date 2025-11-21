package com.ebazar.ebazarapi.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ebazar.ebazarapi.dto.CategoryDto;
import com.ebazar.ebazarapi.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Categories",
    description = "CRUD operations for product categories (admin + API clients)"
)
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
        summary = "List categories (paginated)",
        description = "Returns a page of active categories. Supports pagination and optional text search by name."
    )
    @GetMapping
    public ResponseEntity<Page<CategoryDto>> listCategories(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable,

            @Parameter(description = "Optional search text to match category name (case-insensitive)")
            @RequestParam(required = false) String q
    ) {
        Page<CategoryDto> page = categoryService.listCategories(q, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Parameter(description = "Category payload", required = true)
            @RequestBody CategoryDto dto
    ) {
        CategoryDto created = categoryService.createCategory(dto);
        var location = java.net.URI.create("/api/categories/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Update an existing category")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "Category ID") @PathVariable Long id,
            @Parameter(description = "Category payload", required = true)
            @RequestBody CategoryDto dto
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @Operation(summary = "Change category active status (enable/disable)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoryDto> changeCategoryStatus(
            @Parameter(description = "Category ID") @PathVariable Long id,
            @Parameter(description = "Whether the category should be active")
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok(categoryService.changeActiveStatus(id, active));
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}