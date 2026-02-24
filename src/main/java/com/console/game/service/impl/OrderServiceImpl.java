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

    // Hàm cũ (Giữ lại để tránh lỗi nếu code cũ còn gọi)
    @Override
    @Transactional
    public Order placeOrder(User user, String address, String phone, String fullName, String note) {
        return null;
    }

    // --- HÀM CHÍNH: Đặt hàng và Xóa giỏ hàng ---
    @Override
    @Transactional
    public Order placeOrderWithItems(User user, List<CartItem> items, CheckoutDTO dto) {
        // 1. Khởi tạo đơn hàng (Order) từ thông tin CheckoutDTO
        Order order = new Order();
        order.setUser(user);
        order.setFullName(dto.getFullName());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setShippingAddress(dto.getAddress());
        order.setNote(dto.getNote());
        order.setPaymentMethod(dto.getPaymentMethod());

        // Cài đặt các trạng thái mặc định
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING); // Chờ xử lý
        order.setPaymentStatus(PaymentStatus.UNPAID); // Chưa thanh toán

        // 2. Tính tổng tiền đơn hàng
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        order.setTotalAmount(totalAmount);

        // 3. Lưu Order xuống database để lấy ID
        Order savedOrder = orderRepository.save(order);

        // 4. Tạo chi tiết đơn hàng (Order Items)
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice()); // Lưu giá tại thời điểm mua (Price
                                                                            // Freezing)

            orderItems.add(orderItem);
        }
        // Lưu danh sách Order Items
        orderItemRepository.saveAll(orderItems);

        // --- QUAN TRỌNG: XÓA CÁC SẢN PHẨM ĐÃ MUA KHỎI GIỎ HÀNG ---
        cartItemRepository.deleteAll(items);
        // ---------------------------------------------------------

        return savedOrder;
    }

    // --- CÁC HÀM QUẢN LÝ (ADMIN) ---

    @Override
    public List<Order> getAllOrders() {
        // Lấy tất cả đơn hàng, sắp xếp mới nhất lên đầu
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

        // Nếu chuyển sang trạng thái đã giao (DELIVERED) thì cập nhật luôn là đã thanh
        // toán (PAID)
        if (status == OrderStatus.DELIVERED) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Integer orderId) {
        // 1. Tìm đơn hàng
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Xóa các chi tiết đơn hàng (OrderItem) trước để không bị lỗi khóa ngoại
        // (Nếu bạn đã cài cascade = CascadeType.ALL trong entity thì có thể bỏ qua bước
        // 2 này)
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            orderItemRepository.deleteAll(order.getOrderItems());
        }

        // 3. Cuối cùng mới xóa đơn hàng
        orderRepository.delete(order);
    }
}