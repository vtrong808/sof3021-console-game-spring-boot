package com.console.game.service;

import com.console.game.dto.UserDTO;
import com.console.game.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    User registerUser(User user);

    Optional<User> findByEmail(String email);

    long getTotalUsers(); // Đếm số lượng người dùng cho ADMIN

    List<User> getAllUsers();

    User getUserById(Integer id);

    User saveUserForAdmin(UserDTO userDTO); // Thêm mới hoặc update

    void deleteUser(Integer id);

    void generateAndSendOtp(String email);

    boolean verifyOtpAndResetPassword(String email, String otp, String newPassword);

    boolean changePassword(String email, String oldPassword, String newPassword);
}