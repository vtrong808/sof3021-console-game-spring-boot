package com.console.game.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String password; // Lưu ý: Sẽ lưu trực tiếp, không mã hóa
    private String phoneNumber;
    private String role;     // String để map sang Enum Role
    private Boolean isActive;
}