package com.console.game.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "brands")
@Data
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandId;

    private String brandName;
    private String brandDescription;
    private String logoUrl;
}