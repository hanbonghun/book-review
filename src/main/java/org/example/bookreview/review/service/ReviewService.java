package org.example.bookreview.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.domain.Review;
import org.example.bookreview.review.dto.CreateReviewRequest;
import org.example.bookreview.member.repository.MemberRepository;
import org.example.bookreview.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public Long createReview(Long memberId, CreateReviewRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Review review = Review.builder()
            .isbn(request.getIsbn())
            .bookTitle(request.getBookTitle())
            .bookAuthors(request.getBookAuthors())
            .publisher(request.getPublisher())
            .member(member)
            .rating(request.getRating())
            .content(request.getContent())
            .readingStatus(request.getReadingStatus())
            .build();

        return reviewRepository.save(review).getId();
    }
}