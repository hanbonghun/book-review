package org.example.bookreview.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.oauth.CustomOAuth2User;
import org.example.bookreview.review.dto.CreateReviewRequest;
import org.example.bookreview.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createReview(
        @AuthenticationPrincipal CustomOAuth2User principal,
        @RequestBody @Valid CreateReviewRequest request
    ) {
        Long reviewId = reviewService.createReview(principal.getId(), request);
        return ResponseEntity.ok(reviewId);
    }
}