package com.example.projectbaw.service;

import com.example.projectbaw.model.UserActionLog;
import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.repository.UserActionLogRepository;
import com.example.projectbaw.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserActionLogService {

    private final UserActionLogRepository logRepository;
    private final UserProfileRepository userProfileRepository;

    public void log(String username, String action, String details) {

        Optional<UserProfile> optionalProfile = userProfileRepository.findByUserUsername(username);

        if (optionalProfile.isEmpty()) return;

        UserActionLog log = new UserActionLog();
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        log.setUserProfile(optionalProfile.get());

        logRepository.save(log);
    }}
