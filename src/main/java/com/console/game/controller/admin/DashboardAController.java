package com.console.game.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.console.game.dto.MonthlyRevenueDTO;
import com.console.game.repository.OrderRepository;
import com.console.game.service.DashboardService;
import com.console.game.service.UserService;

@Controller
@RequestMapping("/admin")
public class DashboardAController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "2026") int year, Model model) {
        // 1. Số liệu thống kê Cards
        model.addAttribute("totalRevenue", dashboardService.getTotalRevenue());
        model.addAttribute("totalOrders", dashboardService.getTotalOrders());
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("lowStockCount", dashboardService.getLowStockProductCount()); // Mới

        // 2. Biểu đồ doanh thu (Chart)
        List<MonthlyRevenueDTO> revenues = orderRepository.getMonthlyRevenue(year);
        model.addAttribute("monthlyRevenues", revenues);
        model.addAttribute("selectedYear", year); // Để giữ lại giá trị select box

        // 3. Danh sách
        model.addAttribute("recentOrders", dashboardService.getRecentOrders());
        model.addAttribute("topSellingProducts", dashboardService.getTopSellingProducts()); // Mới

        model.addAttribute("menu", "dashboard");
        return "admin/dashboard";
    }
}