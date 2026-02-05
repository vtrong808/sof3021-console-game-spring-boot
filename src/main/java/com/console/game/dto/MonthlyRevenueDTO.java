package com.console.game.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRevenueDTO {

    private Integer month;
    private BigDecimal revenue;

    public Integer getMonth() {
        return month;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }
}
