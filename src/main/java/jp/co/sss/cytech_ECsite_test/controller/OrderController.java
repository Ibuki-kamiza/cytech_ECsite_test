package jp.co.sss.cytech_ECsite_test.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.cytech_ECsite_test.entity.Cart;
import jp.co.sss.cytech_ECsite_test.entity.Order;
import jp.co.sss.cytech_ECsite_test.service.CartService;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;
import jp.co.sss.cytech_ECsite_test.service.OrderService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private UserService userService;
    @Autowired private CartService cartService;
    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;

    @GetMapping("/order")
    public String orderForm(Principal principal, Model model) {
        Integer userId = userService.findByEmail(principal.getName()).getUserId();
        List<Cart> carts = cartService.findByUserId(userId);
        int totalTax = carts.stream()
            .mapToInt(c -> productService.findById(c.getProductId()).getTaxPrice() * c.getQuantity())
            .sum();

        Order order = new Order();
        order.setTotalPrice(totalTax);

        model.addAttribute("order", order);
        model.addAttribute("totalTax", totalTax);
        model.addAttribute("categories", categoryService.findAll());
        return "order/form";
    }

    @PostMapping("/order/confirm")
    public String confirm(@ModelAttribute Order order, Model model) {
        model.addAttribute("order", order);
        model.addAttribute("categories", categoryService.findAll());
        return "order/confirm";
    }

    @PostMapping("/order/complete")
    public String complete(@ModelAttribute Order order, Principal principal) {
        Integer userId = userService.findByEmail(principal.getName()).getUserId();
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now());
        orderService.save(order);

        // 注文完了後にカートを空にする
        List<Cart> carts = cartService.findByUserId(userId);
        for (Cart cart : carts) {
            cartService.deleteCart(cart.getCartId());
        }

        return "redirect:/order/done";
    }

    @GetMapping("/order/done")
    public String done(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "order/done";
    }
}
