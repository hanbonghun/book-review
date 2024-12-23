package org.example.bookreview.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.bookreview.review.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * 커서 기반으로 리뷰 조회 (createdAt 기준)
     */
    @Query("SELECT r FROM Review r " +
        "LEFT JOIN FETCH r.member m " +
        "LEFT JOIN FETCH r.book b " +
        "WHERE r.createdAt < :cursor " +
        "ORDER BY r.createdAt DESC")
    List<Review> findByCursor(@Param("cursor") LocalDateTime cursor, Pageable pageable);

    /**
     * 초기 조회 (cursor가 없는 요청 시)
     */
    @Query("SELECT r FROM Review r " +
        "LEFT JOIN FETCH r.member m " +
        "LEFT JOIN FETCH r.book b " +
        "ORDER BY r.createdAt DESC")
    List<Review> findInitial(Pageable pageable);
}