package com.console.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.console.game.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsActiveTrue();

    // Tìm kiếm sản phẩm theo tên
    List<Product> findByProductNameContaining(String keyword);
}
