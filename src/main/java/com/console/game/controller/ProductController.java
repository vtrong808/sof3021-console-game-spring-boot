package com.console.game.controller;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ProductController {

    @GetMapping({ "/", "home", "/home/index" })
    public String home() {
        return "home/index";
    }

    @GetMapping("/product/list")
    public String listAll() {
        return "product/list";
    }

    @GetMapping("/product/list-by-category/{id}")
    public String listByCategory(@PathVariable("id") long id) {
        return "product/list";
    }

    @GetMapping("/product/detail/{id}")
    public String detail(@PathVariable("id") Long id) {
        return "product/detail";
    }

}
