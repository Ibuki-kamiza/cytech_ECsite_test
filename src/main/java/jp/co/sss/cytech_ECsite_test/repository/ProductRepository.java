package jp.co.sss.cytech_ECsite_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sss.cytech_ECsite_test.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductNameContaining(String keyword);
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findByCompanyId(Integer companyId);

    // 会社名・商品名・カテゴリー複合曖昧検索
    @Query("SELECT p FROM Product p LEFT JOIN Company c ON p.companyId = c.companyId " +
           "LEFT JOIN Category cat ON p.categoryId = cat.categoryId " +
           "WHERE p.productName LIKE %:keyword% " +
           "OR c.companyName LIKE %:keyword% " +
           "OR cat.categoryName LIKE %:keyword%")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
}
