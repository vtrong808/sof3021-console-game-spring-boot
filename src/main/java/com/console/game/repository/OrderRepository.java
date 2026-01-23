package com.console.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.console.game.model.Order;
import com.console.game.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
}
