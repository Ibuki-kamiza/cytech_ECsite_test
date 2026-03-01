package jp.co.sss.cytech_ECsite_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.cytech_ECsite_test.service.ProductService;

@Controller
public class TopController {

    @Autowired
    private ProductService productService;

    @GetMapping("/top")
    public String top(Model model) {
        model.addAttribute("products", productService.findAll());
        return "top";
    }
}