package com.console.game.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Categories")
@Data
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String categoryName;
    private String categoryDescription;
    private Boolean isActive = true;
}
