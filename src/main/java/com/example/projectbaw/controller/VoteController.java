package com.example.projectbaw.controller;


import com.example.projectbaw.mapper.VoteMapper;
import com.example.projectbaw.service.UserService;
import com.example.projectbaw.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;
    private final UserService userService;
    private final VoteMapper voteMapper;
}
