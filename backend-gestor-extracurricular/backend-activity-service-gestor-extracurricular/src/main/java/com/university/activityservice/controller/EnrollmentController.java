package com.university.activityservice.controller;

import com.university.activityservice.dto.EnrollmentDto;
import com.university.activityservice.service.AuthService;
import com.university.activityservice.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private AuthService authService;

    private boolean isAuthorized(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        Map<String, Object> tokenValidation = authService.validateToken(authHeader);
        return (Boolean) tokenValidation.getOrDefault("valid", false);
    }

    private String getUsernameFromToken(String authHeader) {
        Map<String, Object> tokenValidation = authService.validateToken(authHeader);
        return (String) tokenValidation.get("username");
    }

    @GetMapping("/student/{username}")
    public ResponseEntity<List<EnrollmentDto>> getStudentEnrollments(
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Verificar que el usuario puede ver sus propias inscripciones o es admin
        String tokenUsername = getUsernameFromToken(authHeader);
        if (!username.equals(tokenUsername)) {
            // Aquí podrías verificar si es admin, por simplicidad solo permitimos ver las propias
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<EnrollmentDto> enrollments = enrollmentService.getStudentEnrollments(username);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<EnrollmentDto>> getActivityEnrollments(
            @PathVariable Long activityId,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<EnrollmentDto> enrollments = enrollmentService.getActivityEnrollments(activityId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping("/activity/{activityId}")
    public ResponseEntity<EnrollmentDto> enrollStudent(
            @PathVariable Long activityId,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = getUsernameFromToken(authHeader);

        try {
            EnrollmentDto enrollment = enrollmentService.enrollStudent(activityId, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/activity/{activityId}")
    public ResponseEntity<Void> unenrollStudent(
            @PathVariable Long activityId,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = getUsernameFromToken(authHeader);

        try {
            enrollmentService.unenrollStudent(activityId, username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activity/{activityId}/check/{username}")
    public ResponseEntity<Map<String, Boolean>> checkEnrollment(
            @PathVariable Long activityId,
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isEnrolled = enrollmentService.isStudentEnrolled(activityId, username);
        return ResponseEntity.ok(Map.of("isEnrolled", isEnrolled));
    }
}