package com.ebazar.ebazarapi.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ebazar.ebazarapi.dto.ProductDto;
import com.ebazar.ebazarapi.entity.Brand;
import com.ebazar.ebazarapi.entity.Category;
import com.ebazar.ebazarapi.entity.Product;
import com.ebazar.ebazarapi.exception.DuplicateResourceException;
import com.ebazar.ebazarapi.exception.ResourceNotFoundException;
import com.ebazar.ebazarapi.repository.BrandRepository;
import com.ebazar.ebazarapi.repository.CategoryRepository;
import com.ebazar.ebazarapi.repository.ProductRepository;
import com.ebazar.ebazarapi.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    public Page<ProductDto> listProducts(String q, Pageable pageable) {
        Page<Product> page;

        if (StringUtils.hasText(q)) {
            page = productRepository
                    .findByActiveTrueAndNameContainingIgnoreCase(q.trim(), pageable);
        } else {
            page = productRepository.findByActiveTrue(pageable);
        }

        return page.map(this::toDto);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id %d not found".formatted(id)
                ));

        return toDto(product);
    }

    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with slug '%s' not found".formatted(slug)
                ));

        return toDto(product);
    }

    @Override
    public ProductDto createProduct(ProductDto dto) {

        validateUniqueSkuAndSlug(dto.getSku(), dto.getSlug(), null);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found".formatted(dto.getCategoryId())
                ));

        Brand brand = null;
        if (dto.getBrandId() != null) {
            brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Brand with id %d not found".formatted(dto.getBrandId())
                    ));
        }

        Product product = new Product();
        product.setName(dto.getName().trim());
        product.setSlug(dto.getSlug().trim());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSku(dto.getSku().trim());
        product.setStockQuantity(dto.getStockQuantity());
        product.setActive(dto.isActive());
        product.setCategory(category);
        product.setBrand(brand);

        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id %d not found".formatted(id)
                ));

        validateUniqueSkuAndSlug(dto.getSku(), dto.getSlug(), id);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id %d not found".formatted(dto.getCategoryId())
                ));

        Brand brand = null;
        if (dto.getBrandId() != null) {
            brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Brand with id %d not found".formatted(dto.getBrandId())
                    ));
        }

        existing.setName(dto.getName().trim());
        existing.setSlug(dto.getSlug().trim());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setSku(dto.getSku().trim());
        existing.setStockQuantity(dto.getStockQuantity());
        existing.setActive(dto.isActive());
        existing.setCategory(category);
        existing.setBrand(brand);

        Product saved = productRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id %d not found".formatted(id)
                ));

        productRepository.delete(existing);
    }

    @Override
    public ProductDto changeActiveStatus(Long id, boolean active) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id %d not found".formatted(id)
                ));

        existing.setActive(active);
        Product saved = productRepository.save(existing);
        return toDto(saved);
    }

    // -------------- private helpers -----------------

    private void validateUniqueSkuAndSlug(String sku, String slug, Long currentId) {

        if (sku != null) {
            boolean skuExists = productRepository.existsBySku(sku.trim());
            if (skuExists) {
                if (currentId == null || isDifferentProductBySku(currentId, sku)) {
                    throw new DuplicateResourceException(
                            "Product SKU '%s' is already in use".formatted(sku)
                    );
                }
            }
        }

        if (slug != null) {
            boolean slugExists = productRepository.existsBySlug(slug.trim());
            if (slugExists) {
                if (currentId == null || isDifferentProductBySlug(currentId, slug)) {
                    throw new DuplicateResourceException(
                            "Product slug '%s' is already in use".formatted(slug)
                    );
                }
            }
        }
    }

    private boolean isDifferentProductBySku(Long id, String sku) {
        return productRepository.findById(id)
                .map(p -> !p.getSku().equalsIgnoreCase(sku.trim()))
                .orElse(true);
    }

    private boolean isDifferentProductBySlug(Long id, String slug) {
        return productRepository.findById(id)
                .map(p -> !p.getSlug().equalsIgnoreCase(slug.trim()))
                .orElse(true);
    }

    private ProductDto toDto(Product product) {
        if (product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setSku(product.getSku());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setActive(product.isActive());

        // Relations as IDs + names
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getBrand() != null) {
            dto.setBrandId(product.getBrand().getId());
            dto.setBrandName(product.getBrand().getName());
        }

        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdateAt()); // from BaseEntity

        return dto;
    }
}