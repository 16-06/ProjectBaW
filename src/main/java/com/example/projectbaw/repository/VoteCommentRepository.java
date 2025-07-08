package com.example.projectbaw.repository;

import com.example.projectbaw.model.VoteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {

    List<VoteComment> findByVoteId(Long voteId);

    VoteComment findByIdAndCommentAuthorId(Long voteId, Long userId);
  }