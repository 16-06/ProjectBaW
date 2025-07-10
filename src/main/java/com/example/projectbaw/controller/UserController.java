package com.example.projectbaw.controller;

import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService       userService;

    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto.RegisterDto request) {

        userService.registerUser(request);
        return ResponseEntity.ok("Successfully registered");

    }

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto.LoginDto requestDto) {

        if(!userService.isAccountEnabled(requestDto.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not activated or does not exist");
        }

        if(userService.isAccountBanned(requestDto.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is banned, check your email for more information");
        }

        Optional<String> token = userService.login(requestDto);

        if (token.isEmpty() && userService.isTwoFactorEnabled(requestDto.getUsername())) {
            return ResponseEntity.accepted()
                    .body("Two-factor authentication required, please provide the code sent to your email");
        }

        if (token.isPresent()) {

            return token.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
        }

        return ResponseEntity.badRequest().body("Login failed, please check your credentials or account activation status");
    }

    @PostMapping("/public/login/2fa")
    public ResponseEntity<?> login(@RequestBody UserDto.TwoFactorDto dto) {

        String verified = userService.verifyTwoFactorCode(dto.getUsername(), dto.getCode());
        return ResponseEntity.ok(verified);


    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getBasicInfo(@PathVariable Long id) {

        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserDto.ChangePassDto requestDto) {

        boolean result = userService.changePassword(requestDto.getPassword(),requestDto.getNewPassword());

        if(result)
            return ResponseEntity.ok("Successful password change");
        else
            return ResponseEntity.badRequest().body("Incorrect old password or new password ");
    }

    @GetMapping("/public/auth")
    public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {

        Map<String, Object> userInfo = userService.getAuthenticatedUserInfo(request);

        if (userInfo == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/check")
    public ResponseEntity<?> getUsersByAdmin() {

        List<UserDto.ResponseDto> users = userService.adminGetAllUsers();

        return ResponseEntity.ok(users);

    }

    @GetMapping("/public/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam String token) {

        boolean result = userService.activateAccount(token);

        if(!result){
            return ResponseEntity.badRequest().body("Invalid token");
        }

        return ResponseEntity.ok("Account successfully activated");

    }

    @PostMapping("/public/password-reset-request")
    public ResponseEntity<?> passwordResetRequestByEmail(@RequestBody UserDto.passwordResetRequestDto requestDto) {

            userService.PasswordResetByEmail(requestDto.getEmail());
            return ResponseEntity.ok("Password reset email sent");

    }

    @PostMapping("/public/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserDto.ResetPasswordDto resetPasswordDto) {

        boolean result = userService.resetPassword(resetPasswordDto.getResetPasswordToken(), resetPasswordDto.getNewPassword());

        if(result)
            return ResponseEntity.ok("Password successfully reset");
        else
            return ResponseEntity.badRequest().body("Invalid token or password too short");

    }


}
