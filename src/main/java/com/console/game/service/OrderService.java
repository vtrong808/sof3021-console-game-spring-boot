package com.console.game.service;

import java.util.List;

import com.console.game.model.CartItem;
import com.console.game.model.CheckoutDTO;
import com.console.game.model.Order;
import com.console.game.model.User;

public interface OrderService {
    Order placeOrder(User user, String address, String phone, String fullName, String note);
    
    // THANH TOÁN SẢN PHẨM ĐƯỢC CHỌN
    Order placeOrderWithItems(User user, List<CartItem> items, CheckoutDTO checkoutDTO);
}
