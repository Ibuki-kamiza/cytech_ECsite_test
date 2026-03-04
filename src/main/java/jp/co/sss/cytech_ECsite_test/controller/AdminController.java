package jp.co.sss.cytech_ECsite_test.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sss.cytech_ECsite_test.entity.Announcement;
import jp.co.sss.cytech_ECsite_test.entity.Contact;
import jp.co.sss.cytech_ECsite_test.repository.AnnouncementRepository;
import jp.co.sss.cytech_ECsite_test.repository.ContactRepository;
import jp.co.sss.cytech_ECsite_test.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AnnouncementRepository announcementRepository;
    @Autowired private ContactRepository contactRepository;
    @Autowired private ProductService productService;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("announcementCount", announcementRepository.count());
        model.addAttribute("contactCount", contactRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/announcements")
    public String announcements(Model model) {
        model.addAttribute("announcements", announcementRepository.findAllByOrderByCreatedAtDesc());
        return "admin/announcements";
    }

    @GetMapping("/announcements/new")
    public String newAnnouncement(Model model) {
        model.addAttribute("announcement", new Announcement());
        model.addAttribute("products", productService.findAll());
        return "admin/announcement_form";
    }

    @GetMapping("/announcements/{id}/edit")
    public String editAnnouncement(@PathVariable Integer id, Model model) {
        Announcement a = announcementRepository.findById(id).orElse(null);
        if (a == null) return "redirect:/admin/announcements";
        model.addAttribute("announcement", a);
        model.addAttribute("products", productService.findAll());
        return "admin/announcement_form";
    }

    @PostMapping("/announcements/save")
    public String saveAnnouncement(
            @RequestParam(required = false) Integer id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String type,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) String useSchedule,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) MultipartFile imgFile) throws IOException {

        Announcement a = id != null
            ? announcementRepository.findById(id).orElse(new Announcement())
            : new Announcement();

        a.setTitle(title);
        a.setContent(content);
        a.setType(type);
        a.setIsActive(isActive != null ? 1 : 0);
        a.setProductId(productId);

        // 期間設定
        if (useSchedule != null) {
            if (startDate != null && !startDate.isEmpty())
                a.setStartDate(LocalDateTime.parse(startDate, FMT));
            if (endDate != null && !endDate.isEmpty())
                a.setEndDate(LocalDateTime.parse(endDate, FMT));
        } else {
            a.setStartDate(null);
            a.setEndDate(null);
        }

        // 画像アップロード
        if (imgFile != null && !imgFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imgFile.getOriginalFilename();
            Path uploadDir = Paths.get("src/main/resources/static/images/announcements");
            Files.createDirectories(uploadDir);
            Files.copy(imgFile.getInputStream(), uploadDir.resolve(filename));
            a.setImgPath("announcements/" + filename);
        }

        if (a.getCreatedAt() == null) a.setCreatedAt(LocalDateTime.now());
        announcementRepository.save(a);
        return "redirect:/admin/announcements";
    }

    @PostMapping("/announcements/{id}/delete")
    public String deleteAnnouncement(@PathVariable Integer id) {
        announcementRepository.deleteById(id);
        return "redirect:/admin/announcements";
    }

    @GetMapping("/contacts")
    public String contacts(Model model) {
        List<Contact> contacts = contactRepository.findAll();
        model.addAttribute("contacts", contacts);
        return "admin/contacts";
    }
}
