package com.example.projectbaw.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String author;
    private String category;

    private byte[] image;

    @OneToMany(mappedBy = "vote",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteOption> voteOptions;

    @OneToMany(mappedBy = "vote",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WhoVotedYet> whoVotedYet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

