package com.console.game.service.impl;

import com.console.game.model.Product;
import com.console.game.repository.ProductRepository;
import com.console.game.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllActiveProducts() {
        // Chỉ lấy sản phẩm đang kinh doanh (isActive = true)
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByProductNameContaining(keyword);
    }
}