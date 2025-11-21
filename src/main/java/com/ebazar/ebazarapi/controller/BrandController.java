package com.ebazar.ebazarapi.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ebazar.ebazarapi.dto.BrandDto;
import com.ebazar.ebazarapi.service.BrandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Brands",
    description = "CRUD operations for product brands (admin + API clients)"
)
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @Operation(
        summary = "List brands (paginated)",
        description = "Returns a page of active brands. Supports pagination and optional text search by name."
    )
    @GetMapping
    public ResponseEntity<Page<BrandDto>> listBrands(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable,

            @Parameter(description = "Optional search text to match brand name (case-insensitive)")
            @RequestParam(required = false) String q
    ) {
        Page<BrandDto> page = brandService.listBrands(q, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Get brand by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrandById(
            @Parameter(description = "Brand ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @Operation(summary = "Create a new brand")
    @PostMapping
    public ResponseEntity<BrandDto> createBrand(
            @Parameter(description = "Brand payload", required = true)
            @RequestBody BrandDto dto
    ) {
        BrandDto created = brandService.createBrand(dto);
        var location = java.net.URI.create("/api/brands/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Update an existing brand")
    @PutMapping("/{id}")
    public ResponseEntity<BrandDto> updateBrand(
            @Parameter(description = "Brand ID") @PathVariable Long id,
            @Parameter(description = "Brand payload", required = true)
            @RequestBody BrandDto dto
    ) {
        return ResponseEntity.ok(brandService.updateBrand(id, dto));
    }

    @Operation(summary = "Change brand active status (enable/disable)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BrandDto> changeBrandStatus(
            @Parameter(description = "Brand ID") @PathVariable Long id,
            @Parameter(description = "Whether the brand should be active")
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok(brandService.changeActiveStatus(id, active));
    }

    @Operation(summary = "Delete a brand")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(
            @Parameter(description = "Brand ID") @PathVariable Long id
    ) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}