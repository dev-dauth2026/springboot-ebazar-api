package com.ebazar.ebazarapi.mapper;

import com.ebazar.ebazarapi.dto.ProductDto;
import com.ebazar.ebazarapi.entity.Brand;
import com.ebazar.ebazarapi.entity.Category;
import com.ebazar.ebazarapi.entity.Product;

public final class ProductMapper {
	
	private ProductMapper() {
		
	}
	
	public static ProductDto toDto(Product entity) {
		if (entity ==null) return null;
		
		Category category = entity.getCategory();
		Brand brand = entity.getBrand();
		
		return ProductDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.slug(entity.getSlug())
				.description(entity.getDescription())
				.price(entity.getPrice())
				.sku(entity.getSku())
				.stockQuantity(entity.getStockQuantity())
				.active(entity.isActive())
				.categoryId(category != null ? category.getId() : null )
				.brandId(brand !=  null ? brand.getId() : null)
				.categoryName(category != null ? category.getName() : null)
				.brandName(brand != null ? brand.getName() : null)
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdateAt())
				.build();
	}
	
	public static Product toEntity(ProductDto dto) {
		if (dto == null) return null;
		
		Product product = new Product();
		product.setId(dto.getId());
		product.setName(dto.getName());
		product.setSlug(dto.getSlug());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setSku(dto.getSku());
		product.setStockQuantity(dto.getStockQuantity());
		product.setActive(dto.isActive());
		
		// category / brand will be set in the SErvice after loading drom DB
		return product;
	}
}
