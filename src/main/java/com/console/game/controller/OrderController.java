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
            @ModelAttribute CheckoutDTO checkoutDTO,
            Model model,
            HttpSession session) {

        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<Integer> ids = (List<Integer>) session.getAttribute("CHECKOUT_ITEM_IDS");

        List<CartItem> items = cartService.getCartItemsByIds(ids, user);

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

        Order order = orderService.placeOrderWithItems(user, items, checkoutDTO);

        session.removeAttribute("CHECKOUT_ITEM_IDS");

        return "redirect:/order/list?success";
    }

    // Xem lịch sử đặt hàng
    @GetMapping("/list")
    public String list(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/auth/login";

        User user = userService.findByEmail(principal.getName()).orElseThrow();

        // Lấy danh sách đơn hàng của user đó (Sắp xếp mới nhất lên đầu)
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        model.addAttribute("orders", orders);

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

}