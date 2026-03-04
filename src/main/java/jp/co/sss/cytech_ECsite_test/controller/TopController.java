package jp.co.sss.cytech_ECsite_test.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ECsite_test.entity.Announcement;
import jp.co.sss.cytech_ECsite_test.entity.Product;
import jp.co.sss.cytech_ECsite_test.repository.AnnouncementRepository;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.ReviewService;

@Controller
public class TopController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private ReviewService reviewService;
    @Autowired private AnnouncementRepository announcementRepository;

    @GetMapping("/top")
    public String top(Model model) {
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = productService.findAll();

        Map<Integer, Double> avgRatings = new HashMap<>();
        Map<Integer, Integer> reviewCounts = new HashMap<>();
        for (Product p : products) {
            avgRatings.put(p.getProductId(), reviewService.getAverageRating(p.getProductId()));
            reviewCounts.put(p.getProductId(), reviewService.getReviewCount(p.getProductId()));
        }

        List<Announcement> sales = announcementRepository.findVisibleByType("sale", now);
        List<Announcement> news = announcementRepository.findVisibleByType("news", now);

        // セール商品
        Map<Integer, Product> saleProducts = new HashMap<>();
        for (Announcement sale : sales) {
            if (sale.getProductId() != null) {
                Product p = productService.findById(sale.getProductId());
                if (p != null) saleProducts.put(sale.getId(), p);
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("avgRatings", avgRatings);
        model.addAttribute("reviewCounts", reviewCounts);
        model.addAttribute("sales", sales);
        model.addAttribute("news", news);
        model.addAttribute("saleProducts", saleProducts);
        return "top";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
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

        Map<Integer, Double> avgRatings = new HashMap<>();
        Map<Integer, Integer> reviewCounts = new HashMap<>();
        for (Product p : products) {
            avgRatings.put(p.getProductId(), reviewService.getAverageRating(p.getProductId()));
            reviewCounts.put(p.getProductId(), reviewService.getReviewCount(p.getProductId()));
        }

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("avgRatings", avgRatings);
        model.addAttribute("reviewCounts", reviewCounts);
        return "product/list";
    }

    @GetMapping("/sales")
    public String salesPage(Model model) {
        LocalDateTime now = LocalDateTime.now();
        List<Announcement> sales = announcementRepository.findVisibleByType("sale", now);

        Map<Integer, Product> saleProducts = new HashMap<>();
        for (Announcement sale : sales) {
            if (sale.getProductId() != null) {
                Product p = productService.findById(sale.getProductId());
                if (p != null) saleProducts.put(sale.getId(), p);
            }
        }

        model.addAttribute("sales", sales);
        model.addAttribute("saleProducts", saleProducts);
        model.addAttribute("categories", categoryService.findAll());
        return "sale/list";
    }
}
