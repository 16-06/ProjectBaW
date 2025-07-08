package com.example.projectbaw.repository;

import com.example.projectbaw.model.VoteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {
  }