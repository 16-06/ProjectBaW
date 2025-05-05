package com.example.projectbaw.controller;


import com.example.projectbaw.mapper.WhoVotedYetMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.WhoVotedYet;
import com.example.projectbaw.payload.WhoVotedYetDto;
import com.example.projectbaw.service.WhoVotedYetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/whovoted")
public class WhoVotedYetController {

    private final WhoVotedYetService whoVotedYetService;
    private final WhoVotedYetMapper whoVotedYetMapper;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody WhoVotedYetDto.RequestDto requestDto) {

        WhoVotedYet create = whoVotedYetService.save(whoVotedYetMapper.toEntity(requestDto));
        return ResponseEntity.ok(whoVotedYetMapper.toWhoVotedYetDto(create));
    }

    @GetMapping("/{voteId}")
    public ResponseEntity<?> getById(@PathVariable Long voteId, @AuthenticationPrincipal User user) {

        List<WhoVotedYet> entities  = whoVotedYetService.findByUserAndVoteId(user, voteId);
        List<WhoVotedYetDto.ResponseDto> voteList = entities .stream().map(whoVotedYetMapper::toWhoVotedYetDto).collect(Collectors.toList());
        return ResponseEntity.ok(voteList);

    }

}