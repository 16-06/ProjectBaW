package com.example.projectbaw.service;

import com.example.projectbaw.model.AnalyticsLog;
import com.example.projectbaw.repository.AnalyticsLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final AnalyticsLogRepository analyticsLogRepository;


    public void logEvent(String event, String username, String ip, String endpoint) {
        log.info("Analytics | User: {}, Action: {}, IP: {}, Endpoint: {}", username, event, ip, endpoint);

        AnalyticsLog logEntry = AnalyticsLog.builder()
                .action(event)
                .username(username)
                .ipAddress(ip)
                .endpoint(endpoint)
                .timestamp(LocalDateTime.now())
                .build();

        analyticsLogRepository.save(logEntry);
    }
}
