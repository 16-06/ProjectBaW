package com.example.projectbaw.service;

import com.example.projectbaw.mapper.UserProfileMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.repository.UserProfileRepository;
import com.example.projectbaw.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    public void updateProfile(UserProfileDto.RequestDto profileDto) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElse(new UserProfile());

        UserProfile updatedProfile = userProfileMapper.toEntity(profileDto);
        updatedProfile.setId(user.getId());
        updatedProfile.setUser(user);
        userProfileRepository.save(updatedProfile);

    }

    public UserProfileDto.ResponseDto getUserProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));


        return userProfileMapper.toDto(profile);
    }

}

