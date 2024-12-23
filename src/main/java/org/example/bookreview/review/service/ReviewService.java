package org.example.bookreview.review.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.book.service.RetryableBookService;
import org.example.bookreview.review.domain.Review;
import org.example.bookreview.review.dto.CreateReviewRequest;
import org.example.bookreview.review.dto.ReviewPaginationRequest;
import org.example.bookreview.review.dto.ReviewPaginationResponse;
import org.example.bookreview.review.dto.ReviewResponse;
import org.example.bookreview.review.dto.ReviewSearchCondition;
import org.example.bookreview.review.repository.ReviewRepository;
import org.example.bookreview.review.repository.ReviewLikeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final RetryableBookService retryableBookService;
    private final TransactionalReviewService transactionalReviewService;

    public Long createReview(String isbn, Long memberId, CreateReviewRequest request) {
        Book book = retryableBookService.getOrCreateBookWithRetry(isbn);
        return transactionalReviewService.saveReview(memberId, book, request);
    }

    public ReviewPaginationResponse getReviews(ReviewSearchCondition condition) {
        int size = condition.getSize();
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Review> reviews = reviewRepository.findByCondition(condition, pageable);

        boolean hasNext = reviews.size() > size;
        if (hasNext) {
            reviews = reviews.subList(0, size);
        }

        List<ReviewResponse> reviewResponses = reviews.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());

        String nextCursor = (hasNext && !reviews.isEmpty())
            ? String.valueOf(reviews.get(reviews.size() - 1).getCreatedAt())
            : null;

        return new ReviewPaginationResponse(reviewResponses, nextCursor, hasNext);
    }

    public ReviewPaginationResponse getRecentReviews(ReviewPaginationRequest request) {
        return getReviews(ReviewSearchCondition.ofRecent(request));
    }

    public ReviewPaginationResponse getBookReviews(String isbn, ReviewPaginationRequest request,
        String sort, Long currentUserId) {

        ReviewPaginationResponse response = getReviews(ReviewSearchCondition.ofBook(isbn, request, sort));
        Set<Long> reviewIds = response.getReviews().stream().map(ReviewResponse::getId).collect(
            Collectors.toSet());
        Set<Long> likedReviewIds = reviewLikeRepository.findLikedReviewIdsByMemberIdAndReviewIds(currentUserId, reviewIds);
        response.getReviews().forEach(review -> review.setLikedByCurrentUser(likedReviewIds.contains(review.getId())));
        return response;
    }

    public ReviewPaginationResponse getMyReviews(Long memberId, ReviewPaginationRequest request) {
        return getReviews(ReviewSearchCondition.ofMember(memberId, request));
    }

    private ReviewResponse convertToResponse(Review review) {
        return convertToResponse(review, false);
    }

    private ReviewResponse convertToResponse(Review review, boolean likedByCurrentUser) {

        return new ReviewResponse(
            review.getId(),
            review.getBook().getIsbn(),
            review.getBook().getTitle(),
            review.getBook().getAuthor(),
            review.getBook().getPublisher(),
            review.getContent(),
            review.getRating(),
            review.getReadingStatus(),
            review.getMember().getName(),
            review.getCreatedAt(),
            review.getLikeCount(),
            likedByCurrentUser
        );
    }
}