package com.console.game.service;

import com.console.game.model.CartItem;
import com.console.game.model.User;
import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    void addToCart(Integer productId, Integer quantity, String email); // Thêm sản phẩm vào giỏ

    List<CartItem> getCartItems(User user); // Lấy danh sách mặt hàng trong giỏ

    List<CartItem> getCartItemsByIds(List<Integer> ids, User user); // Lấy danh sách được chọn trong giỏ

    BigDecimal getTotalAmount(List<CartItem> items);    // Tính tổng tiền các mặt hàng được chọn trong giỏ

    void updateQuantity(Integer cartItemId, Integer quantity); // Cập nhật số lượng

    void removeFromCart(Integer cartItemId);

    void clearCart(User user);

    BigDecimal getTotalAmount(User user);
}