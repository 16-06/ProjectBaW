package com.example.projectbaw.controller;


import com.example.projectbaw.mapper.VoteMapper;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.payload.VoteDto;
import com.example.projectbaw.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;
    private final VoteMapper voteMapper;

    @GetMapping("")
    public ResponseEntity<List<VoteDto.ResponseDto>> getAllVotes() {

        List<VoteDto.ResponseDto> dtos = voteService.getAllVotes()
                .stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/caterogy")
    public ResponseEntity<List<VoteDto.ResponseDto>> getVotes(@RequestBody String category) {

        List<VoteDto.ResponseDto> dtos = voteService.getByCategory(category)
                .stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<List<VoteDto.ResponseDto>> getVoteById(@PathVariable Long id) {
        List<VoteDto.ResponseDto> dtos = voteService.getByUserId(id).stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<VoteDto.ResponseDto> getById(@PathVariable Long id) {
        return voteService.getById(id)
                .map(vote -> ResponseEntity.ok(voteMapper.toResponse(vote)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<VoteDto.ResponseDto> createVote(@RequestBody VoteDto.RequestDto requestDto) {

        Vote savedVote = voteService.save(voteMapper.toEntity(requestDto));

        return ResponseEntity.ok(voteMapper.toResponse(savedVote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVote(@PathVariable Long id) {


        voteService.deleteById(id);

        return ResponseEntity.ok("Vote deleted");

    }

    @PutMapping("/category/{voteId}")
    public ResponseEntity<String> updateVote(@PathVariable Long voteId, @RequestBody String category) {

        voteService.updateCategory(voteId,category);

        return ResponseEntity.ok("Category updated");
    }

    @PutMapping("/upload/{voteId}")
    public ResponseEntity<String> updateVote(@PathVariable Long voteId, @RequestParam("photo") MultipartFile file) throws IOException {

        voteService.updateImage(voteId, file.getBytes());

        return ResponseEntity.ok("Image updated");
    }




}
