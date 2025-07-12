package com.university.activityservice.service;

import com.university.activityservice.dto.ActivityDto;
import com.university.activityservice.dto.CreateActivityRequest;
import com.university.activityservice.dto.UpdateActivityRequest;
import com.university.activityservice.entity.Activity;
import com.university.activityservice.entity.ActivityType;
import com.university.activityservice.entity.Enrollment;
import com.university.activityservice.repository.ActivityRepository;
import com.university.activityservice.repository.EnrollmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ReportService reportService;

    public List<ActivityDto> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityDto> getEnabledActivities() {
        return activityRepository.findByIsEnabledTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityDto> getActivitiesByType(ActivityType type) {
        return activityRepository.findByTypeActivityAndIsEnabledTrue(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ActivityDto getActivityById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        return convertToDto(activity);
    }

    public ActivityDto createActivity(CreateActivityRequest request) {
        if (request.maximumEnrollDate().isAfter(request.activityDate())) {
            throw new RuntimeException("Maximum enroll date cannot be after activity date");
        }
        if (request.startTime().isAfter(request.endTime())) {
            throw new RuntimeException("Start time cannot be after end time");
        }

        Activity activity = new Activity();
        activity.setNameActivity(request.nameActivity());
        activity.setActivityDate(request.activityDate());
        activity.setTypeActivity(request.typeActivity());
        activity.setStartTime(request.startTime());
        activity.setEndTime(request.endTime());
        activity.setMaximumEnrollDate(request.maximumEnrollDate());
        activity.setMaximumQuotas(request.maximumQuotas());
        activity.setOrganizer(request.organizer());
        activity.setDescription(request.description());
        activity.setActivityPicture(request.activityPicture());

        Activity saved = activityRepository.save(activity);
        return convertToDto(saved);
    }

    public ActivityDto updateActivity(Long id, UpdateActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        if (request.nameActivity() != null) {
            activity.setNameActivity(request.nameActivity());
        }
        if (request.activityDate() != null) {
            activity.setActivityDate(request.activityDate());
        }
        if (request.typeActivity() != null) {
            activity.setTypeActivity(request.typeActivity());
        }
        if (request.startTime() != null) {
            activity.setStartTime(request.startTime());
        }
        if (request.endTime() != null) {
            activity.setEndTime(request.endTime());
        }
        if (request.maximumEnrollDate() != null) {
            activity.setMaximumEnrollDate(request.maximumEnrollDate());
        }
        if (request.maximumQuotas() != null) {
            activity.setMaximumQuotas(request.maximumQuotas());
        }
        if (request.organizer() != null) {
            activity.setOrganizer(request.organizer());
        }
        if (request.description() != null) {
            activity.setDescription(request.description());
        }
        if (request.activityPicture() != null) {
            activity.setActivityPicture(request.activityPicture());
        }

        Activity saved = activityRepository.save(activity);
        return convertToDto(saved);
    }

    public void deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        activityRepository.delete(activity);
    }

    public ActivityDto enableActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        activity.setIsEnabled(true);
        Activity saved = activityRepository.save(activity);
        return convertToDto(saved);
    }

    public ActivityDto disableActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        activity.setIsEnabled(false);
        Activity saved = activityRepository.save(activity);
        return convertToDto(saved);
    }

    // Tarea programada que se ejecuta diariamente a las 00:01
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional
    public void markCompletedActivities() {
        LocalDate today = LocalDate.now();
        List<Activity> activitiesToComplete = activityRepository.findActivitiesByDateAndNotCompleted(today.minusDays(1));

        for (Activity activity : activitiesToComplete) {
            // Marcar actividad como completada
            activity.setIsCompleted(true);
            activityRepository.save(activity);

            // Marcar inscripciones como completadas
            List<Enrollment> enrollments = enrollmentRepository.findIncompleteEnrollmentsByActivityId(activity.getId());
            for (Enrollment enrollment : enrollments) {
                enrollment.setIsCompleted(true);
            }
            enrollmentRepository.saveAll(enrollments);

            // Enviar reporte
            List<String> completedStudents = enrollments.stream()
                    .map(Enrollment::getStudentUsername)
                    .collect(Collectors.toList());

            if (!completedStudents.isEmpty()) {
                reportService.sendActivityCompletionReport(activity, completedStudents);
            }

            log.info("Activity '{}' marked as completed with {} students",
                    activity.getNameActivity(), completedStudents.size());
        }
    }

    private ActivityDto convertToDto(Activity activity) {
        ActivityDto dto = new ActivityDto();
        dto.setId(activity.getId());
        dto.setNameActivity(activity.getNameActivity());
        dto.setActivityDate(activity.getActivityDate());
        dto.setTypeActivity(activity.getTypeActivity());
        dto.setStartTime(activity.getStartTime());
        dto.setEndTime(activity.getEndTime());
        dto.setMaximumEnrollDate(activity.getMaximumEnrollDate());
        dto.setMaximumQuotas(activity.getMaximumQuotas());
        dto.setOrganizer(activity.getOrganizer());
        dto.setDescription(activity.getDescription());
        dto.setActivityPicture(activity.getActivityPicture());
        dto.setIsEnabled(activity.getIsEnabled());
        dto.setIsCompleted(activity.getIsCompleted());

        // Calcular inscripciones actuales
        Integer currentEnrollments = enrollmentRepository.countEnrollmentsByActivityId(activity.getId());
        dto.setCurrentEnrollments(currentEnrollments);

        // Verificar si se puede inscribir
        boolean canEnroll = activity.getIsEnabled() &&
                !activity.getIsCompleted() &&
                LocalDate.now().isBefore(activity.getMaximumEnrollDate()) &&
                currentEnrollments < activity.getMaximumQuotas();
        dto.setCanEnroll(canEnroll);

        return dto;
    }
}