package com.example.projectbaw.controller;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.payload.VoteDto;
import com.example.projectbaw.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService   voteService;

    @GetMapping("/public/all")
    public ResponseEntity<List<VoteDto.ResponseDto>> getAllVotes() {

        List<VoteDto.ResponseDto> votes = voteService.getAllVotes();

        return ResponseEntity.ok(votes);
    }

    @GetMapping("/public/category")
    public ResponseEntity<List<VoteDto.ResponseDto>> getVotesByCategory(@RequestParam String category) {

        List<VoteDto.ResponseDto> votes = voteService.getByCategory(category);

        return ResponseEntity.ok(votes);
    }

    
    @GetMapping("/public/user/{id}")
    public ResponseEntity<List<VoteDto.ResponseDto>> getVoteByUserId(@PathVariable Long id) {

        List<VoteDto.ResponseDto> vote = voteService.getByUserId(id);

        return ResponseEntity.ok(vote);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<VoteDto.ResponseDto> getByVoteId(@PathVariable Long id) {

        return voteService.getByVoteId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<VoteDto.ResponseDto> createVote(@RequestBody VoteDto.RequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

        VoteDto.ResponseDto responseDto = voteService.createVote(requestDto,userDetails);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVote(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetails userDetails) {


        voteService.deleteById(id,userDetails);

        return ResponseEntity.ok("Vote deleted");

    }

    @PutMapping("/category/{voteId}")
    public ResponseEntity<String> updateVote(@PathVariable Long voteId, @RequestBody String category, @AuthenticationPrincipal CustomUserDetails userDetails) {

        voteService.updateCategory(voteId,category,userDetails);

        return ResponseEntity.ok("Category updated");
    }

    @PutMapping("/upload/{voteId}")
    public ResponseEntity<String> updateVote(@PathVariable Long voteId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("photo") MultipartFile file) throws IOException {

        voteService.updateImage(voteId, file.getBytes(),userDetails);

        return ResponseEntity.ok("Image updated");
    }




}
