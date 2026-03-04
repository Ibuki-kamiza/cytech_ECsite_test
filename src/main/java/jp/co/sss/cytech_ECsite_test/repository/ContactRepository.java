package jp.co.sss.cytech_ECsite_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.cytech_ECsite_test.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
