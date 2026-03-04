package jp.co.sss.cytech_ECsite_test.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ECsite_test.entity.Contact;
import jp.co.sss.cytech_ECsite_test.repository.ContactRepository;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;

@Controller
public class ContactController {

    @Autowired private ContactRepository contactRepository;
    @Autowired private CategoryService categoryService;

    @GetMapping("/contact")
    public String form(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "contact/form";
    }

    @PostMapping("/contact")
    public String submit(@RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String message,
                         Model model) {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setEmail(email);
        contact.setMessage(message);
        contact.setCreatedAt(LocalDateTime.now());
        contactRepository.save(contact);
        model.addAttribute("categories", categoryService.findAll());
        return "contact/done";
    }
}