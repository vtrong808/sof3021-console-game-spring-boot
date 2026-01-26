package com.console.game.controller;

import com.console.game.model.CartItem;
import com.console.game.model.User;
import com.console.game.service.CartService;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/view")
    public String viewCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        String email = principal.getName();
        User user = userService.findByEmail(email).orElse(null);
        
        List<CartItem> cartItems = cartService.getCartItems(user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", cartService.getTotalAmount(user));
        return "cart/cart";
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") Integer productId, 
                            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                            Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        cartService.addToCart(productId, quantity, principal.getName());
        return "redirect:/cart/view";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable("id") Integer cartItemId) {
        cartService.removeFromCart(cartItemId);
        return "redirect:/cart/view";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("id") Integer cartItemId, 
                             @RequestParam("quantity") Integer quantity) {
        cartService.updateQuantity(cartItemId, quantity);
        return "redirect:/cart/view";
    }

    @GetMapping("/clear")
    public String clearCart(Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElse(null);
        cartService.clearCart(user);
        return "redirect:/cart/view";
    }

    @GetMapping("/update/{id}")
    public String updateCartQuantity(@PathVariable("id") Integer cartItemId,
                                     @RequestParam("qty") Integer quantity) {
        // Gọi service để cập nhật số lượng
        cartService.updateQuantity(cartItemId, quantity);
        
        // Load lại trang giỏ hàng
        return "redirect:/cart/view";
    }
}