package jp.co.sss.cytech_ECsite_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ECsite_test.entity.Review;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.ReviewService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/products")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer categoryId,
                       Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("products", productService.searchByName(keyword));
        } else if (categoryId != null) {
            model.addAttribute("products", productService.findByCategory(categoryId));
        } else {
            model.addAttribute("products", productService.findAll());
        }
        return "product/list";
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("reviews", reviewService.findByProductId(id));
        model.addAttribute("review", new Review());
        return "product/detail";
    }
}