package com.console.game.service;

import java.math.BigDecimal;
import java.util.List;

import com.console.game.model.Order;

public interface DashboardService {
    BigDecimal getTotalRevenue();

    Long getTotalOrders();

    List<Order> getRecentOrders();
}
