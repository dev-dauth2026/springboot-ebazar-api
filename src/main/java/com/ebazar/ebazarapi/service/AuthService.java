package com.ebazar.ebazarapi.service;

import com.ebazar.ebazarapi.dto.UserRegistrationDto;
import com.ebazar.ebazarapi.dto.UserResponseDto;

public interface AuthService {

    UserResponseDto register(UserRegistrationDto dto);
}