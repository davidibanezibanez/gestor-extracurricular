package com.university.activityservice.controller;

import com.university.activityservice.dto.ActivityDto;
import com.university.activityservice.dto.CreateActivityRequest;
import com.university.activityservice.dto.UpdateActivityRequest;
import com.university.activityservice.entity.ActivityType;
import com.university.activityservice.service.ActivityService;
import com.university.activityservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

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
    public ResponseEntity<List<ActivityDto>> getAllActivities(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // Para estudiantes y público, solo mostrar actividades habilitadas
        if (authHeader == null || !isAuthorized(authHeader, "READ_ACTIVITY")) {
            List<ActivityDto> activities = activityService.getEnabledActivities();
            return ResponseEntity.ok(activities);
        }

        // Para administradores, mostrar todas las actividades
        List<ActivityDto> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> getActivityById(@PathVariable Long id) {
        try {
            ActivityDto activity = activityService.getActivityById(id);
            return ResponseEntity.ok(activity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ActivityDto>> getActivitiesByType(@PathVariable ActivityType type) {
        List<ActivityDto> activities = activityService.getActivitiesByType(type);
        return ResponseEntity.ok(activities);
    }

    @PostMapping
    public ResponseEntity<ActivityDto> createActivity(
            @Valid @RequestBody CreateActivityRequest request,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "CREATE_ACTIVITY")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ActivityDto activity = activityService.createActivity(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(activity);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDto> updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody UpdateActivityRequest request,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "UPDATE_ACTIVITY")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ActivityDto activity = activityService.updateActivity(id, request);
            return ResponseEntity.ok(activity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "DELETE_ACTIVITY")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            activityService.deleteActivity(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ActivityDto> enableActivity(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "ENABLE_ACTIVITY")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ActivityDto activity = activityService.enableActivity(id);
            return ResponseEntity.ok(activity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ActivityDto> disableActivity(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "DISABLE_ACTIVITY")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ActivityDto activity = activityService.disableActivity(id);
            return ResponseEntity.ok(activity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}