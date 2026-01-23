package com.console.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.console.game.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
