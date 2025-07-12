package com.university.activityservice.dto;

import com.university.activityservice.entity.ActivityType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateActivityRequest(
        @NotBlank String nameActivity,
        @NotNull @Future LocalDate activityDate,
        @NotNull ActivityType typeActivity,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @NotNull LocalDate maximumEnrollDate,
        @Min(1) Integer maximumQuotas,
        @NotBlank String organizer,
        String description,
        String activityPicture
) {}