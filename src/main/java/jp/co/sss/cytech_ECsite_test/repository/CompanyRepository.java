package jp.co.sss.cytech_ECsite_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.cytech_ECsite_test.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}