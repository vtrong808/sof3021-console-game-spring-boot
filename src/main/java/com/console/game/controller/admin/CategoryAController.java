package com.console.game.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CategoryAController {
    @GetMapping("/category")
    public String category() {
        return "admin/category";
    }
}
