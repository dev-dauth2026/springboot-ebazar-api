package com.ebazar.ebazarapi.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Brand", description = "Brand payload for admin and API clients")
public class BrandDto {
	
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@NotBlank(message = "name is required")
	@Size(max = 100, message = "name must be at most 100 characters")
	private String name;
	
	@NotBlank(message = "slug is required")
	@Size(max = 120, message = "slug must be at most 120 characters")
	private String slug;
	
	@Size(max = 500, message = "description must be at most 500 characters")
	private String description;
	
	// Admin can enable/disable a brand
	private boolean active = true;
	
	@JsonProperty(access = Access.READ_ONLY)
	private Instant createdAt;
	
	@JsonProperty(access = Access.READ_ONLY)
	private Instant updatedAt;
	
}
