package com.example.projectbaw.payload;


import com.example.projectbaw.enums.ContentType;
import com.example.projectbaw.enums.ResolutionStatus;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ReportDto {

    @Getter
    @Setter
    public static class ResponseDto {

        Long id;
        Long reporterId;
        Long contentId;
        String reason;
        ContentType contentType;
        ResolutionStatus status;
        LocalDateTime createdAt;
    }

    @Getter
    @Setter
    public static class RequestDto {

        Long contentId;
        String reason;
        ContentType contentType;
    }

    @Getter
    @Setter
    public static class ChangeStatusDto {

        Long id;
        ResolutionStatus status;
    }


}
