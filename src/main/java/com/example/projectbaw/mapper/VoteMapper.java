package com.example.projectbaw.mapper;

import com.example.projectbaw.model.Vote;
import com.example.projectbaw.payload.VoteDto;
import com.example.projectbaw.payload.VoteOptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VoteMapper {

    private final VoteOptionMapper voteOptionMapper;


    public VoteDto.ResponseDto toResponse(Vote vote) {
        if(vote == null) {
            return null;
        }

        VoteDto.ResponseDto responseDto = new VoteDto.ResponseDto();
        responseDto.setId(vote.getId());
        responseDto.setAuthor(vote.getAuthor());
        responseDto.setCategory(vote.getCategory());
        responseDto.setName(vote.getName());
        responseDto.setImageData(vote.getImage());


        if(vote.getVoteOptions() != null){
            List<VoteOptionDto.ResponseDto> options = vote.getVoteOptions().stream()
                    .map(voteOptionMapper::toVoteOptionDto)
                    .collect(Collectors.toList());
            responseDto.setOptions(options);
        }

        return responseDto;
    }

    public Vote toEntity(VoteDto.RequestDto requestDto) {
        if(requestDto == null) {
            return null;
        }

        Vote vote = new Vote();
        vote.setAuthor(requestDto.getAuthor());
        vote.setCategory(requestDto.getCategory());
        vote.setName(requestDto.getName());
        vote.setImage(requestDto.getImageData());

        if(requestDto.getOptions() != null) {
            vote.setVoteOptions(requestDto.getOptions()
                    .stream()
                    .map(voteOptionMapper::toEntity)
                    .collect(Collectors.toList()));
        }

        return vote;
    }


}
