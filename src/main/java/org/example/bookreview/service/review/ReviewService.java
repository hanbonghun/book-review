package org.example.bookreview.service.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.domain.Member;
import org.example.bookreview.domain.Review;
import org.example.bookreview.dto.CreateReviewRequest;
import org.example.bookreview.repository.MemberRepository;
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
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));

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