package org.example.bookreview.review.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.response.ApiResponse;
import org.example.bookreview.review.dto.ReviewPaginationRequest;
import org.example.bookreview.review.dto.ReviewPaginationResponse;
import org.example.bookreview.review.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/recent")
    public ApiResponse<ReviewPaginationResponse> getRecentReviews(
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "10") int size
    ) {
        ReviewPaginationResponse response = reviewService.getRecentReviews(
            new ReviewPaginationRequest(cursor, size)
        );
        return ApiResponse.success(response);
    }
}