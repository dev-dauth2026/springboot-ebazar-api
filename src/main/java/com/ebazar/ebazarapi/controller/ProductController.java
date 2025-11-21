package com.ebazar.ebazarapi.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ebazar.ebazarapi.dto.ProductDto;
import com.ebazar.ebazarapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Products",
    description = "Product catalogue APIs with pagination, filters and admin CRUD"
)
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // -----------------------
    // Public catalogue list
    // -----------------------
    @Operation(
        summary = "List products (paginated, filterable)",
        description = """
            Returns a page of ACTIVE products.
            Optional filters:
            - categorySlug: filter by category
            - brandSlug: filter by brand
            - q: text search on product name
            """
    )
    @GetMapping
    public ResponseEntity<Page<ProductDto>> listProducts(
            @Parameter(description = "Category slug to filter products, e.g. 'electronics'")
            @RequestParam(required = false) String categorySlug,

            @Parameter(description = "Brand slug to filter products, e.g. 'samsung'")
            @RequestParam(required = false) String brandSlug,

            @Parameter(description = "Free text search to match product name")
            @RequestParam(required = false) String q,

            @ParameterObject
            @PageableDefault(page = 0, size = 12, sort = "name") Pageable pageable
    ) {
        Page<ProductDto> page = productService.listProducts(categorySlug, brandSlug, q, pageable);
        return ResponseEntity.ok(page);
    }

    // -----------------------
    // Public detail by slug
    // -----------------------
    @Operation(
        summary = "Get product by slug (public)",
        description = "Used by product detail pages like /products/some-product-slug"
    )
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDto> getBySlug(
            @Parameter(description = "Product slug, e.g. 'iphone-16-pro'") @PathVariable String slug
    ) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }

    // -----------------------
    // Admin detail by ID
    // -----------------------
    @Operation(summary = "Get product by ID (admin)")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(
            @Parameter(description = "Product ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // -----------------------
    // Admin create
    // -----------------------
    @Operation(summary = "Create a new product (admin)")
    @PostMapping
    public ResponseEntity<ProductDto> create(
            @Parameter(description = "Product payload", required = true)
            @RequestBody ProductDto dto
    ) {
        ProductDto created = productService.createProduct(dto);
        var location = java.net.URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    // -----------------------
    // Admin update
    // -----------------------
    @Operation(summary = "Update an existing product (admin)")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "Product payload", required = true)
            @RequestBody ProductDto dto
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    // -----------------------
    // Admin enable/disable
    // -----------------------
    @Operation(summary = "Change product active status (admin)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductDto> changeStatus(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "Whether the product should be active") @RequestParam boolean active
    ) {
        return ResponseEntity.ok(productService.changeActiveStatus(id, active));
    }

    // -----------------------
    // Admin delete
    // -----------------------
    @Operation(summary = "Delete a product (admin)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Product ID") @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}