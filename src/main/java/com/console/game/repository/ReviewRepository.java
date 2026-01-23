package com.console.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.console.game.model.Product;
import com.console.game.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProduct(Product product);
}
