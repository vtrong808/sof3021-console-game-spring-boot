package com.console.game.repository;

import com.console.game.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

       // Tìm sản phẩm đang hoạt động
       List<Product> findByIsActiveTrue();

       // Load sản phẩm lên trang chủ có phân trang
       Page<Product> findByIsActiveTrue(Pageable pageable);

       // Tìm kiếm theo tên (Tối ưu hơn)
       @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.productName LIKE %:keyword%")
       List<Product> searchProducts(@Param("keyword") String keyword);

       // Sản phẩm khuyến mãi (có giảm giá)
       List<Product> findByIsActiveTrueAndDiscountPercentGreaterThan(Integer percent);

       // Sản phẩm hot
       List<Product> findByIsActiveTrueAndIsHotTrue();

       // Phân trang
       Page<Product> findByIsActiveTrueAndDiscountPercentGreaterThan(Integer percent, Pageable pageable);

       // Tìm kiếm theo Category ID
       // Lưu ý: Trong Product entity, tên field là 'category', nên Spring Data sẽ hiểu
       // là category.categoryId
       List<Product> findByCategoryCategoryId(Integer categoryId);

       // Tìm kiếm theo giá
       @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.price <= :maxPrice")
       List<Product> findByPriceLessThanEqual(@Param("maxPrice") BigDecimal maxPrice);

       // Tìm kiếm đa điều kiện
       @Query("SELECT p FROM Product p WHERE p.isActive = true " +
                     "AND (:keyword IS NULL OR p.productName LIKE %:keyword%) " +
                     "AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) " +
                     "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
       List<Product> filterProducts(
                     @Param("keyword") String keyword,
                     @Param("categoryId") Integer categoryId,
                     @Param("maxPrice") BigDecimal maxPrice);

       // Tìm kiếm đa điều kiện và phân trang
       @Query("SELECT p FROM Product p WHERE p.isActive = true " +
                     "AND (:keyword IS NULL OR p.productName LIKE %:keyword%) " +
                     "AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) " +
                     "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
       Page<Product> filterProducts(
                     @Param("keyword") String keyword,
                     @Param("categoryId") Integer categoryId,
                     @Param("maxPrice") BigDecimal maxPrice,
                     Pageable pageable);

       Long countByStockQuantityLessThan(Integer quantity);
}