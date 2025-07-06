package com.example.projectbaw.service;

import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.UserProfile;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.repository.UserProfileRepository;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.role.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository            userRepository;
    private final BCryptPasswordEncoder     bCryptPasswordEncoder;
    private final UserMapper                userMapper;
    private final EmailConfirmationService  emailConfirmationService;
    private final UserProfileRepository     userProfileRepository;
    private final JwtUtil                   jwtUtil;

    @Transactional
    public void registerUser(UserDto.RegisterDto requestDto) {

        if(userRepository.existsByUsername(requestDto.getUsername()) || userRepository.existsByEmail(requestDto.getEmail())){
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
        user.setTwoFactorEnabled(false);
        user.setRole(Role.USER);

        String token = UUID.randomUUID().toString();
        user.setActivationToken(token);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);

        userRepository.save(user);
        userProfileRepository.save(userProfile);

        emailConfirmationService.sendConfirmationEmail(user.getEmail(), token);
    }

    public boolean isAccountEnabled(String username) {

        return userRepository.findByUsername(username)
                .map(User::isEnabledAccount)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<String> login(String username, String password){

                return userRepository.findByUsername(username)
                .filter(user -> user.isEnabledAccount() && bCryptPasswordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    if(user.isTwoFactorEnabled()) {

                        String code = String.format("%04d", new Random().nextInt(9999));
                        user.setTwoFactorCode(code);
                        user.setCodeExpiryTime(LocalDateTime.now().plusMinutes(5));
                        emailConfirmationService.sendTwoFactorCode(user.getEmail(), code);
                        userRepository.save(user);

                        return null;

                    }
                    else {
                        return jwtUtil.generateToken(user);
                    }

                }

                );

    }

    public String verifyTwoFactorCode(String username, String code) {

        Optional<User> userOptional= userRepository.findByUsername(username);

        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if(user.getTwoFactorCode() == null || user.getCodeExpiryTime() == null) {
            throw new RuntimeException("Two-factor authentication is not enabled for this user");
        }

        if(LocalDateTime.now().isAfter(user.getCodeExpiryTime())) {
            throw new RuntimeException("Two-factor code has expired");
        }

        if(user.getTwoFactorCode().equals(code)) {

            user.setTwoFactorCode(null);
            user.setCodeExpiryTime(null);
            userRepository.save(user);

            return jwtUtil.generateToken(user);
        }

        return new RuntimeException("Two-factor code is incorrect").getMessage();
    }


    public Optional<UserDto.ResponseDto> getById(Long id){

        return userRepository.findById(id).map(userMapper::toUserDto);
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

    public boolean activateAccount(String token) {

        Optional<User> user = userRepository.findByActivationToken(token);

        if (user.isEmpty() || user.get().isEnabledAccount()) {
            return false;
        }

        User confirmedUser = user.get();
        confirmedUser.setEnabledAccount(true);
        confirmedUser.setActivationToken(null);
        userRepository.save(confirmedUser);

        return true;
    }

    public List<UserDto.ResponseDto> isAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        if(user.getRole() != Role.ADMIN ){
            throw new AccessDeniedException("Access denied");
        }

        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();

    }

    public Map<String, Object> getAuthenticatedUserInfo(HttpServletRequest request){

        String username = (String) request.getAttribute("username");
        Long userId = (Long) request.getAttribute("UserId");

        if (username == null || userId == null) {
            return null;
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userId);
        userInfo.put("username", username);

        return userInfo;
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
