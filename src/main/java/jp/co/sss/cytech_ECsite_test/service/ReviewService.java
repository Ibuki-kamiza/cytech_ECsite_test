package jp.co.sss.cytech_ECsite_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.cytech_ECsite_test.entity.Review;
import jp.co.sss.cytech_ECsite_test.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> findByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public Double getAverageRating(Integer productId) {
        Double avg = reviewRepository.findAverageRatingByProductId(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : null;
    }

    public Integer getReviewCount(Integer productId) {
        return reviewRepository.countByProductId(productId);
    }
}