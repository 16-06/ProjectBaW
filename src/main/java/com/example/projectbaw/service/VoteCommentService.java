//package com.example.projectbaw.service;
//
//import com.example.projectbaw.mapper.VoteCommentMapper;
//import com.example.projectbaw.model.User;
//import com.example.projectbaw.model.Vote;
//import com.example.projectbaw.model.VoteComment;
//import com.example.projectbaw.payload.VoteCommentDto;
//import com.example.projectbaw.repository.UserRepository;
//import com.example.projectbaw.repository.VoteCommentRepository;
//import com.example.projectbaw.repository.VoteRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import java.time.LocalDateTime;
//
//
//@Service
//@RequiredArgsConstructor
//public class VoteCommentService {
//
//    private final UserRepository userRepository;
//    private final VoteRepository voteRepository;
//    private final VoteCommentRepository voteCommentRepository;
//    private final VoteCommentMapper voteCommentMapper;
//
//    public VoteCommentDto.ResponseDto createComment(VoteCommentDto.RequestDto requestDto){
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = (String) authentication.getPrincipal();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(()-> new RuntimeException("User not found"));
//
//        Vote vote = voteRepository.findById((requestDto.getVoteId()))
//                .orElseThrow(() -> new RuntimeException("Vote not found"));
//
//        VoteComment voteComment = voteCommentMapper.toEntity(requestDto);
//        voteComment.setCommentAuthor(user);
//        voteComment.setVote(vote);
//        voteComment.setCreatedAt(LocalDateTime.now());
//
//        VoteComment saved = voteCommentRepository.save(voteComment);
//
//        return voteCommentMapper.toResponseDto(saved);
//
//    }
//
//    public VoteCommentDto.ResponseDto getById(Long id){
//
//        return voteCommentRepository
//                .findById(id)
//                .map(voteCommentMapper::toResponseDto)
//                .orElseThrow(() -> new RuntimeException("Vote comment not found"));
//    }
//
//}
package com.example.projectbaw.service;

import com.example.projectbaw.mapper.VoteCommentMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.model.VoteComment;
import com.example.projectbaw.payload.VoteCommentDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.VoteCommentRepository;
import com.example.projectbaw.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class VoteCommentService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final VoteCommentMapper voteCommentMapper;

    public VoteCommentDto.ResponseDto createComment(VoteCommentDto.RequestDto requestDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Vote vote = voteRepository.findById((requestDto.getVoteId()))
                .orElseThrow(() -> new RuntimeException("Vote not found"));

        VoteComment voteComment = voteCommentMapper.toEntity(requestDto);
        voteComment.setCommentAuthor(user);
        voteComment.setVote(vote);
        voteComment.setCreatedAt(LocalDateTime.now());

        VoteComment saved = voteCommentRepository.save(voteComment);

        return voteCommentMapper.toResponseDto(saved);

    }

    public VoteCommentDto.ResponseDto getById(Long id){

        return voteCommentRepository
                .findById(id)
                .map(voteCommentMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Vote comment not found"));
    }

}