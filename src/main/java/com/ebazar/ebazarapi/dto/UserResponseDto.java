package com.ebazar.ebazarapi.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "User", description = "Safe user representation (no password)")
public class UserResponseDto {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    private boolean active;
    private String role;

    @JsonProperty(access = Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = Access.READ_ONLY)
    private Instant updatedAt;
}