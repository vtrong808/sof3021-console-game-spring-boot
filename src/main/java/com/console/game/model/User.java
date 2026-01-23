package com.console.game.model;

import com.console.game.enums.Role;
import com.console.game.enums.Provider;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Users")
@Data
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.LOCAL;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;

    private Boolean isActive = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;
}