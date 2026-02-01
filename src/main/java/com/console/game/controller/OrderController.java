package com.console.game.controller;

import com.console.game.enums.PaymentMethod;
import com.console.game.model.CartItem;
import com.console.game.model.Order;
import com.console.game.model.User;
import com.console.game.repository.OrderRepository;
import com.console.game.service.CartService;
import com.console.game.service.OrderService;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;
    @Autowired private UserService userService;
    
    // 1. THÊM CÁI NÀY: Để lưu và lấy đơn hàng từ Database
    @Autowired private OrderRepository orderRepository; 

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal) {
        if (principal == null) return "redirect:/auth/login";
        
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<CartItem> cartItems = cartService.getCartItems(user);
        
        if (cartItems.isEmpty()) {
            return "redirect:/cart/view";
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", cartService.getTotalAmount(user));
        model.addAttribute("user", user);
        
        return "order/check-out";
    }

    // 2. SỬA HÀM NÀY: Thêm nhận paymentMethod và cập nhật vào đơn hàng
    @PostMapping("/checkout")
    public String processCheckout(Principal principal,
                                  @RequestParam("fullName") String fullName,
                                  @RequestParam("phoneNumber") String phoneNumber,
                                  @RequestParam("address") String address,
                                  @RequestParam(value = "note", required = false) String note,
                                  @RequestParam("paymentMethod") String paymentMethod) { // <-- Nhận dữ liệu từ form
        
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        try {
            // Tạo đơn hàng
            Order order = orderService.placeOrder(user, address, phoneNumber, fullName, note);

            // Cập nhật phương thức thanh toán dựa trên lựa chọn của khách
            if ("BANK_TRANSFER".equals(paymentMethod)) {
                order.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
            } else {
                order.setPaymentMethod(PaymentMethod.COD);
            }
            
            // Lưu cập nhật vào Database
            orderRepository.save(order); 

            return "redirect:/order/list?success";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/order/checkout?error";
        }
    }

    // 3. SỬA HÀM NÀY: Lấy danh sách đơn hàng thật thay vì để TODO
    @GetMapping("/list")
    public String list(Model model, Principal principal) {
        if (principal == null) return "redirect:/auth/login";
        
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        
        // Lấy danh sách đơn hàng của user đó (Sắp xếp mới nhất lên đầu)
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        model.addAttribute("orders", orders);
        
        return "order/order-list";
    }

    // 4. THÊM HÀM NÀY: Xem chi tiết đơn hàng
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Integer orderId, Model model, Principal principal) {
        if (principal == null) return "redirect:/auth/login";
        
        Order order = orderRepository.findById(orderId).orElse(null);
        User currentUser = userService.findByEmail(principal.getName()).orElseThrow();

        // Kiểm tra bảo mật: Chỉ cho xem nếu đơn hàng thuộc về user đang đăng nhập
        if (order == null || !order.getUser().getUserId().equals(currentUser.getUserId())) {
             return "redirect:/order/list";
        }

        model.addAttribute("order", order);
        return "order/order-detail";
    }
}