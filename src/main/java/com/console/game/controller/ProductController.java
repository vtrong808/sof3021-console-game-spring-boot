package com.console.game.controller;

import com.console.game.model.Product;
import com.console.game.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // Trang chủ
    @GetMapping({ "/", "/home", "/home/index" })
    public String home(Model model) {
        // Lấy danh sách sản phẩm hiển thị trang chủ
        model.addAttribute("products", productService.getAllActiveProducts());
        return "home/index";
    }

    // Danh sách sản phẩm (có hỗ trợ tìm kiếm)
    @GetMapping("/product/list")
    public String listAll(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("products", productService.searchProducts(keyword));
        } else {
            model.addAttribute("products", productService.getAllActiveProducts());
        }
        return "product/list";
    }

    // Chi tiết sản phẩm
    @GetMapping("/product/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        
        if (product.isPresent()) {
            model.addAttribute("p", product.get());
            return "product/detail";
        } else {
            // Nếu không tìm thấy sản phẩm, quay về trang danh sách
            return "redirect:/product/list";
        }
    }

    // Lọc theo danh mục (Tạm thời để placeholder vì chưa làm Service phần này)
    @GetMapping("/product/list-by-category/{id}")
    public String listByCategory(@PathVariable("id") Integer id, Model model) {
        // TODO: Bổ sung method findByCategoryId vào ProductService sau
        return "redirect:/product/list";
    }
}