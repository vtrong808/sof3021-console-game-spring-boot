package com.console.game.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/checkout")
    public String checkout() {
        return "order/check-out";
    }

    @GetMapping("/list")
    public String list() {
        return "order/order-list";
    }

    @GetMapping("/detail")
    public String detail() {
        return "order/order-detail";
    }

    @GetMapping("/my-product-list")
    public String myProductList() {
        return "order/my-product-list";
    }
}
