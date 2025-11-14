package com.ebazar.ebazarapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "brands")
public class Brand extends BaseEntity{
	
	@Column(nullable = false, unique = true, length = 100)
	private String name;
	
	@Column(nullable = false, unique = true, length = 120)
	private String slug;
	
	@Column(length = 500)
	private String description;
	
	@Column(nullable =false)
	private boolean active = true;
}
