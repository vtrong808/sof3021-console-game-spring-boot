package com.console.game.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Integer orderId;
    private String customerName; // Lấy từ User hoặc fullName trong Order
    private String phoneNumber;
    private String address;
    private String note;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;       // OrderStatus Enum -> String
    private String paymentMethod;
    private String paymentStatus;
    
    private List<OrderItemDTO> items; // Danh sách sản phẩm
}