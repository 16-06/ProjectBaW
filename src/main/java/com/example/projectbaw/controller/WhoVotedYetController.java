package com.example.projectbaw.controller;


import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.payload.UserProfileDto;
import com.example.projectbaw.payload.WhoVotedYetDto;
import com.example.projectbaw.service.WhoVotedYetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/whoVoted")
public class WhoVotedYetController {

    private final WhoVotedYetService    whoVotedYetService;

    @PostMapping("")
    public ResponseEntity<WhoVotedYetDto.ResponseDto> create(@RequestBody WhoVotedYetDto.RequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

        WhoVotedYetDto.ResponseDto responseDto = whoVotedYetService.create(requestDto,userDetails);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{voteId}")
    public ResponseEntity<Map<String, Boolean>> getById(@PathVariable Long voteId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean hasVoted = whoVotedYetService.hasUserVoted(voteId,userDetails);

        return ResponseEntity.ok(new HashMap<>() {{
            put("hasVoted", hasVoted);
        }});

    }

    @GetMapping("/voters/{voteId}")
    public ResponseEntity<List<UserProfileDto.ResponseDto>> getVotersProfiles(@PathVariable Long voteId) {

        return ResponseEntity.ok(whoVotedYetService.getUserProfileWhoVoted(voteId));

    }

}