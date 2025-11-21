package com.ebazar.ebazarapi.mapper;

import com.ebazar.ebazarapi.dto.CategoryDto;
import com.ebazar.ebazarapi.entity.Category;

public final class CategoryMapper {
	
	private CategoryMapper() {
		
	}
	
	public static CategoryDto toDto(Category entity) {
		if (entity == null) return null;
		
		return CategoryDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.active(entity.isActive())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdateAt())
				.build();
		
	}
	
	public static Category toEntity(CategoryDto dto) {
		if(dto == null) return null;
		
		Category category = new Category();
		category.setId(dto.getId());
		category.setName(dto.getName());
		category.setSlug(dto.getSlug());
		category.setDescription(dto.getDescription());
		category.setActive(dto.isActive());
		return category;
	}
}
