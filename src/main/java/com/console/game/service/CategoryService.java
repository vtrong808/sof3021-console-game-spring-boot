package com.console.game.service;

import com.console.game.dto.CategoryDTO;
import com.console.game.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategory();

    List<Category> getAllCategoriesForAdmin(); // Lấy cả ẩn/hiện để quản lý
    Category getCategoryById(Integer id);
    Category saveCategory(CategoryDTO categoryDTO);
    void deleteCategory(Integer id);
}