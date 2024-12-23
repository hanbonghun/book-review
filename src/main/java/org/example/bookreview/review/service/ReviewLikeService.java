package org.example.bookreview.review.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.member.repository.MemberRepository;
import org.example.bookreview.review.domain.Review;
import org.example.bookreview.review.domain.ReviewLike;
import org.example.bookreview.review.repository.ReviewLikeRepository;
import org.example.bookreview.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    //TODO: 동시성 처리 어떻게 할 것인지
    @Transactional
    public void addLikeOnReview(Long reviewId, Long memberId) {
        Review review = getReview(reviewId);
        Member member = getMember(memberId);
        boolean alreadyLiked = reviewLikeRepository.existsByReviewAndMember(review, member);
        if (alreadyLiked) {
            throw new BusinessException(ErrorType.ALREADY_LIKE_EXIST);
        }
        reviewLikeRepository.save(new ReviewLike(review, member));
        review.increaseLikeCount();

    }

    @Transactional
    public void removeLikeOnReview(Long reviewId, Long memberId) {
        Review review = getReview(reviewId);
        Member member = getMember(memberId);

        boolean alreadyLiked = reviewLikeRepository.existsByReviewAndMember(review, member);
        if (!alreadyLiked) {
            throw new BusinessException(ErrorType.LIKE_NOT_FOUND);
        }

        reviewLikeRepository.deleteByReviewAndMember(review, member);
        review.decreaseLikeCount();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new BusinessException(ErrorType.REIVEW_NOT_FOUND));
    }
}