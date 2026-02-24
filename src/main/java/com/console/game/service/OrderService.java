package com.console.game.service;

import com.console.game.enums.OrderStatus;
import com.console.game.model.CartItem;
import com.console.game.model.CheckoutDTO;
import com.console.game.model.Order;
import com.console.game.model.User;
import java.util.List;

public interface OrderService {

    // --- KHÁCH HÀNG (CUSTOMER) ---

    // Đặt hàng từ giỏ hàng hiện tại
    Order placeOrder(User user, String address, String phone, String fullName, String note);

    // Đặt hàng với danh sách sản phẩm cụ thể và thông tin checkout
    Order placeOrderWithItems(User user, List<CartItem> items, CheckoutDTO checkoutDTO);

    // Người dùng yêu cầu hủy đơn hàng
    void cancelOrder(Integer orderId, User user);

    // --- QUẢN TRỊ (ADMIN) ---

    // Lấy danh sách tất cả đơn hàng trong hệ thống
    List<Order> getAllOrders();

    // Tìm chi tiết một đơn hàng theo ID
    Order getOrderById(Integer id);

    // Cập nhật trạng thái đơn hàng (Ví dụ: Chờ xử lý -> Đang giao)
    Order updateOrderStatus(Integer orderId, OrderStatus status);

    // Xóa hoàn toàn đơn hàng khỏi hệ thống
    void deleteOrder(Integer orderId);
}