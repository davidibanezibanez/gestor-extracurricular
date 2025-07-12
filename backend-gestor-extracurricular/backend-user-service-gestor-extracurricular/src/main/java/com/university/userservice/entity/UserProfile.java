package com.university.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    private String role;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    // Constructor personalizado para creación (sin id)
    public UserProfile(String username, String email, String role, String profilePicture) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
        this.isEnabled = true;
    }
}