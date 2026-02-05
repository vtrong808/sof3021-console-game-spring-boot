package com.console.game.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String recipientName;
    private String phoneNumber;

    @Column(nullable = false)
    private String addressLine;

    private String city;
    private String district;

    private Boolean isDefault;
}
