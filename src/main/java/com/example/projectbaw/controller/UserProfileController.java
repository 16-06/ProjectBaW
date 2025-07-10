package com.example.projectbaw.controller;

import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

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

    @PutMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("photo") MultipartFile file) throws IOException {

        userProfileService.uploadImage(file.getBytes());

        return ResponseEntity.ok("Image uploaded successfully");
    }
}
