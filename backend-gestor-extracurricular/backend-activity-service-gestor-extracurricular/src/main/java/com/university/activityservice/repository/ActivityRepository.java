package com.university.activityservice.repository;

import com.university.activityservice.entity.Activity;
import com.university.activityservice.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByIsEnabledTrue();

    List<Activity> findByTypeActivityAndIsEnabledTrue(ActivityType typeActivity);

    @Query("SELECT a FROM Activity a WHERE a.activityDate = :date AND a.isCompleted = false")
    List<Activity> findActivitiesByDateAndNotCompleted(@Param("date") LocalDate date);

    @Query("SELECT a FROM Activity a WHERE a.maximumEnrollDate < :currentDate AND a.isEnabled = true")
    List<Activity> findActivitiesWithExpiredEnrollment(@Param("currentDate") LocalDate currentDate);

    List<Activity> findByOrganizerContainingIgnoreCase(String organizer);
}