package com.example.projectbaw.service;


import com.example.projectbaw.model.Vote;
import com.example.projectbaw.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    public List<Vote> getByCategory(String category) {

        if (category != null && !category.isEmpty()) {
            return voteRepository.findByCategory(category);
        }
        return voteRepository.findAll();
    }

    public List<Vote> getByUser(String user) {
        return voteRepository.findByUserUsername(user);
    }

    public Optional<Vote> getById(Long id) {
        return voteRepository.findById(id);
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public void deleteById(Long id) {
        voteRepository.deleteById(id);
    }

    public void updateCategory(Long voteId, String newCategory) {
        voteRepository.findById(voteId).ifPresent(vote -> {
            vote.setCategory(newCategory);
            voteRepository.save(vote);
        });
    }

    public void updateImage(Long voteId, byte[] newImage) {
        voteRepository.findById(voteId).ifPresent(vote -> {
            vote.setImage(newImage);
            voteRepository.save(vote);
        });
    }
}
