package com.console.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TopSellingProductDTO {
    private String productName;
    private String thumbnailUrl;
    private Long totalSold;
    private BigDecimal totalRevenue;
}