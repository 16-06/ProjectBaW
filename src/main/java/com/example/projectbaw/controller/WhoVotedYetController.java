package com.example.projectbaw.controller;


import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.payload.WhoVotedYetDto;
import com.example.projectbaw.service.WhoVotedYetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/whoVoted")
public class WhoVotedYetController {

    private final WhoVotedYetService    whoVotedYetService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody WhoVotedYetDto.RequestDto requestDto) {

        WhoVotedYetDto.ResponseDto responseDto = whoVotedYetService.create(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{voteId}")
    public ResponseEntity<?> getById(@PathVariable Long voteId) {

        boolean hasVoted = whoVotedYetService.hasUserVoted(voteId);

        return ResponseEntity.ok(new HashMap<String, Boolean>() {{
            put("hasVoted", hasVoted);
        }});

    }

    @GetMapping("/voters/{voteId}")
    public ResponseEntity<List<UserProfileDto.ResponseDto>> getVotersProfiles(@PathVariable Long voteId) {

        return ResponseEntity.ok(whoVotedYetService.getUserProfileWhoVoted(voteId));

    }

}