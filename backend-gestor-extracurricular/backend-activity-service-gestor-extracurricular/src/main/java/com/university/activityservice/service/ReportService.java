package com.university.activityservice.service;

import com.university.activityservice.entity.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportService {

    @Value("${report.service.url}")
    private String reportServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendActivityCompletionReport(Activity activity, List<String> completedStudents) {
        try {
            Map<String, Object> reportData = new HashMap<>();
            reportData.put("activityId", activity.getId());
            reportData.put("activityName", activity.getNameActivity());
            reportData.put("activityDate", activity.getActivityDate());
            reportData.put("activityType", activity.getTypeActivity());
            reportData.put("organizer", activity.getOrganizer());
            reportData.put("completedStudents", completedStudents);
            reportData.put("totalCompletedStudents", completedStudents.size());
            reportData.put("completionDate", java.time.LocalDateTime.now());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(reportData, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    reportServiceUrl + "/reports/activity-completion",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.info("Activity completion report sent successfully for activity: {}", activity.getNameActivity());
        } catch (Exception e) {
            log.error("Failed to send activity completion report for activity: {}", activity.getNameActivity(), e);
        }
    }
}