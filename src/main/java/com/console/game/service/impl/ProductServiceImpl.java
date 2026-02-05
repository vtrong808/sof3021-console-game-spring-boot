package com.console.game.service.impl;

import com.console.game.model.Product;
import com.console.game.repository.ProductRepository;
import com.console.game.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public Page<Product> getActiveProducts(Pageable pageable) {     // Load sản phẩm có phân trang cho trang chủ
        return productRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword); // Tìm kiếm tên tối uue
    }

    @Override
    public List<Product> findByCategoryCategoryId(Integer categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId); // Tìm theo loại
    }

    @Override
    public List<Product> getProductsByMaxPrice(BigDecimal price) {
        return productRepository.findByPriceLessThanEqual(price); // Tìm theo giá
    }

    @Override
    public List<Product> filterProducts(String keyword, Integer categoryId, BigDecimal maxPrice) {
        return productRepository.filterProducts(keyword, categoryId, maxPrice); // Tìm kiếm đa đièu kiện
    }

    @Override
    public Page<Product> filterProducts(String keyword, Integer categoryId, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.filterProducts(keyword, categoryId, maxPrice, pageable); // Tìm kiếm đa điều kiện và
                                                                                          // phân trang
    }
}