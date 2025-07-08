package com.example.projectbaw.repository;

import com.example.projectbaw.model.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {

    Optional<UserSecurity> findByActivationToken(String token);

    Optional<UserSecurity> findByResetPasswordToken(String token);
}