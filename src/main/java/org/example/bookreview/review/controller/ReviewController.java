package org.example.bookreview.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.response.ApiResponse;
import org.example.bookreview.oauth.CustomOAuth2User;
import org.example.bookreview.review.dto.CreateReviewRequest;
import org.example.bookreview.review.dto.ReviewPaginationRequest;
import org.example.bookreview.review.dto.ReviewPaginationResponse;
import org.example.bookreview.review.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<Long> createReview(
        @AuthenticationPrincipal CustomOAuth2User principal,
        @RequestBody @Valid CreateReviewRequest request
    ) {
        Long reviewId = reviewService.createReview(principal.getId(), request);
        return ApiResponse.success(reviewId);
    }

    @GetMapping
    public ApiResponse<ReviewPaginationResponse> getReviews(
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "10") int size
    ) {
        ReviewPaginationResponse response = reviewService.getReviews(
            new ReviewPaginationRequest(cursor, size)
        );
        return ApiResponse.success(response);
    }
}