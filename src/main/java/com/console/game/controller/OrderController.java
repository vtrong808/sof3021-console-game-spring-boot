package com.console.game.controller;

import com.console.game.model.CartItem;
import com.console.game.model.User;
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

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal) {
        if (principal == null) return "redirect:/auth/login";
        
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<CartItem> cartItems = cartService.getCartItems(user);
        
        if (cartItems.isEmpty()) {
            return "redirect:/cart/view"; // Giỏ hàng trống thì quay về giỏ
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", cartService.getTotalAmount(user));
        model.addAttribute("user", user); // Để fill sẵn thông tin người dùng vào form
        
        return "order/check-out";
    }

    @PostMapping("/checkout")
    public String processCheckout(Principal principal,
                                  @RequestParam("fullName") String fullName,
                                  @RequestParam("phoneNumber") String phoneNumber,
                                  @RequestParam("address") String address,
                                  @RequestParam(value = "note", required = false) String note) {
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        try {
            orderService.placeOrder(user, address, phoneNumber, fullName, note);
            return "redirect:/order/list?success";
        } catch (Exception e) {
            return "redirect:/order/checkout?error";
        }
    }

    @GetMapping("/list")
    public String list() {
        // TODO: Gọi Service lấy danh sách đơn hàng của user
        return "order/order-list";
    }
}