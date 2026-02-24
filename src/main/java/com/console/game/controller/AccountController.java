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

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "account/forgot-password";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "account/change-password";
    }
}