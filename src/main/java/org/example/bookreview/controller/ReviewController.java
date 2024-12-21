package org.example.bookreview.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.dto.CreateReviewRequest;
import org.example.bookreview.service.review.ReviewService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Long> createReview(
        @RequestParam Long memberId,  // 임시로 memberId를 파라미터로 받음
        @RequestBody @Valid CreateReviewRequest request
    ) {
        Long reviewId = reviewService.createReview(memberId, request);
        return ResponseEntity.ok(reviewId);
    }
}