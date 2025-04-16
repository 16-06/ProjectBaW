package com.example.projectbaw.controller;

import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> changePassword(@RequestBody UserDto.RequestDto requestDto,@RequestBody String newPassword) {

        boolean result = userService.changePassword(requestDto.getUsername(),requestDto.getPassword(),newPassword);

        if(result)
            return ResponseEntity.ok("success");
        else
            return ResponseEntity.badRequest().body("Nieprawidłowe dane");
    }


}
