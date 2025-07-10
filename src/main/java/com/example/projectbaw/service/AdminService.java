package com.example.projectbaw.service;

import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.AdminDto;
import com.example.projectbaw.repository.*;
import com.example.projectbaw.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final EmailConfirmationService emailConfirmationService;


    public void changeUserRole(AdminDto.ChangeRoleDto changeRoleDto){

        User user = userRepository.findById(changeRoleDto.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(user.getRole() == Role.ADMIN){
            throw new RuntimeException("Admin cannot be changed to another role");
        }

        if(user.getRole() == changeRoleDto.getNewRole()){
            throw new RuntimeException("User already has this role");
        }

        user.setRole(changeRoleDto.getNewRole());
        userRepository.save(user);

    }

    public void blockUser(AdminDto.BanUserDto banUserDto){

        User user = userRepository.findById(banUserDto.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(user.isBannedAccount()){
            throw new RuntimeException("User already blocked");
        }

        LocalDateTime banUntil = LocalDateTime.now().plus(banUserDto.getDurationAmount(), banUserDto.getDurationUnit());

        user.setBannedAccount(true);
        user.getSecurityData().setBanExpiryTime(banUntil);
        emailConfirmationService.sendBanInfo(user.getEmail(), banUntil.toString());
        userRepository.save(user);

    }

    public void unblockUser(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!user.isBannedAccount()){
            throw new RuntimeException("User is not blocked");
        }

        user.setBannedAccount(false);
        user.getSecurityData().setBanExpiryTime(null);
        userRepository.save(user);
    }





}
