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
        Order order = new Order();
        // TỰ ĐỘNG SINH MÃ VẬN ĐƠN (Tracking Number)
        // Kết hợp chữ ORD và thời gian hiện tại để đảm bảo duy nhất
        String trackingNumber = "ORD" + System.currentTimeMillis();
        order.setTrackingNumber(trackingNumber);
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

    @Override
    @Transactional
    public void cancelOrder(Integer orderId, User user) {
        // 1. Tìm đơn hàng
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Bảo mật: Kiểm tra xem đơn hàng này có đúng là của người đang đăng nhập
        // không
        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền hủy đơn hàng này");
        }

        // 3. Logic: Chỉ cho phép hủy nếu đơn hàng đang ở trạng thái PENDING (Chờ xử lý)
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED); // Chuyển sang trạng thái ĐÃ HỦY
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Không thể hủy đơn hàng do đơn đã được xử lý hoặc đã giao");
        }
    }
}