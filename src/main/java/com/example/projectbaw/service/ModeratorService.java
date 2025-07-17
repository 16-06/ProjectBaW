package com.example.projectbaw.service;

import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModeratorService {

    private final UserRepository        userRepository;
    private final VoteRepository        voteRepository;
    private final VoteOptionRepository  voteOptionRepository;
    private final UserProfileRepository userProfileRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final UserMapper            userMapper;


    public void deleteUser(Long id)         {userRepository.deleteById(id);}
    public void deleteVote(Long id)         {voteRepository.deleteById(id);}
    public void deleteVoteOption(Long id)   {voteOptionRepository.deleteById(id);}
    public void deleteUserProfile(Long id)  {userProfileRepository.deleteById(id);}
    public void deleteVoteComment(Long id)  {voteCommentRepository.deleteById(id);}

    public List<UserDto.FullUserDto> getAllUsers(){

        List<User> users = userRepository.findAll();

        return users
                .stream()
                .map(userMapper::toFullUserDto)
                .collect(Collectors.toList());
    }

}
