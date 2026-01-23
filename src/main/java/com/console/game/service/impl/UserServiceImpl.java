package com.console.game.service.impl;

import com.console.game.enums.Role;
import com.console.game.model.User;
import com.console.game.repository.UserRepository;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Mặc định Role là CUSTOMER
        user.setRole(Role.CUSTOMER);
        // Mặc định Active
        user.setIsActive(true);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}