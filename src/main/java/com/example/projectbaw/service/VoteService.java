package com.example.projectbaw.service;


import com.example.projectbaw.analytics.TrackAction;
import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.mapper.VoteMapper;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.payload.VoteDto;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository    voteRepository;
    private final UserRepository    userRepository;
    private final VoteMapper        voteMapper;

    public List<VoteDto.ResponseDto> getByCategory(String category) {

        List<Vote> votes;

        if (category != null && !category.isEmpty()) {
            votes =  voteRepository.findByCategory(category);
        }
        else {
            votes = voteRepository.findAll();
        }

        return votes.stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<VoteDto.ResponseDto> getAllVotes() {

        return voteRepository.findAll()
                .stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());

    }

    public List<VoteDto.ResponseDto> getUserVotes(String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        return voteRepository.findByUserId(user.getId())
                .stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<VoteDto.ResponseDto> getByUserId(Long id) {

        return voteRepository.findByUserId(id)
                .stream()
                .map(voteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<VoteDto.ResponseDto> getByVoteId(Long id) {

        return voteRepository.findById(id)
                .map(voteMapper::toResponse);
    }

    @TrackAction(value = "VOTE_CREATED")
    public VoteDto.ResponseDto createVote(VoteDto.RequestDto requestDto, CustomUserDetails userDetails) {

        // Old userAuth method change to @AuthenticationPrincipal
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String username = (String) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        Vote vote = voteMapper.toEntity(requestDto);
        vote.setUser(user);
        vote.setAuthor(user.getUsername());

        Vote savedVote = voteRepository.save(vote);

        return voteMapper.toResponse(savedVote);
    }

    @TrackAction(value = "VOTE_DELETED")
    public void deleteById(Long id,CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findByIdAndUserId(id,user.getId());

        if(vote != null){
            voteRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }


    }

    @TrackAction(value = "VOTE_EDITED")
    public void updateCategory(Long voteId, String newCategory,CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));
        Vote vote = voteRepository.findByIdAndUserId(voteId,user.getId());

        if(vote!= null){
            vote.setCategory(newCategory);
            voteRepository.save(vote);

        }
        else{
            throw new RuntimeException("Vote not belongs to user");
        }
    }

    @TrackAction(value = "VOTE_EDITED")
    public void updateImage(Long voteId, byte[] newImage,CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));
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

    public Long getAuthorUserId(Long voteId, CustomUserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));

        Optional<Vote> vote = voteRepository.findById(voteId);

        if(vote.isPresent()){
            return vote.get().getUser().getId();
        }
        else{
            throw new RuntimeException("Author not found for the given vote ID");
        }
    }

}
