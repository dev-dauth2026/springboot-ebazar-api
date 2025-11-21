package com.ebazar.ebazarapi.service.impl;

import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ebazar.ebazarapi.dto.BrandDto;
import com.ebazar.ebazarapi.entity.Brand;
import com.ebazar.ebazarapi.exception.DuplicateResourceException;
import com.ebazar.ebazarapi.exception.ResourceNotFoundException;
import com.ebazar.ebazarapi.repository.BrandRepository;
import com.ebazar.ebazarapi.service.BrandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public Page<BrandDto> listBrands(String q, Pageable pageable) {

        Page<Brand> page;

        if (StringUtils.hasText(q)) {
            // search by name, only active
            page = brandRepository.findByActiveTrueAndNameContainingIgnoreCase(q.trim(), pageable);
        } else {
            // all active brands
            page = brandRepository.findByActiveTrue(pageable);
        }

        return page.map(this::toDto);
    }

    @Override
    public BrandDto getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand with id %d not found".formatted(id)
                ));

        return toDto(brand);
    }

    @Override
    public BrandDto createBrand(BrandDto dto) {

        // validate uniqueness for name & slug
        validateUniqueNameAndSlug(dto.getName(), dto.getSlug(), null);

        Brand brand = new Brand();
        brand.setName(dto.getName().trim());
        brand.setSlug(dto.getSlug().trim());
        brand.setDescription(dto.getDescription());
        brand.setActive(dto.isActive());

        Brand saved = brandRepository.save(brand);
        return toDto(saved);
    }

    @Override
    public BrandDto updateBrand(Long id, BrandDto dto) {

        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand with id %d not found".formatted(id)
                ));

        // validate uniqueness but ignore current record’s own name/slug
        validateUniqueNameAndSlug(dto.getName(), dto.getSlug(), id);

        existing.setName(dto.getName().trim());
        existing.setSlug(dto.getSlug().trim());
        existing.setDescription(dto.getDescription());
        existing.setActive(dto.isActive());

        Brand saved = brandRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteBrand(Long id) {
        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand with id %d not found".formatted(id)
                ));

        brandRepository.delete(existing);
    }

    @Override
    public BrandDto changeActiveStatus(Long id, boolean active) {

        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand with id %d not found".formatted(id)
                ));

        existing.setActive(active);
        Brand saved = brandRepository.save(existing);
        return toDto(saved);
    }

    // -------------- private helpers --------------

    private void validateUniqueNameAndSlug(String name, String slug, Long currentId) {

        if (name != null) {
            boolean nameExists = brandRepository.existsByNameIgnoreCase(name.trim());

            if (nameExists) {
                // If updating, allow same name for the same record
                if (currentId == null || isDifferentBrand(currentId, name, true)) {
                    throw new DuplicateResourceException(
                            "Brand name '%s' is already in use".formatted(name)
                    );
                }
            }
        }

        if (slug != null) {
            boolean slugExists = brandRepository.existsBySlugIgnoreCase(slug.trim());

            if (slugExists) {
                if (currentId == null || isDifferentBrand(currentId, slug, false)) {
                    throw new DuplicateResourceException(
                            "Brand slug '%s' is already in use".formatted(slug)
                    );
                }
            }
        }
    }

    /**
     * Very simple check:
     * - load the brand by id
     * - compare its current name/slug with the incoming one
     * If they are same, it's not a real duplicate for this record.
     */
    private boolean isDifferentBrand(Long id, String value, boolean checkName) {
        return brandRepository.findById(id)
                .map(b -> {
                    if (checkName) {
                        return !b.getName().equalsIgnoreCase(value.trim());
                    } else {
                        return !b.getSlug().equalsIgnoreCase(value.trim());
                    }
                })
                .orElse(true);
    }

    private BrandDto toDto(Brand brand) {
        if (brand == null) return null;

        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setSlug(brand.getSlug());
        dto.setDescription(brand.getDescription());
        dto.setActive(brand.isActive());
        dto.setCreatedAt(brand.getCreatedAt());
        dto.setUpdatedAt(brand.getUpdateAt()); // BaseEntity has updateAt

        return dto;
    }
}