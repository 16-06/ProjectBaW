package com.example.projectbaw.controller;

import com.example.projectbaw.payload.NotificationDto;
import com.example.projectbaw.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<List<NotificationDto.ResponseDto>> getNotifications() {

        List<NotificationDto.ResponseDto> notifications = notificationService.getUserNotifications();

        return ResponseEntity.ok(notifications);
    }
}
