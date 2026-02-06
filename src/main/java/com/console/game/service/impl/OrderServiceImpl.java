package com.console.game.service.impl;

import com.console.game.enums.OrderStatus;
import com.console.game.model.*;
import com.console.game.repository.CartItemRepository;
import com.console.game.repository.OrderItemRepository;
import com.console.game.repository.OrderRepository;
import com.console.game.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    // --- CÁC HÀM CŨ (GIỮ NGUYÊN) ---
    @Override
    @Transactional
    public Order placeOrder(User user, String address, String phone, String fullName, String note) {
        // ... (Giữ nguyên code cũ của bạn) ...
        return null; // (Tôi rút gọn ở đây để tập trung vào phần mới, bạn hãy giữ code cũ nhé)
    }

    @Override
    @Transactional
    public Order placeOrderWithItems(User user, List<CartItem> items, CheckoutDTO dto) {
        // ... (Giữ nguyên code cũ của bạn) ...
        return null; // (Tương tự)
    }

    // --- CÁC HÀM MỚI CHO ADMIN ---

    @Override
    public List<Order> getAllOrders() {
        // Lấy danh sách, sắp xếp đơn mới nhất lên đầu
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        return orderRepository.save(order);
    }
}