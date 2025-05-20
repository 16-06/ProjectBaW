package com.example.projectbaw.service;

import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public void registerUser(UserDto.RequestDto requestDto) {

        if(userRepository.existsByUsername(requestDto.getUsername())){
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setUsername(userMapper.toEntity(requestDto).getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userMapper.toEntity(requestDto).getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public Optional<User> login(String username, String password){

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent() && bCryptPasswordEncoder.matches(password, user.get().getPassword())){
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> getById(Long id){

        return userRepository.findById(id);
    }

    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword){

        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent() && bCryptPasswordEncoder.matches(oldPassword, user.get().getPassword())){

                user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
                userRepository.save(user.get());
                return true;
        }
        return false;
    }

}
