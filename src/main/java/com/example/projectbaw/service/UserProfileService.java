package com.example.projectbaw.service;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.mapper.UserProfileMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.repository.UserProfileRepository;
import com.example.projectbaw.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    public void updateProfile(UserProfileDto.RequestDto profileDto, CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElse(new UserProfile());

        profile.setFirstName(profileDto.getFirstName());
        profile.setLastName(profileDto.getLastName());
        profile.setBio(profileDto.getBio());
        profile.setInterests(profileDto.getInterests());
        profile.setUser(user);

        userProfileRepository.save(profile);

    }

    public UserProfileDto.ResponseDto getUserProfile(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));


        return userProfileMapper.toDto(profile);
    }

    public void uploadImage(byte[] imageData,CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        UserProfile userProfile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        if(userProfile != null){
            userProfile.setAvatarImage(imageData);
            userProfileRepository.save(userProfile);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }
    }

}

