package com.ebazar.ebazarapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ebazar.ebazarapi.dto.BrandDto;

public interface BrandService {

    Page<BrandDto> listBrands(String q, Pageable pageable);

    BrandDto getBrandById(Long id);

    BrandDto createBrand(BrandDto dto);

    BrandDto updateBrand(Long id, BrandDto dto);

    void deleteBrand(Long id);

    BrandDto changeActiveStatus(Long id, boolean active);
}