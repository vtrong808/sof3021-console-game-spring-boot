package com.console.game.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.console.game.dto.CategoryRevenueDTO;
import com.console.game.dto.MonthlyRevenueDTO;
import com.console.game.dto.TopSellingProductDTO;
import com.console.game.model.Order;
import com.console.game.repository.OrderRepository;
import com.console.game.repository.ProductRepository;
import com.console.game.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public BigDecimal getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    @Override
    public Long getTotalOrders() {
        return orderRepository.getTotalOrders();
    }

    @Override
    public List<Order> getRecentOrders() {
        return orderRepository.findRecentOrders(PageRequest.of(0, 5)).getContent();
    }

    @Override
    public Long getTotalSoldProducts() {
        return orderRepository.getTotalSoldProducts();
    }

    @Override
    public List<MonthlyRevenueDTO> getMonthlyRevenue(int year) {
        return orderRepository.getMonthlyRevenue(year);
    }

    @Override
    public List<CategoryRevenueDTO> getRevenueByCategory() {
        return orderRepository.getRevenueByCategory();
    }

    @Override
    public Long getLowStockProductCount() {
        // Đếm số sản phẩm có tồn kho dưới 10
        return productRepository.countByStockQuantityLessThan(10);
    }

    @Override
    public List<TopSellingProductDTO> getTopSellingProducts() {
        // Lấy top 5
        return orderRepository.findTopSellingProducts(PageRequest.of(0, 5));
    }
}