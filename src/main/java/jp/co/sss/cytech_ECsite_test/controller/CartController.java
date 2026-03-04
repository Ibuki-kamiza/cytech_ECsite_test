package jp.co.sss.cytech_ECsite_test.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.cytech_ECsite_test.entity.Cart;
import jp.co.sss.cytech_ECsite_test.entity.Product;
import jp.co.sss.cytech_ECsite_test.service.CartService;
import jp.co.sss.cytech_ECsite_test.service.CategoryService;
import jp.co.sss.cytech_ECsite_test.service.ProductService;
import jp.co.sss.cytech_ECsite_test.service.UserService;

@Controller
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private ProductService productService;
    @Autowired private UserService userService;
    @Autowired private CategoryService categoryService;

    @PostMapping("/cart/add/{productId}")
    public String cartAddView(@PathVariable Integer productId,
                              @RequestParam Integer quantity,
                              Principal principal, Model model) {
        var product = productService.findById(productId);
        Integer userId = userService.findByEmail(principal.getName()).getUserId();

        Cart cart = new Cart();
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setUserId(userId);
        cartService.addCart(cart);

        List<Cart> carts = cartService.findByUserId(userId);
        int total = carts.stream()
            .mapToInt(c -> productService.findById(c.getProductId()).getPrice() * c.getQuantity())
            .sum();
        int totalTax = carts.stream()
            .mapToInt(c -> productService.findById(c.getProductId()).getTaxPrice() * c.getQuantity())
            .sum();

        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);
        model.addAttribute("total", total);
        model.addAttribute("totalTax", totalTax);
        model.addAttribute("cartCount", carts.size());
        model.addAttribute("categories", categoryService.findAll());
        return "cart/add";
    }

    @GetMapping("/cart")
    public String cartDetail(Principal principal, Model model) {
        Integer userId = userService.findByEmail(principal.getName()).getUserId();
        List<Cart> carts = cartService.findByUserId(userId);

        // cartId -> Product のMapを作成
        Map<Integer, Product> productMap = new LinkedHashMap<>();
        int total = 0, totalTax = 0;
        for (Cart cart : carts) {
            Product p = productService.findById(cart.getProductId());
            productMap.put(cart.getCartId(), p);
            total += p.getPrice() * cart.getQuantity();
            totalTax += p.getTaxPrice() * cart.getQuantity();
        }

        model.addAttribute("carts", carts);
        model.addAttribute("productMap", productMap);
        model.addAttribute("total", total);
        model.addAttribute("totalTax", totalTax);
        model.addAttribute("categories", categoryService.findAll());
        return "cart/detail";
    }

    @PostMapping("/cart/delete")
    public String deleteCart(@RequestParam Integer cartId) {
        cartService.deleteCart(cartId);
        return "redirect:/cart";
    }
}
