package jp.co.sss.cytech_ECsite_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sss.cytech_ECsite_test.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(Integer productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.productId = :productId")
    Integer countByProductId(@Param("productId") Integer productId);

    @Query("SELECT r.productId, AVG(r.rating) as avg FROM Review r GROUP BY r.productId HAVING COUNT(r) >= 1 ORDER BY avg DESC")
    List<Object[]> findTopRatedProductIds();
}
