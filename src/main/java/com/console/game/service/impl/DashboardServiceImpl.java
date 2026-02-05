package com.console.game.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.console.game.model.Order;
import com.console.game.repository.OrderRepository;
import com.console.game.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrderRepository orderRepository;

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
        return orderRepository
                .findRecentOrders(PageRequest.of(0, 5))
                .getContent();
    }

}
