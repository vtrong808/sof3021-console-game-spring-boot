package com.console.game.controller.customer;

import com.console.game.model.User;
import com.console.game.service.CartService;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttribute {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userService.findByEmail(authentication.getName()).orElse(null);
    }

    @ModelAttribute("cartItemCount")
    public int cartItemCount(@ModelAttribute("currentUser") User user) {
        if (user == null)
            return 0;
        return cartService.getCartItemCount(user);
    }
}