package com.console.game.controller.admin;

import com.console.game.dto.OrderDTO;
import com.console.game.dto.OrderItemDTO;
import com.console.game.enums.OrderStatus;
import com.console.game.model.Order;
import com.console.game.model.OrderItem;
import com.console.game.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
public class OrderApiAdminController {

    @Autowired
    private OrderService orderService;

    // 1. Lấy tất cả đơn hàng
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> dtos = orders.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 2. Lấy chi tiết đơn hàng
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOne(@PathVariable Integer id) {
        Order order = orderService.getOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertToDTO(order));
    }

    // 3. Cập nhật trạng thái
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            orderService.updateOrderStatus(id, newStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
        }
    }

    // --- Helper chuyển đổi Entity -> DTO ---
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerName(order.getFullName() != null ? order.getFullName() : order.getUser().getUsername());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setAddress(order.getShippingAddress());
        dto.setNote(order.getNote());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setPaymentMethod(order.getPaymentMethod().name());
        dto.setPaymentStatus(order.getPaymentStatus().name());

        // Map danh sách sản phẩm
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setProductName(item.getProduct().getProductName());
            itemDto.setProductThumbnail(item.getProduct().getThumbnailUrl());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPriceAtPurchase());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}