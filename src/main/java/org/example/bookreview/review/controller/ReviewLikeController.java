package org.example.bookreview.review.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.response.ApiResponse;
import org.example.bookreview.oauth.CustomOAuth2User;
import org.example.bookreview.review.service.ReviewLikeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    // 좋아요 추가
    @PostMapping("/{reviewId}/likes")
    public ApiResponse<Void> addLike(
        @PathVariable Long reviewId,
        @AuthenticationPrincipal CustomOAuth2User principal
    ) {
        reviewLikeService.addLikeOnReview(reviewId, principal.getId());
        return ApiResponse.success();
    }

    // 좋아요 삭제
    @DeleteMapping("/{reviewId}/likes")
    public ApiResponse<Void> removeLike(
        @PathVariable Long reviewId,
        @AuthenticationPrincipal CustomOAuth2User principal
    ) {
        reviewLikeService.removeLikeOnReview(reviewId, principal.getId());
        return ApiResponse.success();
    }
}