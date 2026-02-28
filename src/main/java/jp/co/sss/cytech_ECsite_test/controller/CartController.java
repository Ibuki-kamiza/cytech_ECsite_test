package jp.co.sss.cytech_ECsite_test.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ECsite_test.entity.Cart;
import jp.co.sss.cytech_ECsite_test.service.CartService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/add")
    public String addCart(@RequestParam Integer productId,
                          @RequestParam Integer quantity,
                          Principal principal) {
        Cart cart = new Cart();
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setUserId(userService.findByEmail(principal.getName()).getUserId());
        cartService.addCart(cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cartDetail(Principal principal, Model model) {
        Integer userId = userService.findByEmail(principal.getName()).getUserId();
        List<Cart> carts = cartService.findByUserId(userId);
        model.addAttribute("carts", carts);
        model.addAttribute("products", productService.findAll());
        return "cart/detail";
    }

    @PostMapping("/cart/delete")
    public String deleteCart(@RequestParam Integer cartId) {
        cartService.deleteCart(cartId);
        return "redirect:/cart";
    }
    @GetMapping("/cart/add/{productId}")
    public String cartAddView(@PathVariable Integer productId,
                              @RequestParam Integer quantity,
                              Principal principal, Model model) {
        var product = productService.findById(productId);
        Integer userId = userService.findByEmail(principal.getName()).getUserId();
        
        // カートに保存
        Cart cart = new Cart();
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setUserId(userId);
        cartService.addCart(cart);
        
        // カート内合計計算
        List<Cart> carts = cartService.findByUserId(userId);
        int total = carts.stream()
            .mapToInt(c -> productService.findById(c.getProductId()).getPrice() * c.getQuantity())
            .sum();
        
        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);
        model.addAttribute("total", total);
        return "cart/add";
    }
}