package com.console.game.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;
    private String categoryDescription;
    private Boolean isActive;
}