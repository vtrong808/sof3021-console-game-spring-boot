package com.console.game.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String phoneNumber;
    private String addressLine;
    private String city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Liên kết ngược lại bảng User
}
