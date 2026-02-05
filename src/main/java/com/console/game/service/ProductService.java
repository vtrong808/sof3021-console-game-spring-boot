package com.console.game.service;

import com.console.game.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    List<Product> getAllActiveProducts();

    Page<Product> getActiveProducts(Pageable pageable);

    Optional<Product> getProductById(Integer id);

    List<Product> searchProducts(String keyword);
    // Sau này có thể thêm phân trang (Pageable)

    List<Product> findByCategoryCategoryId(Integer categoryId); // Lọc danh mục

    List<Product> getProductsByMaxPrice(BigDecimal price); // Lọc giá

    List<Product> filterProducts(String keyword, Integer categoryId, BigDecimal maxPrice); // Lọc đa điều kiện

    Page<Product> filterProducts(String keyword, Integer categoryId, BigDecimal maxPrice, Pageable pageable);   // Tìm kiếm đa điều kiện và phân trang
}