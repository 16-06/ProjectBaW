package com.example.projectbaw.service;


import com.example.projectbaw.mapper.UserProfileMapper;
import com.example.projectbaw.mapper.WhoVotedYetMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.WhoVotedYet;
import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.payload.WhoVotedYetDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.WhoVotedYetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WhoVotedYetService {

    private final WhoVotedYetRepository     whoVotedYetRepository;
    private final UserRepository            userRepository;
    private final WhoVotedYetMapper         whoVotedYetMapper;
    private final UserProfileMapper         userProfileMapper;

    public WhoVotedYetDto.ResponseDto create(WhoVotedYetDto.RequestDto requestDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        WhoVotedYet whoVotedYet = whoVotedYetMapper.toEntity(requestDto);

        boolean alreadyVoted = whoVotedYetRepository.existsByUserIdAndVoteId(user.getId(), whoVotedYet.getVote().getId());

        if(alreadyVoted){
            throw new RuntimeException("User already voted");
        }

        whoVotedYet.setUser(user);

        WhoVotedYet saved = whoVotedYetRepository.save(whoVotedYet);

        return whoVotedYetMapper.toWhoVotedYetDto(saved);
    }

    public List<WhoVotedYet> findByVoteId(Long voteId){

        return whoVotedYetRepository.findByVoteId(voteId);

    }

    public boolean hasUserVoted(Long voteId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        Long userId = user.getId();

        return whoVotedYetRepository.existsByUserIdAndVoteId(userId, voteId);
    }


    public List<UserProfileDto.ResponseDto> getUserProfileWhoVoted(Long voteId) {

        List<WhoVotedYet> whoVoted = whoVotedYetRepository.findByVoteId(voteId);

        return whoVoted.stream()
                .map(whoVotedList -> whoVotedList.getUser().getProfile())
                .filter(Objects::nonNull)
                .map(userProfileMapper::toDto)
                .collect(Collectors.toList());


    }

}
