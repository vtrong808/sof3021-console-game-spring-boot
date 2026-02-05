package com.console.game.controller.admin;

import com.console.game.dto.CategoryDTO;
import com.console.game.model.Category;
import com.console.game.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryApiAdminController {

    @Autowired
    private CategoryService categoryService;

    // 1. Lấy danh sách (Dùng hàm mới lấy ALL)
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategoriesForAdmin());
    }

    // 2. Lấy chi tiết
    @GetMapping("/{id}")
    public ResponseEntity<Category> getOne(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    // 3. Tạo mới
    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.saveCategory(dto));
    }

    // 4. Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Integer id, @RequestBody CategoryDTO dto) {
        dto.setCategoryId(id);
        return ResponseEntity.ok(categoryService.saveCategory(dto));
    }

    // 5. Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}