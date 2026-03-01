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
}