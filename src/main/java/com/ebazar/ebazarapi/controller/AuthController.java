package com.ebazar.ebazarapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ebazar.ebazarapi.dto.UserRegistrationDto;
import com.ebazar.ebazarapi.dto.UserResponseDto;
import com.ebazar.ebazarapi.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Auth",
    description = "Authentication and user registration endpoints"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user (public)")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
            @Validated @RequestBody UserRegistrationDto dto
    ) {
        UserResponseDto created = authService.register(dto);
        var location = java.net.URI.create("/api/users/" + created.getId()); // future user detail URL
        return ResponseEntity.created(location).body(created);
    }
}