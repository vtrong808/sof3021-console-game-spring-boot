package com.console.game.controller;

import com.console.game.model.Product;
import com.console.game.service.CategoryService;
import com.console.game.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Trang chủ
    @GetMapping({ "/", "/home", "/home/index" })
    public String home(
            Model model,
            @RequestParam(defaultValue = "0") int page) {
        int size = 16; // 16 sản phẩm / trang

        PageRequest pageable = PageRequest.of(page, size, Sort.by("productId").descending());
        Page<Product> productPage = productService.getActiveProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "home/index";
    }

    // Danh sách sản phẩm tìm kiếm đa điều kiện
    @GetMapping("/product/list")
    public String listAll(
            Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page) {
        int size = 9; // 9 sản phẩm / trang

        PageRequest pageable = PageRequest.of(page, size, Sort.by("productId").descending());
        Page<Product> productPage = productService.filterProducts(keyword, categoryId, maxPrice, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        // giữ lại filter khi chuyển trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("maxPrice", maxPrice);

        model.addAttribute("categories", categoryService.getAllCategory());
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