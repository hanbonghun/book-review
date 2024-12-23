package org.example.bookreview.review.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.book.service.RetryableBookService;
import org.example.bookreview.repository.ReviewRepository;
import org.example.bookreview.review.domain.Review;
import org.example.bookreview.review.dto.CreateReviewRequest;
import org.example.bookreview.review.dto.ReviewPaginationRequest;
import org.example.bookreview.review.dto.ReviewPaginationResponse;
import org.example.bookreview.review.dto.ReviewResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RetryableBookService retryableBookService;
    private final TransactionalReviewService transactionalReviewService;

    public Long createReview(Long memberId, CreateReviewRequest request) {
        Book book = retryableBookService.getOrCreateBookWithRetry(request.getIsbn());
        return transactionalReviewService.saveReview(memberId, book, request);
    }

    public ReviewPaginationResponse getReviews(ReviewPaginationRequest request) {
        int size = request.getSize();
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Review> reviews;

        String cursor = request.getCursor();
        if (cursor != null && !cursor.isBlank()) {
            LocalDateTime cursorTime = LocalDateTime.parse(cursor);
            reviews = reviewRepository.findByCursor(cursorTime, pageable);
        } else {
            reviews = reviewRepository.findInitial(pageable);
        }
        boolean hasNext = reviews.size() > size;
        if (hasNext) {
            reviews = reviews.subList(0, size);
        }

        List<ReviewResponse> reviewResponses = reviews.stream()
            .map(review -> new ReviewResponse(
                review.getId(),
                review.getBook().getIsbn(),
                review.getBook().getTitle(),
                review.getBook().getAuthor(),
                review.getBook().getPublisher(),
                review.getContent(),
                review.getRating(),
                review.getReadingStatus(),
                review.getMember().getName(),
                review.getCreatedAt()
            ))
            .collect(Collectors.toList());

        String nextCursor = (hasNext && !reviews.isEmpty())
            ? String.valueOf(reviews.get(reviews.size() - 1).getCreatedAt())
            : null;

        return new ReviewPaginationResponse(reviewResponses, nextCursor, hasNext);
    }
}