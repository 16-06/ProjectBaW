package com.example.projectbaw.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String firstName;
    private String lastName;
    private String bio;
    private String interests;
    private boolean notificationsEnabled = true;

    @Lob
    private byte[] avatarImage;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notification;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserActionLog> userActionLogs;

}
