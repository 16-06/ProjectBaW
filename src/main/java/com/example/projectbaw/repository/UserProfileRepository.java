package com.example.projectbaw.repository;

import com.example.projectbaw.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserUsername(String userUsername);
}
