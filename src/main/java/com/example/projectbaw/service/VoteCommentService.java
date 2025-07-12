package com.example.projectbaw.service;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.mapper.VoteCommentMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.model.VoteComment;
import com.example.projectbaw.payload.VoteCommentDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.VoteCommentRepository;
import com.example.projectbaw.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VoteCommentService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final VoteCommentMapper voteCommentMapper;
    private final NotificationService notificationService;

    public VoteCommentDto.ResponseDto createComment(VoteCommentDto.RequestDto requestDto, CustomUserDetails userDetails){

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        Vote vote = voteRepository.findById((requestDto.getVoteId()))
                .orElseThrow(() -> new RuntimeException("Vote not found"));

        VoteComment voteComment = voteCommentMapper.toEntity(requestDto);
        voteComment.setCommentAuthor(user);
        voteComment.setVote(vote);
        voteComment.setCreatedAt(LocalDateTime.now());

        VoteComment saved = voteCommentRepository.save(voteComment);

        notificationService.notifyUser(
                vote.getUser().getProfile(),
                "New comment in your vote " + vote.getName(),
                "From: " + user.getUsername()
        );

        return voteCommentMapper.toResponseDto(saved);

    }

    public VoteCommentDto.ResponseDto getById(Long id){

        return voteCommentRepository
                .findById(id)
                .map(voteCommentMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Vote comment not found"));
    }

    public List<VoteCommentDto.ResponseDto> getByVoteId(Long voteId) {

        if (voteId == null) {
            throw new IllegalArgumentException("Vote ID must not be null");
        }
        return voteCommentRepository.findByVoteId(voteId)
                .stream()
                .map(voteCommentMapper::toResponseDto)
                .toList();
    }

    public void deleteById(Long id,CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        VoteComment voteComment = voteCommentRepository.findByIdAndCommentAuthorId(id,user.getId());

        if(voteComment != null){
            voteRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }

    }

}