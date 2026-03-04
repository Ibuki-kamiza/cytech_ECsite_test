package jp.co.sss.cytech_ECsite_test.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sss.cytech_ECsite_test.entity.Company;
import jp.co.sss.cytech_ECsite_test.entity.CompanyReview;
import jp.co.sss.cytech_ECsite_test.entity.Product;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;
import jp.co.sss.cytech_ECsite_test.service.CompanyService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class CompanyController {

    @Autowired private CompanyService companyService;
    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private UserService userService;

    @GetMapping("/companies/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Company company = companyService.findById(id);
        if (company == null) return "redirect:/products";

        List<Product> products = productService.findByCompanyId(id);
        List<CompanyReview> reviews = companyService.findReviewsByCompanyId(id);

        Map<Integer, String> reviewUserNames = new HashMap<>();
        for (CompanyReview r : reviews) {
            try {
                String name = userService.findById(r.getUserId()).getUserName();
                reviewUserNames.put(r.getReviewId(), name);
            } catch (Exception e) {
                reviewUserNames.put(r.getReviewId(), "不明");
            }
        }

        model.addAttribute("company", company);
        model.addAttribute("products", products);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewUserNames", reviewUserNames);
        model.addAttribute("avgRating", companyService.getAverageRating(id));
        model.addAttribute("reviewCount", companyService.getReviewCount(id));
        model.addAttribute("categories", categoryService.findAll());
        return "company/detail";
    }

    @PostMapping("/companies/{id}/review")
    public String submitReview(@PathVariable Integer id,
                               @RequestParam Integer rating,
                               @RequestParam String comment,
                               @RequestParam(required = false) MultipartFile imgFile,
                               Principal principal) throws IOException {

        Integer userId = userService.findByEmail(principal.getName()).getUserId();

        String imgPath = null;
        if (imgFile != null && !imgFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imgFile.getOriginalFilename();
            Path uploadDir = Paths.get("src/main/resources/static/images/company_reviews");
            Files.createDirectories(uploadDir);
            Files.copy(imgFile.getInputStream(), uploadDir.resolve(filename));
            imgPath = "company_reviews/" + filename;
        }

        CompanyReview review = new CompanyReview();
        review.setCompanyId(id);
        review.setUserId(userId);
        review.setRating(rating);
        review.setComment(comment);
        review.setImgPath(imgPath);
        review.setCreatedAt(LocalDateTime.now());
        companyService.saveReview(review);

        return "redirect:/companies/" + id;
    }
}