package com.console.game.service.impl;

import com.console.game.dto.CategoryDTO;
import com.console.game.model.Category;
import com.console.game.repository.CategoryRepository;
import com.console.game.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findByIsActiveTrue(); // Chỉ lấy cái đang hiện cho trang chủ
    }

    @Override
    public List<Category> getAllCategoriesForAdmin() {
        return categoryRepository.findAll(); // Lấy tất cả cho Admin
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category saveCategory(CategoryDTO dto) {
        Category category;
        if (dto.getCategoryId() != null) {
            // Update
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } else {
            // Create New
            category = new Category();
        }

        category.setCategoryName(dto.getCategoryName());
        category.setCategoryDescription(dto.getCategoryDescription());
        category.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
    }
}