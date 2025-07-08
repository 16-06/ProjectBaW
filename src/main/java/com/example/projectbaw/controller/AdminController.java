package com.example.projectbaw.controller;

import com.example.projectbaw.payload.AdminDto;
import com.example.projectbaw.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

   private final AdminService adminService;

    @PostMapping("/change-role")
    public ResponseEntity<String> changeRole(@RequestBody AdminDto.ChangeRoleDto changeRoleDto) {

        adminService.changeUserRole(changeRoleDto);
        return ResponseEntity.ok("Role changed successfully");

    }

    @PostMapping("/block-user")
    public ResponseEntity<String> blockUser(@RequestBody AdminDto.BanUserDto banUserDto) {

        adminService.blockUser(banUserDto);
        return ResponseEntity.ok("User blocked successfully until " +
                LocalDateTime.now().plus(banUserDto.getDurationAmount(), banUserDto.getDurationUnit()));

    }

    @PostMapping("/unblock-user")
    public ResponseEntity<String> unblockUser(@RequestBody Long id) {

        adminService.unblockUser(id);
        return ResponseEntity.ok("User unblocked successfully");

    }


}


