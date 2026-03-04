package jp.co.sss.cytech_ECsite_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.cytech_ECsite_test.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(Integer userId);

    @Transactional
    void deleteByCartId(Integer cartId);
}