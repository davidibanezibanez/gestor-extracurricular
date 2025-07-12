package com.university.activityservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_activity", nullable = false)
    @NotBlank
    private String nameActivity;

    @Column(name = "activity_date", nullable = false)
    @NotNull
    @Future
    private LocalDate activityDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_activity", nullable = false)
    @NotNull
    private ActivityType typeActivity;

    @Column(name = "start_time", nullable = false)
    @NotNull
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull
    private LocalTime endTime;

    @Column(name = "maximum_enroll_date", nullable = false)
    @NotNull
    private LocalDate maximumEnrollDate;

    @Column(name = "maximum_quotas", nullable = false)
    @Min(1)
    private Integer maximumQuotas;

    @Column(nullable = false)
    @NotBlank
    private String organizer;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "activity_picture")
    private String activityPicture;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();
}