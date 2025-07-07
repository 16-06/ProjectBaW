package com.example.projectbaw.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "user_security")

public class UserSecurity {
    @Id
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String activationToken;
    private String resetPasswordToken;
    private String TwoFactorCode;
    LocalDateTime CodeExpiryTime;
    LocalDateTime BanExpiryTime = null;
}
