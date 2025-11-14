package com.ebazar.ebazarapi.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "products")
public class Product extends BaseEntity{
	
	@Column(nullable = false, length = 150)
	private String name;
	
	@Column(nullable = false, unique = true, length = 180)
	private String slug;
	
	@Column(length = 1000)
	private String description;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;
	
	@Column(nullable = false, unique = true, length = 50)
	private String sku;
	
	@Column(nullable = false)
	private Integer stockQuantity;
	
	@Column(nullable = false)
	private boolean active = true;
	
	// relationships
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "brand_id")
	private Brand brand;
	

}
