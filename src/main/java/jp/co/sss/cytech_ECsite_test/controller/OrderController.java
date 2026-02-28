package jp.co.sss.cytech_ECsite_test.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.cytech_ECsite_test.entity.Order;
import jp.co.sss.cytech_ECsite_test.service.OrderService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/order")
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order/form";
    }

    @PostMapping("/order/confirm")
    public String confirm(@ModelAttribute Order order, Model model) {
        model.addAttribute("order", order);
        return "order/confirm";
    }

    @PostMapping("/order/complete")
    public String complete(@ModelAttribute Order order, Principal principal) {
        order.setUserId(userService.findByEmail(principal.getName()).getUserId());
        orderService.save(order);
        return "redirect:/order/done";
    }

    @GetMapping("/order/done")
    public String done() {
        return "order/done";
    }
}