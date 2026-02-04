package com.console.game.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ReportAController {
    @GetMapping("/revenue")
    public String revenue() {
        return "admin/revenue";
    }

    @GetMapping("/vip")
    public String vip() {
        return "admin/vip";
    }
}
