package com.console.game.service.impl;

import com.console.game.enums.OrderStatus;
import com.console.game.enums.PaymentStatus;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    // --- KHÁCH HÀNG ---

    @Override
    @Transactional
    public Order placeOrder(User user, String address, String phone, String fullName, String note) {
        // Giữ lại để tránh lỗi nếu code cũ còn gọi, bạn có thể triển khai logic tương
        // tự placeOrderWithItems nếu cần
        return null;
    }

    // --- HÀM CHÍNH: Đặt hàng và Xóa giỏ hàng ---
    @Override
    @Transactional
    public Order placeOrderWithItems(User user, List<CartItem> items, CheckoutDTO dto) {
        // 1. Khởi tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setFullName(dto.getFullName());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setShippingAddress(dto.getAddress());
        order.setNote(dto.getNote());
        order.setPaymentMethod(dto.getPaymentMethod());

        // Tự động sinh mã vận đơn (Tracking Number)
        String trackingNumber = "ORD" + System.currentTimeMillis();
        order.setTrackingNumber(trackingNumber);

        // Trạng thái mặc định
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);

        // 2. Tính tổng tiền
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        order.setTotalAmount(totalAmount);

        // 3. Lưu Order
        Order savedOrder = orderRepository.save(order);

        // 4. Tạo chi tiết đơn hàng (Order Items)
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            // Lưu giá tại thời điểm mua (Price Freezing)
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        // 5. Xóa sản phẩm khỏi giỏ hàng
        cartItemRepository.deleteAll(items);

        return savedOrder;
    }

    @Override
    @Transactional
    public void cancelOrder(Integer orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // Bảo mật: Kiểm tra quyền sở hữu
        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không có quyền hủy đơn hàng này");
        }

        // Chỉ cho phép hủy khi đang chờ xử lý (PENDING) hoặc đã xác nhận (CONFIRMED)
        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Không thể hủy đơn hàng do đơn đã được vận chuyển hoặc hoàn thành");
        }
    }

    // --- QUẢN TRỊ (ADMIN) ---

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        // Tự động cập nhật trạng thái thanh toán nếu đã giao hàng thành công
        if (status == OrderStatus.DELIVERED || status == OrderStatus.COMPLETED) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng để xóa"));

        // Xóa chi tiết trước để tránh lỗi ràng buộc khóa ngoại
        if (order.getOrderItems() != null) {
            orderItemRepository.deleteAll(order.getOrderItems());
        }

        orderRepository.delete(order);
    }
}