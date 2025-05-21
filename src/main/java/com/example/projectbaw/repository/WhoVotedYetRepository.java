package com.example.projectbaw.repository;

import com.example.projectbaw.model.User;
import com.example.projectbaw.model.WhoVotedYet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhoVotedYetRepository extends JpaRepository<WhoVotedYet, Long> {

    List<WhoVotedYet> findByUserAndVoteId(User user, long voteId);
    List<WhoVotedYet> findByVoteId(Long voteId);
    boolean existsByUserIdAndVoteId(Long userid, Long voteId);

}
