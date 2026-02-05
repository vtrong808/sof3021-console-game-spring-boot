package com.console.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryRevenueDTO {
    private String categoryName;
    private BigDecimal revenue;
    private Long quantity;
}