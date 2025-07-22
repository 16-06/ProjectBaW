package com.example.projectbaw.service;

import com.example.projectbaw.analytics.TrackAction;
import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.mapper.VoteOptionMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.model.VoteOption;
import com.example.projectbaw.payload.VoteOptionDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.VoteOptionRepository;
import com.example.projectbaw.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteOptionService {

    private final VoteOptionRepository  voteOptionRepository;
    private final VoteOptionMapper      voteOptionMapper;
    private final VoteRepository        voteRepository;
    private final UserRepository        userRepository;

    public List<VoteOptionDto.ResponseDto> getByVoteId(Long voteId) {
        return voteOptionRepository.findByVoteId(voteId)
                .stream()
                .map(voteOptionMapper::toVoteOptionDto)
                .collect(Collectors.toList());
    }

    public Optional<VoteOptionDto.ResponseDto> getById(Long Id) {
        return voteOptionRepository.findById(Id).map(voteOptionMapper::toVoteOptionDto);

    }

    @TrackAction(value = "VOTE_OPTION_CREATED")
    public VoteOptionDto.ResponseDto create(VoteOptionDto.RequestDto dto, CustomUserDetails userDetails) {


        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findById(dto.getVoteId()).orElseThrow(()-> new EntityNotFoundException("Vote not found"));
        Vote voteUser = voteRepository.findByIdAndUserId(dto.getVoteId(),user.getId());


        if(voteUser != null) {

            VoteOption saved = voteOptionMapper.toEntity(dto);
            saved.setVote(vote);

            voteOptionRepository.save(saved);

            return voteOptionMapper.toVoteOptionDto(saved);
        }
        else{
            throw new EntityNotFoundException("Vote not belongs to user");
        }
    }

    public VoteOptionDto.ResponseDto updateCount(VoteOptionDto.UpdateCountDto dto) {
        VoteOption voteOption = voteOptionRepository.findById(dto.getId())
                .orElseThrow(()->new RuntimeException("Vote option not found"));


        voteOption.setCount(voteOption.getCount() + 1);

        return voteOptionMapper.toVoteOptionDto(voteOptionRepository.save(voteOption));
    }

    @TrackAction(value = "VOTE_OPTION_DELETED")
    public void deleteById(Long id,Long voteId, CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        Vote vote = voteRepository.findByIdAndUserId(voteId,user.getId());

        if(vote != null){
            voteOptionRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }

    }

    @TrackAction(value = "VOTE_OPTION_EDITED")
    public void uploadImage(Long optionId, byte[] newImage, CustomUserDetails userDetails){


        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        VoteOption voteOption = voteOptionRepository.findById(optionId)
                .orElseThrow(()->new RuntimeException("Vote option not found"));

        Vote vote = voteRepository.findByIdAndUserId(voteOption.getVote().getId(),user.getId());

        if(vote != null){

            voteOption.setImageData(newImage);
            voteOptionRepository.save(voteOption);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }
    }
}
