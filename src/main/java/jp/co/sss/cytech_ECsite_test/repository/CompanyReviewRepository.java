package jp.co.sss.cytech_ECsite_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sss.cytech_ECsite_test.entity.CompanyReview;

public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Integer> {
    List<CompanyReview> findByCompanyId(Integer companyId);

    @Query("SELECT AVG(r.rating) FROM CompanyReview r WHERE r.companyId = :companyId")
    Double findAverageRatingByCompanyId(@Param("companyId") Integer companyId);

    @Query("SELECT COUNT(r) FROM CompanyReview r WHERE r.companyId = :companyId")
    Integer countByCompanyId(@Param("companyId") Integer companyId);
}