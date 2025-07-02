package com.example.projectbaw.repository;

import com.example.projectbaw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findByActivationToken(String token);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
}
