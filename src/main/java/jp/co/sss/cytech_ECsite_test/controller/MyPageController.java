package jp.co.sss.cytech_ECsite_test.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.cytech_ECsite_test.service.OrderService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class MyPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/mypage")
    public String mypage(Principal principal, Model model) {
        var user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.findByUserId(user.getUserId()));
        return "mypage";
    }
}