package jp.co.sss.cytech_ECsite_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.cytech_ECsite_test.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}