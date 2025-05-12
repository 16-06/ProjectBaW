package com.example.projectbaw.repository;

import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    List<Vote> findByCategory(String category);

    List<Vote> findByUser(User user);

    List<Vote> findByUserUsername(String userUsername);

    List<Vote> findByUserId(long userId);
}
