package com.example.projectbaw.mapper;

import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.model.WhoVotedYet;
import com.example.projectbaw.payload.WhoVotedYetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WhoVotedYetMapper {

    public WhoVotedYetDto.ResponseDto toWhoVotedYetDto(WhoVotedYet whoVotedYet) {
        if(whoVotedYet == null) {
            return null;
        }
        WhoVotedYetDto.ResponseDto responseDto = new WhoVotedYetDto.ResponseDto();
        responseDto.setId(whoVotedYet.getId());
        responseDto.setUserId(whoVotedYet.getId());
        responseDto.setVoteId(whoVotedYet.getId());
        responseDto.setAlreadyVoted(whoVotedYet.isAlreadyVoted());

        return responseDto;
    }

    public WhoVotedYet toEntity(WhoVotedYetDto.RequestDto requestDto) {
        if(requestDto == null) {
            return null;
        }

        WhoVotedYet whoVotedYet = new WhoVotedYet();
        whoVotedYet.setAlreadyVoted(requestDto.isAlreadyVoted());

        User user = new User();
        user.setId(requestDto.getUserId());
        whoVotedYet.setUser(user);

        Vote vote = new Vote();
        vote.setId(requestDto.getVoteId());
        whoVotedYet.setVote(vote);

        return whoVotedYet;
    }
}
