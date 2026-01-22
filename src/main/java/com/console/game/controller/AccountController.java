package com.console.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    @GetMapping("/sign-up")
    public String signUp() {
        return "account/sign-up";
    }

    @GetMapping("/edit-profile")
    public String editProfile() {
        return "account/edit-profile";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "account/forgot-password";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "account/change-password";
    }

}
