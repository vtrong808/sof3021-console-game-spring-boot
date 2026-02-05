package com.console.game.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.console.game.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsActiveTrue();

    // Tìm kiếm theo tên (Tối ưu hơn)
    @Query("SELECT DISTINCT p FROM Product p WHERE p.isActive = true AND p.productName LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);

    // Tìm kiếm theo ID
    List<Product> findByCategoryCategoryId(Integer categoryId);

    // Tìm kiếm theo giá
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.price <= :maxPrice")
    List<Product> findByPriceLessThanEqual(@Param("maxPrice") BigDecimal maxPrice);

    // Tìm kiếm đa điều kiện
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (:keyword IS NULL OR p.productName LIKE %:keyword%) AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> filterProducts(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("maxPrice") BigDecimal maxPrice);

    // Tìm kiếm đa kiều kiện và phân trang
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (:keyword IS NULL OR p.productName LIKE %:keyword%) AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> filterProducts(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

}
