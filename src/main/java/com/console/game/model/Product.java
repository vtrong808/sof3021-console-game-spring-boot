package com.console.game.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Products")
@Data
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String productName;
    @Column(columnDefinition = "TEXT")
    private String productDescription;

    private BigDecimal price;
    private Integer stockQuantity;
    private String thumbnailUrl;
    private Boolean isActive = true;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;
}