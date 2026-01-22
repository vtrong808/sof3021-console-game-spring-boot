package com.console.game.service;

import com.console.game.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllActiveProducts();

    Optional<Product> getProductById(Integer id);

    List<Product> searchProducts(String keyword);
    // Sau này có thể thêm phân trang (Pageable)
}