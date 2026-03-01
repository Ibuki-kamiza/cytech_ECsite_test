package jp.co.sss.cytech_ECsite_test.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.cytech_ECsite_test.entity.Review;
import jp.co.sss.cytech_ECsite_test.service.ReviewService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("/review/{productId}")
    public String reviewForm(@PathVariable Integer productId, Model model) {
        model.addAttribute("productId", productId);
        model.addAttribute("review", new Review());
        return "review/form";
    }

    @PostMapping("/review/{productId}")
    public String postReview(@PathVariable Integer productId,
                             @ModelAttribute Review review,
                             Principal principal) {
        review.setProductId(productId);
        review.setUserId(userService.findByEmail(principal.getName()).getUserId());
        reviewService.save(review);
        return "redirect:/products/" + productId;
    }
}