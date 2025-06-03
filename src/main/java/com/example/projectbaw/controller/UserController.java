package com.example.projectbaw.controller;

import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserDto.RequestDto request) {

        userService.registerUser(request);
        return ResponseEntity.ok("success");

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.RequestDto requestDto) {

        return userService.login(requestDto.getUsername(), requestDto.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    return ResponseEntity.ok(token);
                })
                .orElse(ResponseEntity.badRequest().body("nie udane logowanie"));
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


}
