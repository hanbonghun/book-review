package org.example.bookreview.review.repository;

import java.util.List;
import java.util.Set;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.review.domain.Review;
import org.example.bookreview.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    boolean existsByReviewAndMember(Review review, Member member);
    void deleteByReviewAndMember(Review review, Member member);

    /**
     * 특정 리뷰 ID 목록에 대해 사용자가 좋아요를 누른 리뷰 ID 조회
     */
    @Query("SELECT distinct rl.review.id FROM ReviewLike rl " +
        "WHERE rl.member.id = :memberId AND rl.review.id IN :reviewIds")
    Set<Long> findLikedReviewIdsByMemberIdAndReviewIds(@Param("memberId") Long memberId, @Param("reviewIds") Set<Long> reviewIds);
}
