package com.ebazar.ebazarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(name = "UserRegistration", description = "Payload for new user registration")
public class UserRegistrationDto {

    @NotBlank(message = "firstName is required")
    @Size(max = 100, message = "firstName must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(max = 100, message = "lastName must be at most 100 characters")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 120, message = "email must be at most 120 characters")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
    private String password;
}