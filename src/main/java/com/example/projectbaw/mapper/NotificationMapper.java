package com.example.projectbaw.mapper;

import com.example.projectbaw.model.Notification;
import com.example.projectbaw.payload.NotificationDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface NotificationMapper {


    NotificationDto.ResponseDto toResponseDto(Notification notification);
}
