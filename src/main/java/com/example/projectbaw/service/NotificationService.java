package com.example.projectbaw.service;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.mapper.NotificationMapper;
import com.example.projectbaw.model.Notification;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.payload.NotificationDto;
import com.example.projectbaw.repository.NotificationRepository;
import com.example.projectbaw.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper;

    public void notifyUser(UserProfile userProfile, String title, String message) {

        if(userProfile == null || userProfile.getUser()  == null) {
            throw new IllegalArgumentException("User profile or user cannot be null");
        }

        Notification notification = new Notification();
        notification.setUserProfile(userProfile);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);

        NotificationDto.ResponseDto dto = notificationMapper.toResponseDto(notification);

        if(userProfile.isNotificationsEnabled())

            messagingTemplate.convertAndSendToUser(
                    userProfile.getUser().getUsername(),
                    "/queue/notifications",
                    dto
            );
    }

    public List<NotificationDto.ResponseDto> getUserNotificationsHistory(CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // In this case, don't have to find UserProfileId, inject userProfileRepository etc.
        // UserProfile entity MapsId from User entity,
        // So we can directly use user.getId() to find notifications,

        List<Notification> notifications = notificationRepository.findByUserProfileId((user.getId()));

        return notifications.stream()
                .map(notificationMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
