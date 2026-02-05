package com.console.game.service;

import java.math.BigDecimal;
import java.util.List;
import com.console.game.dto.CategoryRevenueDTO;
import com.console.game.dto.MonthlyRevenueDTO;
import com.console.game.dto.TopSellingProductDTO;
import com.console.game.model.Order;
import com.console.game.dto.TopSellingProductDTO;

public interface DashboardService {
    BigDecimal getTotalRevenue();

    Long getTotalOrders();

    List<Order> getRecentOrders();

    Long getTotalSoldProducts();

    List<MonthlyRevenueDTO> getMonthlyRevenue(int year);

    List<CategoryRevenueDTO> getRevenueByCategory();

    Long getLowStockProductCount();

    List<TopSellingProductDTO> getTopSellingProducts();
}