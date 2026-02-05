package com.console.game.controller.common;

import com.console.game.model.User;
import com.console.game.service.CartService;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttribute {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @ModelAttribute("cartItemCount")
    public int cartItemCount(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return 0;
        }

        return cartService.getCartItemCount(user);
    }
}
