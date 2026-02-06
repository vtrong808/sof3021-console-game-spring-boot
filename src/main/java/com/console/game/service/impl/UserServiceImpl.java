package com.console.game.service.impl;

import com.console.game.dto.UserDTO;
import com.console.game.enums.Provider;
import com.console.game.enums.Role;
import com.console.game.model.User;
import com.console.game.repository.UserRepository;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setIsActive(true);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public long getTotalUsers(){
        return userRepository.count();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveUserForAdmin(UserDTO dto) {
        User user;

        if (dto.getUserId() != null) {
            // Update
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            // Create New
            user = new User();
            user.setProvider(Provider.LOCAL); // Mặc định là Local
        }

        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        // Xử lý Role
        if (dto.getRole() != null) {
            user.setRole(Role.valueOf(dto.getRole())); 
        } else {
            user.setRole(Role.CUSTOMER);
        }

        // Xử lý Password (KHÔNG MÃ HÓA theo yêu cầu)
        // Chỉ cập nhật password nếu người dùng nhập vào ô password (không để trống)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword()); 
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
    }
}