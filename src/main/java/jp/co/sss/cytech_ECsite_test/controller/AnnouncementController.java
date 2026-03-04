package jp.co.sss.cytech_ECsite_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jp.co.sss.cytech_ECsite_test.entity.Announcement;
import jp.co.sss.cytech_ECsite_test.entity.Product;
import jp.co.sss.cytech_ECsite_test.repository.AnnouncementRepository;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;

@Controller
public class AnnouncementController {

    @Autowired private AnnouncementRepository announcementRepository;
    @Autowired private CategoryService categoryService;
    @Autowired private ProductService productService;

    @GetMapping("/announcements/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null) return "redirect:/top";

        Product product = null;
        if (announcement.getProductId() != null) {
            product = productService.findById(announcement.getProductId());
        }

        model.addAttribute("announcement", announcement);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "announcement/detail";
    }
}
