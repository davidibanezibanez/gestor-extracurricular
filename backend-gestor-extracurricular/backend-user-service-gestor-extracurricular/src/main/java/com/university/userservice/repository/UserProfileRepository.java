package com.university.userservice.repository;

import com.university.userservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUsername(String username);
    List<UserProfile> findByRole(String role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}