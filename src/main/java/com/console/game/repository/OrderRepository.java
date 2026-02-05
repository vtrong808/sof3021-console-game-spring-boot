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
import com.console.game.model.Order;
import com.console.game.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByOrderDateDesc(User user);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.orderId = :id")
    Optional<Order> findDetailById(@Param("id") Integer id);

    // Tính tổng doanh thu cho ADMIN
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status <> com.console.game.enums.OrderStatus.CANCELLED ")
    BigDecimal getTotalRevenue();

    // Tính tổng số đơn hàng cho ADMIN
    @Query("SELECT COUNT(o) FROM Order o ")
    Long getTotalOrders();

    // Truy vấn doanh số theo năm cho ADMIN
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

    // 5 Đơn hàng mới nhất cho ADMIN
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC ")
    Page<Order> findRecentOrders(Pageable pageable);

}
