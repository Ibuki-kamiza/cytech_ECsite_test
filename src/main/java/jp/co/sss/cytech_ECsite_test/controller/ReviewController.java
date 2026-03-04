package jp.co.sss.cytech_ECsite_test.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sss.cytech_ECsite_test.entity.Review;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.ReviewService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class ReviewController {

    @Autowired private ReviewService reviewService;
    @Autowired private ProductService productService;
    @Autowired private UserService userService;
    @Autowired private CategoryService categoryService;

    @GetMapping("/review/{productId}")
    public String reviewForm(@PathVariable Integer productId, Model model) {
        model.addAttribute("product", productService.findById(productId));
        model.addAttribute("categories", categoryService.findAll());
        return "review/form";
    }

    @PostMapping("/review/{productId}")
    public String reviewSubmit(@PathVariable Integer productId,
                               @RequestParam Integer rating,
                               @RequestParam String comment,
                               @RequestParam(required = false) MultipartFile imgFile,
                               Principal principal, Model model) throws IOException {

        Integer userId = userService.findByEmail(principal.getName()).getUserId();

        String imgPath = null;
        if (imgFile != null && !imgFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imgFile.getOriginalFilename();
            Path uploadDir = Paths.get("src/main/resources/static/images/reviews");
            Files.createDirectories(uploadDir);
            Files.copy(imgFile.getInputStream(), uploadDir.resolve(filename));
            imgPath = "reviews/" + filename;
        }

        Review review = new Review();
        review.setProductId(productId);
        review.setUserId(userId);
        review.setRating(rating);
        review.setComment(comment);
        review.setImgPath(imgPath);
        review.setCreatedAt(LocalDateTime.now());
        reviewService.save(review);

        return "redirect:/products/" + productId;
    }
}
