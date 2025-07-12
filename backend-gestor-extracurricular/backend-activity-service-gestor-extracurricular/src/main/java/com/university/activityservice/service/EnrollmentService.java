package com.university.activityservice.service;

import com.university.activityservice.dto.EnrollmentDto;
import com.university.activityservice.entity.Activity;
import com.university.activityservice.entity.Enrollment;
import com.university.activityservice.repository.ActivityRepository;
import com.university.activityservice.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public List<EnrollmentDto> getStudentEnrollments(String studentUsername) {
        return enrollmentRepository.findByStudentUsername(studentUsername).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDto> getActivityEnrollments(Long activityId) {
        return enrollmentRepository.findByActivityId(activityId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EnrollmentDto enrollStudent(Long activityId, String studentUsername) {
        // Verificar que la actividad existe
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        // Verificar que la actividad está habilitada
        if (!activity.getIsEnabled()) {
            throw new RuntimeException("Activity is disabled");
        }

        // Verificar que la actividad no está completada
        if (activity.getIsCompleted()) {
            throw new RuntimeException("Activity is already completed");
        }

        // Verificar que no ha pasado la fecha límite de inscripción
        if (LocalDate.now().isAfter(activity.getMaximumEnrollDate())) {
            throw new RuntimeException("Enrollment period has ended");
        }

        // Verificar que el estudiante no está ya inscrito
        if (enrollmentRepository.existsByActivityIdAndStudentUsername(activityId, studentUsername)) {
            throw new RuntimeException("Student is already enrolled in this activity");
        }

        // Verificar que hay cupos disponibles
        Integer currentEnrollments = enrollmentRepository.countEnrollmentsByActivityId(activityId);
        if (currentEnrollments >= activity.getMaximumQuotas()) {
            throw new RuntimeException("Activity is full");
        }

        // Crear inscripción
        Enrollment enrollment = new Enrollment();
        enrollment.setActivity(activity);
        enrollment.setStudentUsername(studentUsername);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return convertToDto(saved);
    }

    public void unenrollStudent(Long activityId, String studentUsername) {
        Enrollment enrollment = enrollmentRepository.findByActivityIdAndStudentUsername(activityId, studentUsername)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Verificar que la actividad no está completada
        if (enrollment.getActivity().getIsCompleted()) {
            throw new RuntimeException("Cannot unenroll from completed activity");
        }

        enrollmentRepository.delete(enrollment);
    }

    public boolean isStudentEnrolled(Long activityId, String studentUsername) {
        return enrollmentRepository.existsByActivityIdAndStudentUsername(activityId, studentUsername);
    }

    private EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());
        dto.setActivityId(enrollment.getActivity().getId());
        dto.setActivityName(enrollment.getActivity().getNameActivity());
        dto.setStudentUsername(enrollment.getStudentUsername());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setIsCompleted(enrollment.getIsCompleted());
        return dto;
    }
}