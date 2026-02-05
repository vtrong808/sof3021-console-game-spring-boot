package com.console.game.service;

import com.console.game.model.User;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);

    Optional<User> findByEmail(String email);

    long getTotalUsers(); // Đếm số lượng người dùng cho ADMIN
}