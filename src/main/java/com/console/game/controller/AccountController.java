package com.console.game.controller;

import com.console.game.model.User;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        // Tạo object User rỗng để bind dữ liệu từ form
        model.addAttribute("user", new User());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute("user") User user) {
        try {
            // Gọi service để lưu user (đã mã hóa pass)
            userService.registerUser(user);
            // Đăng ký thành công -> Chuyển hướng sang trang đăng nhập
            return "redirect:/auth/login?success";
        } catch (Exception e) {
            // Nếu lỗi (ví dụ trùng email), có thể xử lý ở đây
            return "redirect:/account/sign-up?error";
        }
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