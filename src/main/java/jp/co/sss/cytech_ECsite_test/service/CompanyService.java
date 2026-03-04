package jp.co.sss.cytech_ECsite_test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.cytech_ECsite_test.entity.Company;
import jp.co.sss.cytech_ECsite_test.entity.CompanyReview;
import jp.co.sss.cytech_ECsite_test.repository.CompanyRepository;
import jp.co.sss.cytech_ECsite_test.repository.CompanyReviewRepository;

@Service
public class CompanyService {

    @Autowired private CompanyRepository companyRepository;
    @Autowired private CompanyReviewRepository companyReviewRepository;

    public Company findById(Integer id) {
        return companyRepository.findById(id).orElse(null);
    }

    public List<CompanyReview> findReviewsByCompanyId(Integer companyId) {
        return companyReviewRepository.findByCompanyId(companyId);
    }

    public void saveReview(CompanyReview review) {
        companyReviewRepository.save(review);
    }

    public Double getAverageRating(Integer companyId) {
        Double avg = companyReviewRepository.findAverageRatingByCompanyId(companyId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : null;
    }

    public Integer getReviewCount(Integer companyId) {
        return companyReviewRepository.countByCompanyId(companyId);
    }
}