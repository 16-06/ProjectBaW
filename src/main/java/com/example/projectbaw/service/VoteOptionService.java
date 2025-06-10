package com.example.projectbaw.service;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteOptionService {

    private final VoteOptionRepository voteOptionRepository;
    private final VoteOptionMapper voteOptionMapper;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public List<VoteOptionDto.ResponseDto> getByVoteId(Long voteId) {
        return voteOptionRepository.findByVoteId(voteId)
                .stream()
                .map(voteOptionMapper::toVoteOptionDto)
                .collect(Collectors.toList());
    }

    public Optional<VoteOptionDto.ResponseDto> getById(Long Id) {
        return voteOptionRepository.findById(Id).map(voteOptionMapper::toVoteOptionDto);

    }

    public VoteOptionDto.ResponseDto create(VoteOptionDto.RequestDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();

        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findById(dto.getVoteId()).orElseThrow(()-> new EntityNotFoundException("Vote not found"));
        Vote voteUser = voteRepository.findByIdAndUserId(dto.getVoteId(),user.getId());

        //VoteOption saved = new VoteOption();

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

    public VoteOptionDto.ResponseDto updateCount(VoteOptionDto.ResponseDto dto) {
        VoteOption voteOption = voteOptionRepository.findById(dto.getId())
                .orElseThrow(()->new RuntimeException("Vote option not found"));

        voteOption.setCount(dto.getCount());
        return voteOptionMapper.toVoteOptionDto(voteOptionRepository.save(voteOption));
    }

    public void deleteById(Long id,Long voteId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findByIdAndUserId(voteId,user.getId());

        if(vote != null){
            voteOptionRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }

    }

    public void uploadImage(Long optionId, byte[] newImage) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        VoteOption voteOption = voteOptionRepository.findById(optionId)
                .orElseThrow(()->new RuntimeException("Vote option not found"));

        Vote vote = voteRepository.findByIdAndUserId(voteOption.getVote().getId(),user.getId());

        if(vote != null){

            voteOption.setImagedata(newImage);
            voteOptionRepository.save(voteOption);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }
    }
}
