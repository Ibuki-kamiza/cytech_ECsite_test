package jp.co.sss.cytech_ECsite_test.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.co.sss.cytech_ECsite_test.entity.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    // TOP公開中または期間限定
    @Query("SELECT a FROM Announcement a WHERE a.type = :type AND a.isActive = 1 " +
           "AND (a.startDate IS NULL OR a.startDate <= :now) " +
           "AND (a.endDate IS NULL OR a.endDate >= :now) " +
           "ORDER BY a.createdAt DESC")
    List<Announcement> findVisibleByType(@Param("type") String type, @Param("now") LocalDateTime now);

    List<Announcement> findAllByOrderByCreatedAtDesc();
}
