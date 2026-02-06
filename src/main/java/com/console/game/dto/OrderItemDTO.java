package com.console.game.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private String productName;
    private String productThumbnail;
    private Integer quantity;
    private BigDecimal price; // Giá tại thời điểm mua
}