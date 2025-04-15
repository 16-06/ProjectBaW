package com.example.projectbaw.mapper;

import com.example.projectbaw.model.VoteOption;
import com.example.projectbaw.payload.VoteOptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteOptionMapper {

    public VoteOptionDto.ResponseDto toVoteOptionDto(VoteOption voteOption) {
        if(voteOption == null) {
            return null;
        }

        VoteOptionDto.ResponseDto responseDto = new VoteOptionDto.ResponseDto();
        responseDto.setId(voteOption.getId());
        responseDto.setCount(voteOption.getCount());
        responseDto.setName(voteOption.getName());
        responseDto.setImagedata(voteOption.getImagedata());

        return responseDto;
    }

    public VoteOption toEntity(VoteOptionDto.RequestDto requestDto) {
        if(requestDto == null) {
            return null;
        }

        VoteOption voteOption = new VoteOption();
        voteOption.setName(requestDto.getName());
        voteOption.setImagedata(requestDto.getImagedata());
        voteOption.setCount(0);
        return voteOption;
    }
}
