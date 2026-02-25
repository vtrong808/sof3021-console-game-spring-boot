package com.console.game.controller;

import com.console.game.model.Address;
import com.console.game.model.User;
import com.console.game.repository.AddressRepository;
import com.console.game.repository.UserRepository;
import com.console.game.service.UserService;
import com.console.game.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;
    // Thêm Repository này để xử lý bảng Address
    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("/sign-up")
    public String signUp(Model model) { // Tạo object User rỗng để bind dữ liệu từ form
        model.addAttribute("user", new User());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute("user") User user) {
        try {
            // Gọi service để lưu user (đã mã hóa pass)
            userService.registerUser(user);
            return "redirect:/auth/login?verify_email"; // Đăng ký thành công (Yêu cầu check mail) -> Chuyển hướng sang
                                                        // trang đăng nhập
        } catch (Exception e) {
            return "redirect:/account/sign-up?error"; // Nếu lỗi (ví dụ trùng email), có thể xử lý ở đây
        }
    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model, Principal principal) {// Principal chính là đối tượng chứa thông tin user từ
                                                                 // Session
        String email = principal.getName(); // Lấy email của user đã đăng nhập
        User user = userService.findByEmail(email).orElse(null);
        List<Address> addresses = addressService.findAddressByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);
        return "account/edit-profile";
    }

    // 2. XỬ LÝ CẬP NHẬT HỒ SƠ
    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam String fullName,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) Integer addressId,
            Principal principal) {

        if (principal == null)
            return "redirect:/auth/login";

        User currentUser = userService.findByEmail(principal.getName()).orElseThrow();

        // Update user
        currentUser.setFullName(fullName);
        currentUser.setPhoneNumber(phoneNumber);
        userRepository.save(currentUser);

        // Set địa chỉ mặc định
        if (addressId != null) {
            List<Address> addresses = addressRepository.findByUser(currentUser);

            for (Address addr : addresses) {
                addr.setIsDefault(addr.getAddressId().equals(addressId));
                addressRepository.save(addr);
            }
        }

        return "redirect:/account/edit-profile?success";
    }

    @PostMapping("/add-address")
    public String addAddress(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String addressLine,
            @RequestParam String district,
            @RequestParam String city,
            @RequestParam(required = false) Boolean isDefault,
            Principal principal) {

        if (principal == null)
            return "redirect:/auth/login";

        User user = userService.findByEmail(principal.getName()).orElseThrow();

        Address address = new Address();
        address.setUser(user);
        address.setRecipientName(fullName);
        address.setPhoneNumber(phone);
        address.setAddressLine(addressLine);
        address.setDistrict(district);
        address.setCity(city);
        address.setIsDefault(isDefault != null && isDefault);

        // Nếu set mặc định → bỏ mặc định cũ
        if (address.getIsDefault()) {
            List<Address> addresses = addressRepository.findByUser(user);
            for (Address addr : addresses) {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            }
        }
        
        addressRepository.save(address);
        return "redirect:/account/edit-profile?address_added";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        try {
            userService.generateAndSendOtp(email);
            model.addAttribute("step", "otp"); // Chuyển sang bước nhập OTP
            model.addAttribute("email", email);
            model.addAttribute("message", "Mã OTP đã được gửi đến email của bạn!");
        } catch (Exception e) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
        }
        return "account/forgot-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String email, 
                                       @RequestParam String otp, 
                                       @RequestParam String newPassword, 
                                       Model model) {
        boolean isSuccess = userService.verifyOtpAndResetPassword(email, otp, newPassword);
        if (isSuccess) {
            return "redirect:/auth/login?reset_success";
        } else {
            model.addAttribute("step", "otp");
            model.addAttribute("email", email);
            model.addAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn!");
            return "account/forgot-password";
        }
    }

    // === XỬ LÝ ĐỔI MẬT KHẨU (Khi đã đăng nhập) ===
    @PostMapping("/change-password")
    public String processChangePassword(@RequestParam String currentPassword,
                                        @RequestParam String newPassword,
                                        @RequestParam String confirmPassword,
                                        Principal principal, Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "account/change-password";
        }

        boolean isSuccess = userService.changePassword(principal.getName(), currentPassword, newPassword);
        if (isSuccess) {
            model.addAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            model.addAttribute("error", "Mật khẩu hiện tại không đúng!");
        }
        return "account/change-password";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        // Đặt mặc định bước đầu tiên là nhập email
        model.addAttribute("step", "email");
        return "account/forgot-password";
    }

    // 2. Hàm hiển thị trang Đổi mật khẩu (Dành cho chức năng đổi pass)
    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "account/change-password";
    }
}