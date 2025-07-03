package com.example.projectbaw.controller;

import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService       userService;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserDto.RegisterDto request) {

        userService.registerUser(request);
        return ResponseEntity.ok("Successfully registered");

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.RequestDto requestDto) {

        return userService.login(requestDto.getUsername(), requestDto.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("Invalid credentials or account not activated"));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getBasicInfo(@PathVariable Long id) {

        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody UserDto.ChangePassDto requestDto) {

        boolean result = userService.changePassword(requestDto.getPassword(),requestDto.getNewPassword());

        if(result)
            return ResponseEntity.ok("Successful password change");
        else
            return ResponseEntity.badRequest().body("Incorrect old password or new password ");
    }

    @GetMapping("/auth")
    public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {

        Map<String, Object> userInfo = userService.getAuthenticatedUserInfo(request);

        if (userInfo == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/check")
    public ResponseEntity<?> isAdmin() {

        List<UserDto.ResponseDto> users = userService.isAdmin();

        return ResponseEntity.ok(users);

    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam String token) {

        boolean result = userService.activateAccount(token);

        if(!result){
            return ResponseEntity.badRequest().body("Invalid token");
        }

        return ResponseEntity.ok("Account successfully activated");

    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<?> passwordResetRequestByEmail(@RequestBody UserDto.passwordResetRequestDto requestDto) {

            userService.PasswordResetByEmail(requestDto.getEmail());
            return ResponseEntity.ok("Password reset email sent");

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserDto.ResetPasswordDto resetPasswordDto) {

        boolean result = userService.resetPassword(resetPasswordDto.getResetPasswordToken(), resetPasswordDto.getNewPassword());

        if(result)
            return ResponseEntity.ok("Password successfully reset");
        else
            return ResponseEntity.badRequest().body("Invalid token or password too short");

    }


}
