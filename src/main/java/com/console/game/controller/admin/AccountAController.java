package com.console.game.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AccountAController {
    @GetMapping("/account")
    public String account() {
        return "admin/account";
    }
}
