package com.university.activityservice.repository;

import com.university.activityservice.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentUsername(String studentUsername);

    List<Enrollment> findByActivityId(Long activityId);

    Optional<Enrollment> findByActivityIdAndStudentUsername(Long activityId, String studentUsername);

    boolean existsByActivityIdAndStudentUsername(Long activityId, String studentUsername);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.activity.id = :activityId")
    Integer countEnrollmentsByActivityId(@Param("activityId") Long activityId);

    @Query("SELECT e FROM Enrollment e WHERE e.activity.id = :activityId AND e.isCompleted = false")
    List<Enrollment> findIncompleteEnrollmentsByActivityId(@Param("activityId") Long activityId);
}