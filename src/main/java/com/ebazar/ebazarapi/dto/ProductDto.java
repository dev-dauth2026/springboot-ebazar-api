package com.ebazar.ebazarapi.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Product", description = "Product payload for admin and API clients")
public class ProductDto {
	
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@NotBlank(message = "name is required")
	@Size(max = 150, message = "name must be most 150 characters")
	private String name;
	
	@NotBlank(message = "slug is required")
	@Size(max = 180, message = "slug must be at most 180 characters")
	private String slug;
	
	@Size(max = 1000, message = "description must be at most 1000 characters")
	private String description;
	
	@NotNull(message = "price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
	@Digits(integer = 8, fraction = 2, message = "price must have up to 8 interger digits and 2 decimal digits")
	private BigDecimal price;
	
	@NotNull(message = "sku is required")
	@Size(max = 50, message = "sku must be at most 50 characters")
	private String sku;
	
	@NotNull(message = "stockQuantity is required")
	private Integer stockQuantity;
	
	private boolean active = true;
	
	// relations as IDs for writes
	@NotNull(message = "categoryId is required")
	private Long categoryId;
	
	private Long brandId;
	
	// Conveience read-only fields for UI display
	@JsonProperty(access = Access.READ_ONLY)
	private String categoryName;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String brandName;
	
	@JsonProperty(access = Access.READ_ONLY)
	private Instant createdAt;
	
	@JsonProperty(access = Access.READ_ONLY)
	private Instant updatedAt;
	
}
