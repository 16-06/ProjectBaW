package com.example.projectbaw.service;


import com.example.projectbaw.model.User;
import com.example.projectbaw.model.WhoVotedYet;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.WhoVotedYetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WhoVotedYetService {

    private final WhoVotedYetRepository whoVotedYetRepository;
    private final UserRepository userRepository;

    public WhoVotedYet save(WhoVotedYet whoVotedYet){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        whoVotedYet.setUser(user);

        return whoVotedYetRepository.save(whoVotedYet);
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

}
