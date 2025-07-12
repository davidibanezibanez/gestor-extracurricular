package com.university.activityservice.dto;

import com.university.activityservice.entity.ActivityType;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateActivityRequest(
        String nameActivity,
        LocalDate activityDate,
        ActivityType typeActivity,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate maximumEnrollDate,
        Integer maximumQuotas,
        String organizer,
        String description,
        String activityPicture
) {}