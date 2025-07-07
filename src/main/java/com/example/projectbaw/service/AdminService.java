package com.example.projectbaw.service;


import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.mapper.VoteMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.payload.VoteDto;
import com.example.projectbaw.repository.UserProfileRepository;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.VoteOptionRepository;
import com.example.projectbaw.repository.VoteRepository;
import com.example.projectbaw.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final UserProfileRepository userProfileRepository;
    private final VoteMapper voteMapper;
    private final UserMapper userMapper;

    public void deleteUser(Long id)         {userRepository.deleteById(id);}
    public void deleteVote(Long id)         {voteRepository.deleteById(id);}
    public void deleteVoteOption(Long id)   {voteOptionRepository.deleteById(id);}
    public void deleteUserProfile(Long id)  {userProfileRepository.deleteById(id);}

    public void changeUserRole(Long userId, Role newRole){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(user.getRole() == Role.ADMIN){
            throw new RuntimeException("Admin cannot be changed to another role");
        }

        if(user.getRole() == newRole){
            throw new RuntimeException("User already has this role");
        }

        user.setRole(newRole);
        userRepository.save(user);

    }

    public void blockUser(Long userId, LocalDateTime date){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(user.isBannedAccount()){
            throw new RuntimeException("User already blocked");
        }

        user.setBannedAccount(true);
        user.getSecurityData().setBanExpiryTime(date);
        userRepository.save(user);

    }

    public void unblockUser(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!user.isBannedAccount()){
            throw new RuntimeException("User is not blocked");
        }

        user.setBannedAccount(false);
    }

    public List<UserDto.ResponseDto> getAllUsers(){

        List<User> users = userRepository.findAll();

        return users
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }



}
