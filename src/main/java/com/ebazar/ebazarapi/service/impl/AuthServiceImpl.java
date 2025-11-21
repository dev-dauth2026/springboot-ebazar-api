package com.ebazar.ebazarapi.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ebazar.ebazarapi.dto.UserRegistrationDto;
import com.ebazar.ebazarapi.dto.UserResponseDto;
import com.ebazar.ebazarapi.entity.User;
import com.ebazar.ebazarapi.exception.DuplicateResourceException;
import com.ebazar.ebazarapi.repository.UserRepository;
import com.ebazar.ebazarapi.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationDto dto) {

        // 1) check email uniqueness
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail().trim())) {
            throw new DuplicateResourceException(
                    "User with email '%s' already exists".formatted(dto.getEmail())
            );
        }

        // 2) create entity + hash password
        User user = User.builder()
                .firstName(dto.getFirstName().trim())
                .lastName(dto.getLastName().trim())
                .email(dto.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(dto.getPassword()))
                .active(true)
                .role("ROLE_USER")
                .build();

        User saved = userRepository.save(user);

        // 3) map to response dto (no password)
        return toResponseDto(saved);
    }

    private UserResponseDto toResponseDto(User user) {
        if (user == null) return null;

        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .active(user.isActive())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdateAt())
                .build();
    }
}