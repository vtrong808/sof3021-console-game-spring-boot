package com.console.game.config;

import com.console.game.enums.Role;
import com.console.game.model.Brand;
import com.console.game.model.Category;
import com.console.game.model.Product;
import com.console.game.model.User;
import com.console.game.repository.BrandRepository; // Import BrandRepository
import com.console.game.repository.CategoryRepository;
import com.console.game.repository.ProductRepository;
import com.console.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(ProductRepository productRepo,
            CategoryRepository categoryRepo,
            BrandRepository brandRepo, // <--- Thêm tham số này
            UserRepository userRepo) {
        return args -> {
            if (userRepo.count() == 0) {
                User admin = new User();
                admin.setFullName("Admin System");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("123"));
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                userRepo.save(admin);

                User user = new User();
                user.setFullName("Nguyễn Văn Khách");
                user.setEmail("user@gmail.com");
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole(Role.CUSTOMER);
                user.setIsActive(true);
                userRepo.save(user);
            }

            if (productRepo.count() == 0) {
                // Tạo Danh mục mẫu nếu chưa có
                if (categoryRepo.count() == 0) {
                    categoryRepo.save(createCategory("Console"));
                    categoryRepo.save(createCategory("Game Disc"));
                    categoryRepo.save(createCategory("Accessory"));
                }

                // Tạo Thương hiệu mẫu nếu chưa có <--- MỚI
                if (brandRepo.count() == 0) {
                    brandRepo.save(createBrand("Sony"));
                    brandRepo.save(createBrand("Nintendo"));
                    brandRepo.save(createBrand("Microsoft"));
                }

                Category cat = categoryRepo.findAll().get(0);
                Brand brand = brandRepo.findAll().get(0); // Lấy thương hiệu đầu tiên (Sony) <--- MỚI

                for (int i = 1; i <= 200; i++) {
                    Product p = new Product();
                    p.setProductName("Sản phẩm Demo " + i);
                    p.setProductDescription("Mô tả sản phẩm " + i);
                    p.setPrice(new BigDecimal(100000 + i * 1000));
                    p.setStockQuantity(50);
                    p.setIsActive(true);
                    p.setThumbnailUrl("https://placehold.co/300x300?text=Product+" + i);

                    p.setCategory(cat);
                    p.setBrand(brand); // <--- QUAN TRỌNG: Gán thương hiệu vào sản phẩm

                    productRepo.save(p);
                }
            }
        };
    }

    private Category createCategory(String name) {
        Category c = new Category();
        c.setCategoryName(name);
        c.setIsActive(true);
        return c;
    }

    // Hàm tạo Brand <--- MỚI
    private Brand createBrand(String name) {
        Brand b = new Brand();
        b.setBrandName(name);
        return b;
    }
}