package com.example.projectbaw.controller;

import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService       userService;
    private final UserRepository    userRepository;
    private final JwtUtil           jwtUtil;
    private final UserMapper        userMapper;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserDto.RegisterDto request) {

        userService.registerUser(request);
        return ResponseEntity.ok("Successfully registered");

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.RequestDto requestDto) {

        return userService.login(requestDto.getUsername(), requestDto.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    return ResponseEntity.ok(token);
                })
                .orElse(ResponseEntity.badRequest().body("Invalid credentials or account not activated"));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getBasicInfo(@PathVariable Long id) {

        return userService.getById(id).map(user -> {

            UserDto.ResponseDto responseDto = new UserMapper().toUserDto(user);
            return ResponseEntity.ok(responseDto);

        }).orElse(ResponseEntity.notFound().build());
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
        String username = (String) request.getAttribute("username");
        Long userId = (Long) request.getAttribute("UserId");

        if (username == null || userId == null) {
            return ResponseEntity.status(401).body("Unauthorized1");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userId);
        userInfo.put("username", username);

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/check")
    public ResponseEntity<?> isAdmin() {

        boolean isAdmin = userService.isAdmin();


        List<UserDto.ResponseDto> users = userService.getAllUsers()
                .stream()
                .map(userMapper::toUserDto)
                .toList();


        if (isAdmin) {
            return ResponseEntity.ok(users);
        } else {
            return  ResponseEntity.status(401).body("Unauthorized, not admin user access");
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam String token) {

        Optional<User> user = userRepository.findByActivationToken(token);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid token");
        }

        User confirmedUser = user.get();
        confirmedUser.setEnabledAccount(true);
        confirmedUser.setActivationToken(null);
        userRepository.save(confirmedUser);

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
