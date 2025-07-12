package com.university.userservice.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserProfileRequest(
        @Email String email,
        String profilePicture
) {}