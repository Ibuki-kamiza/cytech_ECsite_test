package jp.co.sss.cytech_ECsite_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.cytech_ECsite_test.entity.Product;
import jp.co.sss.cytech_ECsite_test.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> searchByName(String keyword) {
        return productRepository.findByProductNameContaining(keyword);
    }

    public List<Product> findByCategory(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}