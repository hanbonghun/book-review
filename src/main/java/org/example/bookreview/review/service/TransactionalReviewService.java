package org.example.bookreview.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.member.repository.MemberRepository;
import org.example.bookreview.review.repository.ReviewRepository;
import org.example.bookreview.review.domain.Review;
import org.example.bookreview.review.dto.CreateReviewRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionalReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveReview(Long memberId, Book book, CreateReviewRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Review review = Review.builder()
            .book(book)
            .member(member)
            .rating(request.getRating())
            .content(request.getContent())
            .readingStatus(request.getReadingStatus())
            .build();

        return reviewRepository.save(review).getId();
    }
}