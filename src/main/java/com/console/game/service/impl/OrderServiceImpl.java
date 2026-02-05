package com.console.game.service.impl;

import com.console.game.enums.OrderStatus;
import com.console.game.model.*;
import com.console.game.repository.CartItemRepository;
import com.console.game.repository.OrderItemRepository;
import com.console.game.repository.OrderRepository;
import com.console.game.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional
    public Order placeOrder(User user, String address, String phone, String fullName, String note) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng");
        }

        // 1. Tạo Order mới
        Order order = new Order();
        order.setUser(user);
        order.setFullName(fullName);
        order.setPhoneNumber(phone);
        order.setShippingAddress(address);
        order.setNote(note);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // Tính tổng tiền
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal linePrice = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(linePrice);
        }
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // 2. Chuyển CartItem -> OrderItem
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(item.getProduct().getPrice()); // Lưu giá tại thời điểm mua

            orderItemRepository.save(orderItem);
        }

        // 3. Xóa giỏ hàng
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    @Transactional
    public Order placeOrderWithItems(
            User user,
            List<CartItem> items,
            CheckoutDTO dto) {

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Không có sản phẩm để thanh toán");
        }

        // 1. Tạo Order
        Order order = new Order();
        order.setUser(user);
        order.setFullName(dto.getFullName());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setShippingAddress(dto.getAddress());
        order.setNote(dto.getNote());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // 2. Tính tổng tiền
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal line = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(line);
        }
        order.setTotalAmount(total);

        orderRepository.save(order);

        // 3. CartItem → OrderItem
        for (CartItem item : items) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(item.getProduct());
            oi.setQuantity(item.getQuantity());
            oi.setPriceAtPurchase(item.getProduct().getPrice());

            orderItemRepository.save(oi);
        }

        // 4. Xóa cart item đã thanh toán
        cartItemRepository.deleteAll(items);

        return order;
    }
}