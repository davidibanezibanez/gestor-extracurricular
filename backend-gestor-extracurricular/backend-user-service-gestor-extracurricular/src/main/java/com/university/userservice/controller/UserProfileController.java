package com.university.userservice.controller;

import com.university.userservice.dto.CreateUserProfileRequest;
import com.university.userservice.dto.UpdateUserProfileRequest;
import com.university.userservice.dto.UserProfileDto;
import com.university.userservice.service.AuthService;
import com.university.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private AuthService authService;

    private boolean isAuthorized(String authHeader, String requiredPermission) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        Map<String, Object> tokenValidation = authService.validateToken(authHeader);
        return (Boolean) tokenValidation.getOrDefault("valid", false);
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> getAllUsers(
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "READ_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<UserProfileDto> users = userProfileService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDto> getUserByUsername(
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "READ_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserProfileDto user = userProfileService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserProfileDto> createUser(
            @Valid @RequestBody CreateUserProfileRequest request,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "CREATE_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserProfileDto user = userProfileService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserProfileDto> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserProfileRequest request,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "UPDATE_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserProfileDto user = userProfileService.updateUser(username, request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "DELETE_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            userProfileService.deleteUser(username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{username}/enable")
    public ResponseEntity<UserProfileDto> enableUser(
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "ENABLE_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserProfileDto user = userProfileService.enableUser(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{username}/disable")
    public ResponseEntity<UserProfileDto> disableUser(
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "DISABLE_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserProfileDto user = userProfileService.disableUser(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserProfileDto>> getUsersByRole(
            @PathVariable String role,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "READ_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<UserProfileDto> users = userProfileService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
}