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
    public String updateProfile(@RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String addressStr, // Nhận chuỗi địa chỉ từ form
            Principal principal) {
        if (principal == null)
            return "redirect:/auth/login";

        // 1. Cập nhật bảng User (Tên, SĐT)
        User currentUser = userService.findByEmail(principal.getName()).orElseThrow();
        currentUser.setFullName(fullName);
        currentUser.setPhoneNumber(phoneNumber);
        userRepository.save(currentUser);

        // 2. Cập nhật bảng Address (Địa chỉ)
        List<Address> addresses = addressRepository.findByUser(currentUser);
        Address userAddress;

        if (addresses.isEmpty()) {
            // Nếu chưa có địa chỉ nào -> Tạo mới
            userAddress = new Address();
            userAddress.setUser(currentUser);
            userAddress.setIsDefault(true);
        } else {
            // Nếu có rồi -> Lấy cái đầu tiên để sửa
            userAddress = addresses.get(0);
        }

        // Set thông tin vào bảng Address
        userAddress.setAddressLine(addressStr);
        userAddress.setRecipientName(fullName); // Tên người nhận mặc định là tên user
        userAddress.setPhoneNumber(phoneNumber); // SĐT người nhận mặc định là SĐT user

        addressRepository.save(userAddress); // Lưu bảng Address

        return "redirect:/account/edit-profile?success";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "account/forgot-password";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "account/change-password";
    }
}