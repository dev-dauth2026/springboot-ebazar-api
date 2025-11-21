package com.ebazar.ebazarapi.mapper;

import com.ebazar.ebazarapi.dto.BrandDto;
import com.ebazar.ebazarapi.entity.Brand;

public final class BrandMapper {
	
	private BrandMapper() {
		
	}
	
	public static BrandDto toDto(Brand entity) {
		if (entity == null) return null;
		
		return BrandDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.slug(entity.getSlug())
				.description(entity.getDescription())
				.active(entity.isActive())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdateAt())
				.build();
	}
	
	public static Brand toEntity(BrandDto dto) {
		if (dto == null) return null;
		
		Brand brand = new Brand();
		brand.setId(dto.getId());
		brand.setName(dto.getName());
		brand.setSlug(dto.getSlug());
		brand.setDescription(dto.getDescription());
		brand.setActive(dto.isActive());
		
		return brand;
		
	}
	
	
}
