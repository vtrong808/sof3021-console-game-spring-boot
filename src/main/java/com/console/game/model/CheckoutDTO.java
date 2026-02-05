package com.console.game.model;

import lombok.Data;

@Data
public class CheckoutDTO {
    private String fullName;
    private String phoneNumber;
    private String address;
    private String note;
    private String paymentMethod;
}
