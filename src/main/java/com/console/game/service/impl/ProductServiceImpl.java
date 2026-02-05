package com.console.game.service.impl;

import com.console.game.model.Product;
import com.console.game.repository.ProductRepository;
import com.console.game.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.console.game.dto.ProductDTO;
import com.console.game.repository.CategoryRepository;
import com.console.game.repository.BrandRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public Page<Product> getActiveProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    @Override
    public List<Product> findByCategoryCategoryId(Integer categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    @Override
    public List<Product> getProductsByMaxPrice(BigDecimal price) {
        return productRepository.findByPriceLessThanEqual(price);
    }

    @Override
    public List<Product> filterProducts(String keyword, Integer categoryId, BigDecimal maxPrice) {
        return productRepository.filterProducts(keyword, categoryId, maxPrice);
    }

    @Override
    public Page<Product> filterProducts(String keyword, Integer categoryId, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.filterProducts(keyword, categoryId, maxPrice, pageable);
    }

    @Override
    public Product saveProduct(ProductDTO dto) {
        Product product = new Product();
        return mapDtoToEntity(dto, product);
    }

    @Override
    public Product updateProduct(Integer id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapDtoToEntity(dto, product);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    // Helper method để map dữ liệu
    private Product mapDtoToEntity(ProductDTO dto, Product product) {
        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setProductDescription(dto.getProductDescription());
        product.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        // Set quan hệ
        if (dto.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));
        }
        if (dto.getBrandId() != null) {
            product.setBrand(brandRepository.findById(dto.getBrandId()).orElse(null));
        }
        return productRepository.save(product);
    }
}