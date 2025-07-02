package com.example.projectbaw.mapper;

import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserDto.ResponseDto toUserDto(User user) {
        if(user == null) {
            return null;
        }

        UserDto.ResponseDto responseDto = new UserDto.ResponseDto();
        responseDto.setId(user.getId());
        responseDto.setUsername(user.getUsername());
        return responseDto;
    }

    public User toEntity(UserDto.RequestDto requestDto) {
        if(requestDto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(requestDto.getPassword());
        return user;
    }

    public User toRegister(UserDto.RegisterDto requestDto) {
        if(requestDto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(requestDto.getPassword());
        user.setEmail(requestDto.getEmail());

        return user;
    }
}

