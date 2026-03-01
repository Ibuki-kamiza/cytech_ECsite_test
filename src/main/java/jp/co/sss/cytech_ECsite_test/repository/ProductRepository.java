package jp.co.sss.cytech_ECsite_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.cytech_ECsite_test.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductNameContaining(String keyword);
    List<Product> findByCategoryId(Integer categoryId);
}