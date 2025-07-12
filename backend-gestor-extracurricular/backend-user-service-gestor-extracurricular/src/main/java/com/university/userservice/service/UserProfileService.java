package com.university.userservice.service;

import com.university.userservice.dto.CreateUserProfileRequest;
import com.university.userservice.dto.UpdateUserProfileRequest;
import com.university.userservice.dto.UserProfileDto;
import com.university.userservice.entity.UserProfile;
import com.university.userservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfileDto> getAllUsers() {
        return userProfileRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserProfileDto getUserByUsername(String username) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(profile);
    }

    public UserProfileDto createUser(CreateUserProfileRequest request) {
        if (userProfileRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        if (userProfileRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        UserProfile profile = new UserProfile(
                request.username(),
                request.email(),
                request.role(),
                request.profilePicture()
        );

        UserProfile saved = userProfileRepository.save(profile);
        return convertToDto(saved);
    }

    public UserProfileDto updateUser(String username, UpdateUserProfileRequest request) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.email() != null) {
            if (!request.email().equals(profile.getEmail()) &&
                    userProfileRepository.existsByEmail(request.email())) {
                throw new RuntimeException("Email already exists");
            }
            profile.setEmail(request.email());
        }

        if (request.profilePicture() != null) {
            profile.setProfilePicture(request.profilePicture());
        }

        UserProfile saved = userProfileRepository.save(profile);
        return convertToDto(saved);
    }

    public void deleteUser(String username) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userProfileRepository.delete(profile);
    }

    public UserProfileDto enableUser(String username) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        profile.setIsEnabled(true);
        UserProfile saved = userProfileRepository.save(profile);
        return convertToDto(saved);
    }

    public UserProfileDto disableUser(String username) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        profile.setIsEnabled(false);
        UserProfile saved = userProfileRepository.save(profile);
        return convertToDto(saved);
    }

    public List<UserProfileDto> getUsersByRole(String role) {
        return userProfileRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserProfileDto convertToDto(UserProfile profile) {
        return new UserProfileDto(
                profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                profile.getRole(),
                profile.getProfilePicture(),
                profile.getIsEnabled()
        );
    }
}