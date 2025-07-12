package com.example.projectbaw.controller;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.payload.VoteOptionDto;
import com.example.projectbaw.service.VoteOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/vote-options")
@RequiredArgsConstructor
public class VoteOptionController {

    private final VoteOptionService     voteOptionService;

    @GetMapping("/{voteId}")
    public ResponseEntity<List<VoteOptionDto.ResponseDto>> getByVoteId(@PathVariable Long voteId) {
        return ResponseEntity.ok(voteOptionService.getByVoteId(voteId));
    }

    @GetMapping("/byOptionId/{optionId}")
    public ResponseEntity<VoteOptionDto.ResponseDto> getByOptionId(@PathVariable Long optionId) {
        return voteOptionService.getById(optionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<VoteOptionDto.ResponseDto> create(@RequestBody VoteOptionDto.RequestDto dto,@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(voteOptionService.create(dto,userDetails));

    }

    @PostMapping("/count")
    public ResponseEntity<VoteOptionDto.ResponseDto> updateCount(@RequestBody VoteOptionDto.UpdateCountDto dto) {
        return ResponseEntity.ok(voteOptionService.updateCount(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody Long voteId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        voteOptionService.deleteById(id,voteId,userDetails);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/upload/{id}")
    public ResponseEntity<String> upload(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("photo") MultipartFile file) throws IOException {

        voteOptionService.uploadImage(id,file.getBytes(),userDetails);

        return ResponseEntity.ok("Picture added");
    }
}
