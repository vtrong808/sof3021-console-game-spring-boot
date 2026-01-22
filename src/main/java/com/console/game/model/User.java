package com.console.game.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private String provider = "local";
    private String providerId;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.customer;
    private LocalDateTime createdAt = LocalDateTime.now();
}
