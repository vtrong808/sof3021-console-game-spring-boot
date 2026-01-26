package com.console.game.service;

import com.console.game.model.CartItem;
import com.console.game.model.User;
import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    void addToCart(Integer productId, Integer quantity, String email);
    
    List<CartItem> getCartItems(User user);
    
    void updateQuantity(Integer cartItemId, Integer quantity);
    
    void removeFromCart(Integer cartItemId);
    
    void clearCart(User user);
    
    BigDecimal getTotalAmount(User user);
}