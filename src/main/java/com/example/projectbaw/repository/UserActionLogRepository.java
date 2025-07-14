package com.example.projectbaw.repository;

import com.example.projectbaw.model.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long> {
}