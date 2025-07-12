package com.example.projectbaw.repository;

import com.example.projectbaw.model.AnalyticsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsLogRepository extends JpaRepository<AnalyticsLog, Long> {
}