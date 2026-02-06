package com.console.game.service;

import com.console.game.enums.OrderStatus;
import com.console.game.model.CartItem;
import com.console.game.model.CheckoutDTO;
import com.console.game.model.Order;
import com.console.game.model.User;
import java.util.List;

public interface OrderService {
    // --- KHÁCH HÀNG ---
    Order placeOrder(User user, String address, String phone, String fullName, String note);
    Order placeOrderWithItems(User user, List<CartItem> items, CheckoutDTO checkoutDTO);

    // --- ADMIN ---
    List<Order> getAllOrders();
    Order getOrderById(Integer id);
    Order updateOrderStatus(Integer orderId, OrderStatus status);
}