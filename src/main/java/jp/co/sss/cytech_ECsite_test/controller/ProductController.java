package jp.co.sss.cytech_ECsite_test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ECsite_test.entity.Product;
import jp.co.sss.cytech_ECsite_test.entity.Review;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;
import jp.co.sss.cytech_ECsite_test.service.CompanyService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.ReviewService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private ReviewService reviewService;
    @Autowired private CategoryService categoryService;
    @Autowired private UserService userService;
    @Autowired private CompanyService companyService;

    @GetMapping("/products")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer categoryId,
                       Model model) {
        List<Product> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchByKeyword(keyword);
        } else if (categoryId != null) {
            products = productService.findByCategory(categoryId);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            products = productService.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());

        Map<Integer, Double> avgRatings = new HashMap<>();
        Map<Integer, Integer> reviewCounts = new HashMap<>();
        for (Product p : products) {
            avgRatings.put(p.getProductId(), reviewService.getAverageRating(p.getProductId()));
            reviewCounts.put(p.getProductId(), reviewService.getReviewCount(p.getProductId()));
        }
        model.addAttribute("avgRatings", avgRatings);
        model.addAttribute("reviewCounts", reviewCounts);

        return "product/list";
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        List<Review> reviews = reviewService.findByProductId(id);

        Map<Integer, String> reviewUserNames = new HashMap<>();
        for (Review r : reviews) {
            try {
                String name = userService.findById(r.getUserId()).getUserName();
                reviewUserNames.put(r.getReviewId(), name);
            } catch (Exception e) {
                reviewUserNames.put(r.getReviewId(), "不明");
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewUserNames", reviewUserNames);
        model.addAttribute("review", new Review());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("avgRating", reviewService.getAverageRating(id));
        model.addAttribute("reviewCount", reviewService.getReviewCount(id));
        if (product.getCategoryId() != null) {
            model.addAttribute("category", categoryService.findById(product.getCategoryId()));
        }
        if (product.getCompanyId() != null) {
            model.addAttribute("company", companyService.findById(product.getCompanyId()));
        }
        return "product/detail";
    }
}
