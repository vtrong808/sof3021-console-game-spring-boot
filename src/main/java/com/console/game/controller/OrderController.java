package com.console.game.controller;

import com.console.game.enums.PaymentMethod;
import com.console.game.model.CartItem;
import com.console.game.model.CheckoutDTO;
import com.console.game.model.Order;
import com.console.game.model.User;
import com.console.game.repository.OrderRepository;
import com.console.game.service.CartService;
import com.console.game.service.OrderService;
import com.console.game.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    // 1. THÊM CÁI NÀY: Để lưu và lấy đơn hàng từ Database
    @Autowired
    private OrderRepository orderRepository;

    // Xem trước khi xuất hóa đơn
    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal, HttpSession session) {

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<Integer> ids = (List<Integer>) session.getAttribute("CHECKOUT_ITEM_IDS");

        if (ids == null || ids.isEmpty()) {
            return "redirect:/cart/view";
        }

        List<CartItem> items = cartService.getCartItemsByIds(ids, user);

        model.addAttribute("cartItems", items);
        model.addAttribute("totalAmount", cartService.getTotalAmount(items));
        model.addAttribute("checkoutDTO", new CheckoutDTO());

        return "order/check-out";
    }

    // Xuất thông tin người dùng và phương thức trước khi xuất hóa đơn
    @PostMapping("/checkout")
    public String backToCheckout(@ModelAttribute CheckoutDTO checkoutDTO,
            Principal principal,
            Model model) {

        if (principal == null)
            return "redirect:/auth/login";

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<CartItem> cartItems = cartService.getCartItems(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", cartService.getTotalAmount(user));
        model.addAttribute("checkoutDTO", checkoutDTO);

        return "order/check-out";
    }

    @PostMapping("/checkout-selected")
    public String checkoutSelected(
            @RequestParam("selectedItemIds") List<Integer> cartItemIds,
            Principal principal,
            Model model,
            HttpSession session) {

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<CartItem> selectedItems = cartService.getCartItemsByIds(cartItemIds, user);

        if (selectedItems.isEmpty()) {
            return "redirect:/cart/view?error=empty";
        }

        session.setAttribute("CHECKOUT_ITEM_IDS", cartItemIds);

        model.addAttribute("cartItems", selectedItems);
        model.addAttribute("totalAmount", cartService.getTotalAmount(selectedItems));
        model.addAttribute("checkoutDTO", new CheckoutDTO());

        return "order/check-out";
    }

    // Xem trước hóa đơn
    @PostMapping("/preview")
    public String previewInvoice(
            Principal principal,
            @Valid @ModelAttribute("checkoutDTO") CheckoutDTO checkoutDTO,
            BindingResult result,
            Model model,
            HttpSession session) {

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<Integer> ids = (List<Integer>) session.getAttribute("CHECKOUT_ITEM_IDS");

        List<CartItem> items = cartService.getCartItemsByIds(ids, user);

        // ❌ Nếu có lỗi → quay lại trang checkout
        if (result.hasErrors()) {
            model.addAttribute("cartItems", items);
            model.addAttribute("totalAmount", cartService.getTotalAmount(items));
            return "order/check-out";
        }

        // ✅ Hợp lệ → sang preview
        model.addAttribute("cartItems", items);
        model.addAttribute("totalAmount", cartService.getTotalAmount(items));
        model.addAttribute("checkout", checkoutDTO);

        return "order/invoice-preview";
    }

    // Xác nhận và tạo hóa đơn
    @PostMapping("/confirm")
    public String confirmOrder(
            Principal principal,
            @ModelAttribute CheckoutDTO checkoutDTO,
            HttpSession session) {

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<Integer> ids = (List<Integer>) session.getAttribute("CHECKOUT_ITEM_IDS");
        if (ids == null || ids.isEmpty()) {
            return "redirect:/cart/view";
        }
        List<CartItem> items = cartService.getCartItemsByIds(ids, user);

        // Tạo đơn hàng
        Order order = orderService.placeOrderWithItems(user, items, checkoutDTO);

        // xoá sp khỏi giỏ hàng sau khi đặt hàng thành công
        if (ids != null) {
            for (Integer id : ids) {
                cartService.removeFromCart(id);
                System.out.println("Đã xóa CartItem ID: " + id); // In ra log để kiểm tra
            }
        }
        // -----------------------------------------------------------------------------------

        session.removeAttribute("CHECKOUT_ITEM_IDS");
        return "redirect:/order/list?success";
    }

    // Xem lịch sử đặt hàng
    @GetMapping("/list")
    public String list(@RequestParam(value = "status", required = false) String status,
            Model model, Principal principal) {
        if (principal == null)
            return "redirect:/auth/login";

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<Order> orders;

        // Kiểm tra nếu người dùng có chọn lọc theo trạng thái
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            // Bạn cần đảm bảo OrderRepository có hàm
            // findByUserAndStatusOrderByOrderDateDesc
            orders = orderRepository.findByUserAndStatusOrderByOrderDateDesc(user,
                    com.console.game.enums.OrderStatus.valueOf(status));
        } else {
            orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("currentStatus", status != null ? status : "ALL"); // Để giữ trạng thái active trên nút
        return "order/order-list";
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Integer orderId,
            Model model,
            Principal principal) {
        if (principal == null)
            return "redirect:/auth/login";
        Order order = orderRepository.findDetailById(orderId).orElse(null);
        User currentUser = userService.findByEmail(principal.getName()).orElseThrow();

        // Bảo mật: chỉ xem đơn của chính mình
        if (order == null || !order.getUser().getUserId().equals(currentUser.getUserId())) {
            return "redirect:/order/list";
        }
        model.addAttribute("order", order);
        return "order/order-detail";
    }

    @GetMapping("/my-wishlist")
    public String wishlist() {
        return "order/my-wishlist";
    }

    // Thêm phương thức để xử lý yêu cầu hủy đơn từ giao diện
    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam("id") Integer orderId, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName()).orElseThrow();

        try {
            // Gọi service để thực hiện logic hủy đơn
            orderService.cancelOrder(orderId, user);
            // Hủy thành công, quay về danh sách với thông báo
            return "redirect:/order/list?cancelled";
        } catch (Exception e) {
            // Nếu có lỗi (ví dụ: đơn đã giao không thể hủy), quay về với thông báo lỗi
            return "redirect:/order/list?error=" + e.getMessage();
        }
    }
}