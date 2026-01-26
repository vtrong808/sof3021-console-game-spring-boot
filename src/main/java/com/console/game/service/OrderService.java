package com.console.game.service;

import com.console.game.model.Order;
import com.console.game.model.User;

public interface OrderService {
    Order placeOrder(User user, String address, String phone, String fullName, String note);
    // Các method khác như xem lịch sử, chi tiết...
}
