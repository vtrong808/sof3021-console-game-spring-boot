package com.console.game.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Integer productId;
    private String productName;
    private BigDecimal price;
    private Integer stockQuantity;
    private String productDescription;
    private String thumbnailUrl;
    private Boolean isActive;
    
    // Chỉ nhận ID từ form, không nhận cả object
    private Integer categoryId; 
    private Integer brandId;
}