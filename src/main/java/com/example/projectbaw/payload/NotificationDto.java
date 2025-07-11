package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class NotificationDto {

    @Getter
    @Setter
    public static class ResponseDto {

        private String title;
        private String message;
        private LocalDateTime createdAt;
    }

}
