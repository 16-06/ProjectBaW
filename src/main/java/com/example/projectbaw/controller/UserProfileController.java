package com.example.projectbaw.controller;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody UserProfileDto.RequestDto request,@AuthenticationPrincipal CustomUserDetails userDetails) {

        userProfileService.updateProfile(request,userDetails);

        return ResponseEntity.ok("Profile updated successfully");

    }

    @GetMapping("/public/{id}")
    public ResponseEntity<UserProfileDto.ResponseDto> getProfile(@PathVariable Long id) {

        UserProfileDto.ResponseDto profile = userProfileService.getUserProfile(id);

        return ResponseEntity.ok(profile);

    }

    @PutMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam("photo") MultipartFile file) throws IOException {

        userProfileService.uploadImage(file.getBytes(),userDetails);

        return ResponseEntity.ok("Image uploaded successfully");
    }
}
