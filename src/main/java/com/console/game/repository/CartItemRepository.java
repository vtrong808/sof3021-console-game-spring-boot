package com.console.game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.console.game.model.CartItem;
import com.console.game.model.Product;
import com.console.game.model.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    List<CartItem> findByCartItemIdInAndUser(List<Integer> ids, User user);
}
