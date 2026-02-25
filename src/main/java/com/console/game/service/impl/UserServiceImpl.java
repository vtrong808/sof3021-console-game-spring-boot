package com.console.game.service.impl;

import com.console.game.dto.UserDTO;
import com.console.game.enums.Provider;
import com.console.game.enums.Role;
import com.console.game.model.User;
import com.console.game.repository.UserRepository;
import com.console.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    @Autowired
    private JavaMailSender mailSender;

    // 1. Tạo và gửi OTP
    @Override
    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));

        // Tạo OTP ngẫu nhiên 6 số
        String otp = String.format("%06d", new Random().nextInt(999999));
        
        user.setResetOtp(otp);
        user.setOtpExpiryTime(java.time.LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút
        userRepository.save(user);

        // Gửi mail
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP khôi phục mật khẩu - Console Game Store");
        message.setText("Mã OTP của bạn là: " + otp + "\n\nMã này sẽ hết hạn sau 5 phút. Vui lòng không chia sẻ mã này cho bất kỳ ai.");
        mailSender.send(message);
    }

    // 2. Xác thực OTP và đặt lại mật khẩu mới
    @Override
    public boolean verifyOtpAndResetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getResetOtp() == null) {
            return false;
        }

        // Kiểm tra mã OTP và thời gian hết hạn
        if (user.getResetOtp().equals(otp) && user.getOtpExpiryTime().isAfter(java.time.LocalDateTime.now())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetOtp(null); // Xóa OTP sau khi dùng
            user.setOtpExpiryTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // 3. Đổi mật khẩu cho người dùng đang đăng nhập
    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}