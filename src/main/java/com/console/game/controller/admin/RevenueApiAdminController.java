package com.console.game.controller.admin;

import com.console.game.dto.CategoryRevenueDTO;
import com.console.game.dto.MonthlyRevenueDTO;
import com.console.game.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/revenue")
public class RevenueApiAdminController {

    @Autowired
    private DashboardService dashboardService;

    // 1. Lấy dữ liệu biểu đồ theo năm
    @GetMapping("/chart")
    public ResponseEntity<List<MonthlyRevenueDTO>> getRevenueChart(@RequestParam(defaultValue = "2026") int year) {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenue(year));
    }

    // 2. Lấy số liệu thống kê tổng quan (Cards)
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        BigDecimal totalRevenue = dashboardService.getTotalRevenue();
        Long totalOrders = dashboardService.getTotalOrders();
        Long totalSold = dashboardService.getTotalSoldProducts();
        
        // Tính giá trị trung bình đơn hàng
        BigDecimal avgOrderValue = (totalOrders > 0) 
                ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 0, java.math.RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalOrders", totalOrders);
        stats.put("totalSold", totalSold);
        stats.put("avgOrderValue", avgOrderValue);

        return ResponseEntity.ok(stats);
    }

    // 3. Lấy báo cáo theo danh mục
    @GetMapping("/category")
    public ResponseEntity<List<CategoryRevenueDTO>> getCategoryRevenue() {
        return ResponseEntity.ok(dashboardService.getRevenueByCategory());
    }
}