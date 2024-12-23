package org.example.bookreview.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookreview.review.domain.Review.ReadingStatus;

@Getter
@NoArgsConstructor
public class CreateReviewRequest {

    @NotNull(message = "평점은 필수입니다")
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다")
    private Integer rating;

    @NotBlank(message = "리뷰 내용은 필수입니다")
    private String content;

    @NotNull(message = "독서 상태는 필수입니다")
    private ReadingStatus readingStatus;
}