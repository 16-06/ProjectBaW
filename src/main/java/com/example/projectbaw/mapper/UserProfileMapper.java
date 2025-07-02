package com.example.projectbaw.mapper;

import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.payload.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    public UserProfileDto.ResponseDto toDto(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        UserProfileDto.ResponseDto dto = new UserProfileDto.ResponseDto();

        dto.setId(userProfile.getId());
        dto.setFirstName(userProfile.getFirstName());
        dto.setLastName(userProfile.getLastName());
        dto.setBio(userProfile.getBio());
        dto.setAvatarImage(userProfile.getAvatarImage());
        dto.setInterests(userProfile.getInterests());

        return dto;
    }

    public UserProfile toEntity(UserProfileDto.RequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName(requestDto.getFirstName());
        userProfile.setLastName(requestDto.getLastName());
        userProfile.setBio(requestDto.getBio());
        userProfile.setAvatarImage(requestDto.getAvatarImage());
        userProfile.setInterests(requestDto.getInterests());

        return userProfile;
    }
}
