package com.example.projectbaw.repository;

import com.example.projectbaw.model.Vote;
import com.example.projectbaw.model.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {

    List<VoteOption> vote(Vote vote);

    List<VoteOption> findByVoteId(Long voteId);
}
