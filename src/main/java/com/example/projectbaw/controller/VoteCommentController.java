package com.example.projectbaw.controller;

import com.example.projectbaw.payload.VoteCommentDto;
import com.example.projectbaw.service.VoteCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vote-comments")
@RequiredArgsConstructor
public class VoteCommentController {

    private final VoteCommentService voteCommentService;

    @PostMapping("")
    public ResponseEntity<VoteCommentDto.ResponseDto> create(@RequestBody VoteCommentDto.RequestDto request) {

        return ResponseEntity.ok(voteCommentService.createComment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoteCommentDto.ResponseDto> getById(@RequestBody Long id) {
        return ResponseEntity.ok(voteCommentService.getById(id));

    }

    @GetMapping("/byVoteId/{voteId}")
    public ResponseEntity<List<VoteCommentDto.ResponseDto>> getByVoteId(@PathVariable Long voteId) {

        List<VoteCommentDto.ResponseDto> comments = voteCommentService.getByVoteId(voteId);
        return ResponseEntity.ok(comments);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        voteCommentService.deleteById(id);
        return ResponseEntity.ok().build();

    }
}
