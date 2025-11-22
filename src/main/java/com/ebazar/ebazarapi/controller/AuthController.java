package com.ebazar.ebazarapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebazar.ebazarapi.dto.UserRegistrationDto;
import com.ebazar.ebazarapi.dto.UserResponseDto;
import com.ebazar.ebazarapi.security.CustomUserDetails;
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
    
    @Operation(summary = "Get currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        String email;
        if (principal instanceof CustomUserDetails cud) {
            email = cud.getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            email = ud.getUsername();
        } else {
            email = authentication.getName();
        }

        UserResponseDto user = authService.getCurrentUser(email);
        return ResponseEntity.ok(user);
    }
}