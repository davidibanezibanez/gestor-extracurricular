package com.university.activityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private Long id;
    private Long activityId;
    private String activityName;
    private String studentUsername;
    private LocalDateTime enrollmentDate;
    private Boolean isCompleted;
}