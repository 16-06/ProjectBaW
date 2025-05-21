package com.example.projectbaw.service;

import com.example.projectbaw.mapper.VoteOptionMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.model.VoteOption;
import com.example.projectbaw.payload.VoteOptionDto;
import com.example.projectbaw.repository.VoteOptionRepository;
import com.example.projectbaw.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

        Vote vote = voteRepository.findById(dto.getVoteId()).orElseThrow(()-> new EntityNotFoundException("Vote not found"));

        VoteOption saved = new VoteOption();

        saved = voteOptionMapper.toEntity(dto);
        saved.setVote(vote);

        voteOptionRepository.save(saved);

        return voteOptionMapper.toVoteOptionDto(saved);
    }

    public VoteOptionDto.ResponseDto updateCount(VoteOptionDto.ResponseDto dto) {
        VoteOption voteOption = voteOptionRepository.findById(dto.getId())
                .orElseThrow(()->new RuntimeException("Vote option not found"));

        voteOption.setCount(dto.getCount());
        return voteOptionMapper.toVoteOptionDto(voteOptionRepository.save(voteOption));
    }

    public void deleteById(Long voteId) {
        voteOptionRepository.deleteById(voteId);
    }

    public void uploadImage(Long voteId, MultipartFile file) throws IOException {
        VoteOption voteOption = voteOptionRepository.findById(voteId)
                .orElseThrow(()->new RuntimeException("Vote option not found"));

        voteOption.setImagedata(file.getBytes());
        voteOptionRepository.save(voteOption);
    }
}
