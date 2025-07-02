package com.example.projectbaw.controller;


import com.example.projectbaw.mapper.UserProfileMapper;
import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.service.UserProfileService;
import com.example.projectbaw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserService userService;

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileDto.RequestDto request) {

        userProfileService.updateProfile(request);

        return ResponseEntity.ok("Profile updated successfully");

    }

    @GetMapping("/get")
    public ResponseEntity<?> getProfile(@RequestBody UserDto.getProfileDto request) {

        UserProfileDto.ResponseDto profile = userProfileService.getUserProfile(request.getUsername());

        return ResponseEntity.ok(profile);

    }
}
