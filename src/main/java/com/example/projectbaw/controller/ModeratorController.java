package com.example.projectbaw.controller;

import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.ModeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moderator")
@PreAuthorize("hasRole('MODERATOR')")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto.FullUserDto>> getAllUsers() {

        List<UserDto.FullUserDto> users = moderatorService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestBody Long id) {
        moderatorService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @DeleteMapping("/delete-vote")
    public ResponseEntity<String> deleteVote(@RequestBody Long id) {
        moderatorService.deleteVote(id);
        return ResponseEntity.ok("Vote deleted successfully");
    }

    @DeleteMapping("/delete-vote-option")
    public ResponseEntity<String> deleteVoteOption(@RequestBody Long id) {
        moderatorService.deleteVoteOption(id);
        return ResponseEntity.ok("Vote option deleted successfully");
    }

    @DeleteMapping("/delete-user-profile")
    public ResponseEntity<String> deleteUserProfile(@RequestBody Long id) {
        moderatorService.deleteUserProfile(id);
        return ResponseEntity.ok("User profile deleted successfully");
    }

    @DeleteMapping("/delete-vote-comment")
    public ResponseEntity<String> deleteVoteComment(@RequestBody Long id) {
        moderatorService.deleteVoteComment(id);
        return ResponseEntity.ok("Vote comment deleted successfully");
    }
}
