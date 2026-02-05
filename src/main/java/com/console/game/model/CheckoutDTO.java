package com.console.game.model;

import com.console.game.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CheckoutDTO {

    @NotBlank(message = "Vui lòng nhập họ và tên")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(
        regexp = "^(0[0-9]{9})$",
        message = "Số điện thoại không hợp lệ"
    )
    private String phoneNumber;

    @NotBlank(message = "Vui lòng nhập địa chỉ nhận hàng")
    private String address;

    private String note;

    @NotNull(message = "Vui lòng chọn phương thức thanh toán")
    private PaymentMethod paymentMethod;
}
