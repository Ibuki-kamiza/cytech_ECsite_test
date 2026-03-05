package jp.co.sss.cytech_ECsite_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ECsite_test.entity.User;
import jp.co.sss.cytech_ECsite_test.repository.UserRepository;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthenticationManager authenticationManager;

    // 管理者ログインフォーム
    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "メールアドレスまたはパスワードが正しくありません");
        return "admin/login";
    }

    // 管理者ログイン処理
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            // ADMIN権限チェック
            boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                model.addAttribute("error", "管理者権限がありません");
                return "admin/login";
            }
            SecurityContextHolder.getContext().setAuthentication(auth);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", "メールアドレスまたはパスワードが正しくありません");
            return "admin/login";
        }
    }

    // 管理者新規登録フォーム
    @GetMapping("/register")
    public String registerForm() {
        return "admin/register";
    }

    // 管理者新規登録処理
    @PostMapping("/register")
    public String register(@RequestParam String userName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String secretKey,
                           Model model) {
        // 管理者登録用秘密キー
        if (!"CYTECH_ADMIN_2026".equals(secretKey)) {
            model.addAttribute("error", "秘密キーが正しくありません");
            return "admin/register";
        }
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("error", "このメールアドレスは既に登録されています");
            return "admin/register";
        }

        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPasswords(password);
        user.setRole("ADMIN");
        user.setUserAddress("-");
        userService.register(user);
        return "redirect:/admin/login?registered";
    }
}
