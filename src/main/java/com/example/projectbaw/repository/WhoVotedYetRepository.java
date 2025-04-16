package com.example.projectbaw.repository;

import com.example.projectbaw.model.WhoVotedYet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhoVotedYetRepository extends JpaRepository<WhoVotedYet, Long> {

}
