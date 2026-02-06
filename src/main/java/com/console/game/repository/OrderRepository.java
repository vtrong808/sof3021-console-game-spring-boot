package com.console.game.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.console.game.dto.MonthlyRevenueDTO;
import com.console.game.dto.CategoryRevenueDTO;
import com.console.game.model.Order;
import com.console.game.model.User;
import com.console.game.dto.TopSellingProductDTO;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // --- Các hàm cũ giữ nguyên ---
    List<Order> findByUserOrderByOrderDateDesc(User user);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.orderId = :id")
    Optional<Order> findDetailById(@Param("id") Integer id);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status <> com.console.game.enums.OrderStatus.CANCELLED ")
    BigDecimal getTotalRevenue();

    @Query("SELECT COUNT(o) FROM Order o ")
    Long getTotalOrders();

    @Query("""
            SELECT new com.console.game.dto.MonthlyRevenueDTO(
                MONTH(o.orderDate),
                SUM(o.totalAmount)
            )
            FROM Order o
            WHERE YEAR(o.orderDate) = :year
            AND o.status <> com.console.game.enums.OrderStatus.CANCELLED
            GROUP BY MONTH(o.orderDate)
            ORDER BY MONTH(o.orderDate)
            """)
    List<MonthlyRevenueDTO> getMonthlyRevenue(@Param("year") int year);

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC ")
    Page<Order> findRecentOrders(Pageable pageable);

    // --- THÊM CÁC HÀM MỚI NÀY ---

    // 1. Tổng số sản phẩm đã bán (dựa trên OrderItem)
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.order.status <> com.console.game.enums.OrderStatus.CANCELLED")
    Long getTotalSoldProducts();

    // 2. Thống kê doanh thu theo Danh mục
    @Query("""
            SELECT new com.console.game.dto.CategoryRevenueDTO(
                p.category.categoryName,
                SUM(oi.quantity * oi.priceAtPurchase),
                SUM(oi.quantity)
            )
            FROM OrderItem oi
            JOIN oi.product p
            JOIN oi.order o
            WHERE o.status <> com.console.game.enums.OrderStatus.CANCELLED
            GROUP BY p.category.categoryName
            ORDER BY SUM(oi.quantity * oi.priceAtPurchase) DESC
            """)
    List<CategoryRevenueDTO> getRevenueByCategory();

    @Query("""
            SELECT new com.console.game.dto.TopSellingProductDTO(
                p.productName,
                p.thumbnailUrl,
                SUM(oi.quantity),
                SUM(oi.quantity * oi.priceAtPurchase)
            )
            FROM OrderItem oi
            JOIN oi.product p
            JOIN oi.order o
            WHERE o.status <> com.console.game.enums.OrderStatus.CANCELLED
            GROUP BY p.productName, p.thumbnailUrl
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<TopSellingProductDTO> findTopSellingProducts(Pageable pageable);
}