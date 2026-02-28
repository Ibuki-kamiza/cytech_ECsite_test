package jp.co.sss.cytech_ECsite_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.cytech_ECsite_test.entity.Cart;
import jp.co.sss.cytech_ECsite_test.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void deleteCart(Integer cartId) {
        cartRepository.deleteByCartId(cartId);
    }
}