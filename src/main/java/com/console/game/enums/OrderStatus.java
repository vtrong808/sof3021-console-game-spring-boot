package com.console.game.enums;

public enum OrderStatus {

    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    SHIPPING("Đang giao hàng"),
    DELIVERED("Đã giao"),
    CANCELLED("Đã hủy"),
    COMPLETED("Đã giao thành công"),
    RETURNED("Hoàn trả");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}