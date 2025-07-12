package com.university.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserProfileRequest(
        @NotBlank String username,
        @Email @NotBlank String email,
        String role,
        String profilePicture
) {}