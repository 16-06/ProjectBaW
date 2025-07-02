package com.example.projectbaw.service;

import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository            userRepository;
    private final BCryptPasswordEncoder     bCryptPasswordEncoder;
    private final UserMapper                userMapper;
    private final EmailConfirmationService  emailConfirmationService;

    @Transactional
    public void registerUser(UserDto.RegisterDto requestDto) {

        if(userRepository.existsByUsername(requestDto.getUsername())){
            throw new IllegalArgumentException("User already exists");
        }

        if(requestDto.getPassword().length() < 8){
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        User user = new User();
        user.setUsername(userMapper.toRegister(requestDto).getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userMapper.toRegister(requestDto).getPassword()));
        user.setEmail(userMapper.toRegister(requestDto).getEmail());
        user.setEnabledAccount(false);
        user.setRole(Role.USER);

        String token = UUID.randomUUID().toString();
        user.setActivationToken(token);

        userRepository.save(user);
        emailConfirmationService.sendConfirmationEmail(user.getEmail(), token);
    }

    public Optional<User> login(String username, String password){

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent() && user.get().isEnabledAccount() && bCryptPasswordEncoder.matches(password, user.get().getPassword())){
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> getById(Long id){

        return userRepository.findById(id);
    }

    @Transactional
    public boolean changePassword(String oldPassword, String newPassword){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        if(newPassword.length() < 8 && bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){

                user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
        }
        return false;
    }

    public boolean isAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        return user.getRole() == Role.ADMIN;

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();

    }

    public void PasswordResetByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);

        userRepository.save(user);

        emailConfirmationService.sendResetPasswordEmail(user.getEmail(), resetToken);
    }

    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);

        return true;
    }



}
