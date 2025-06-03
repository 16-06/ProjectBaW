package com.example.projectbaw.service;


import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public List<Vote> getByCategory(String category) {

        if (category != null && !category.isEmpty()) {
            return voteRepository.findByCategory(category);
        }
        return voteRepository.findAll();
    }

    public List<Vote> getAllVotes() { return voteRepository.findAll(); }

    public List<Vote> getByUser(String user) {
        return voteRepository.findByUserUsername(user);
    }

    public List<Vote> getByUserId(Long id) {
        return voteRepository.findByUserId(id);
    }

    public Optional<Vote> getById(Long id) {
        return voteRepository.findById(id);
    }

    public Vote save(Vote vote) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        vote.setUser(user);

        return voteRepository.save(vote);
    }

    public void deleteById(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findByIdAndUserId(id,user.getId());

        if(vote != null){
            voteRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }


    }

    public void updateCategory(Long voteId, String newCategory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findByIdAndUserId(voteId,user.getId());

        if(vote!= null){
            vote.setCategory(newCategory);
            voteRepository.save(vote);

        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }
    }

    public void updateImage(Long voteId, byte[] newImage) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findByIdAndUserId(voteId,user.getId());

        if(vote != null){
            voteRepository.findById(voteId).ifPresent(v -> {
                v.setImage(newImage);
                voteRepository.save(v);
            });
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }
    }

}
