package com.console.game.service.impl;

import com.console.game.model.CartItem;
import com.console.game.model.Product;
import com.console.game.model.User;
import com.console.game.repository.CartItemRepository;
import com.console.game.repository.ProductRepository;
import com.console.game.repository.UserRepository;
import com.console.game.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public int getCartItemCount(User user) {
        return cartItemRepository.findByUser(user).stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addToCart(Integer productId, Integer quantity, String email) {
        // Tìm User theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Tìm Product theo Id
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            // Nếu có rồi -> Cộng dồn số lượng
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // Nếu chưa có -> Tạo mới
            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Override
    public List<CartItem> getCartItemsByIds(List<Integer> ids, User user) {
        return cartItemRepository.findByCartItemIdInAndUser(ids, user);
    }

    @Override
    public BigDecimal getTotalAmount(List<CartItem> items) {
        return items.stream()
                .map(i -> i.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void updateQuantity(Integer cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId).orElse(null);
        if (item != null && quantity > 0) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    public void removeFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional // Thêm Transactional để đảm bảo xóa thành công
    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }

    @Override
    public BigDecimal getTotalAmount(User user) {
        List<CartItem> items = getCartItems(user);

        // Tính tổng tiền bằng BigDecimal để tránh lỗi làm tròn
        return items.stream()
                .map(item -> {
                    BigDecimal price = item.getProduct().getPrice();
                    BigDecimal qty = BigDecimal.valueOf(item.getQuantity());
                    return price.multiply(qty);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}