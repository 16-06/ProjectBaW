package com.example.projectbaw.controller;


import com.example.projectbaw.mapper.WhoVotedYetMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.VoteOption;
import com.example.projectbaw.model.WhoVotedYet;
import com.example.projectbaw.payload.WhoVotedYetDto;
import com.example.projectbaw.service.UserService;
import com.example.projectbaw.service.VoteOptionService;
import com.example.projectbaw.service.WhoVotedYetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
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

}