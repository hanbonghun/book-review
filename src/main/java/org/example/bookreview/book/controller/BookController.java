package org.example.bookreview.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.dto.BookDetailResponse;
import org.example.bookreview.book.service.TransactionalBookService;
import org.example.bookreview.common.response.ApiResponse;
import org.example.bookreview.oauth.CustomOAuth2User;
import org.example.bookreview.review.dto.CreateReviewRequest;
import org.example.bookreview.review.dto.ReviewPaginationRequest;
import org.example.bookreview.review.dto.ReviewPaginationResponse;
import org.example.bookreview.review.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final TransactionalBookService transactionalBookService;
    private final ReviewService reviewService;

    // 특정 책의 상세 정보 조회
    @GetMapping("/{isbn}")
    public ApiResponse<BookDetailResponse> getBookDetails(@PathVariable String isbn) {
        BookDetailResponse bookDetailResponse = transactionalBookService.getBookDetail(isbn);
        return ApiResponse.success(bookDetailResponse);
    }

    // 특정 책에 대한 리뷰 조회
    @GetMapping("/{isbn}/reviews")
    public ApiResponse<ReviewPaginationResponse> getReviewsByBook(
        @PathVariable String isbn,
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "latest") String sort, // 정렬 가능,
        @AuthenticationPrincipal CustomOAuth2User principal
    ) {
        Long currentUserId = (principal != null) ? principal.getId() : null; // 비로그인 허용
        ReviewPaginationResponse response = reviewService.getBookReviews(
            isbn,
            new ReviewPaginationRequest(cursor, size),
            sort,
            currentUserId // 사용자 ID 전달
        );
        return ApiResponse.success(response);
    }

    // 특정 책에 리뷰 생성
    @PostMapping("/{isbn}/reviews")
    public ApiResponse<Long> addReviewToBook(
        @AuthenticationPrincipal CustomOAuth2User principal,
        @PathVariable String isbn,
        @RequestBody @Valid CreateReviewRequest request
    ) {
        Long reviewId = reviewService.createReview(isbn, principal.getId(), request);
        return ApiResponse.success(reviewId);
    }
}
