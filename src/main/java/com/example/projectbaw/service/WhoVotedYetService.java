package com.example.projectbaw.service;


import com.example.projectbaw.model.User;
import com.example.projectbaw.model.WhoVotedYet;
import com.example.projectbaw.repository.WhoVotedYetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WhoVotedYetService {

    private final WhoVotedYetRepository whoVotedYetRepository;

    public WhoVotedYet save(WhoVotedYet whoVotedYet){

        return whoVotedYetRepository.save(whoVotedYet);
    }

    public List<WhoVotedYet> findByUserAndVoteId(User user, Long voteId){

        return whoVotedYetRepository.findByUserAndVoteId(user,voteId);

    }


}
