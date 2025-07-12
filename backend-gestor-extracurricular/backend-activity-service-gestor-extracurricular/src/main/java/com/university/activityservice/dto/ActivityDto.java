package com.university.activityservice.dto;

import com.university.activityservice.entity.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private Long id;
    private String nameActivity;
    private LocalDate activityDate;
    private ActivityType typeActivity;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate maximumEnrollDate;
    private Integer maximumQuotas;
    private String organizer;
    private String description;
    private String activityPicture;
    private Boolean isEnabled;
    private Boolean isCompleted;
    private Integer currentEnrollments;
    private Boolean canEnroll;
}