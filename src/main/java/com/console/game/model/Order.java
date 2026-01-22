package com.console.game.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal totalAmount;
    private String shippingAddressSnapshot;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.pending;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private LocalDateTime orderDate = LocalDateTime.now();
}
