package org.example.bookreview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookreview.domain.Review.ReadingStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CreateReviewRequest {

    @NotBlank(message = "ISBN은 필수 값입니다.")
    private String isbn;

    @NotBlank(message = "책 제목은 필수 값입니다.")
    private String bookTitle;

    @NotBlank(message = "저자 정보는 필수 값입니다.")
    private String bookAuthors;

    @NotBlank(message = "출판사는 필수 값입니다.")
    private String publisher;

    @NotNull(message = "평점은 필수 값입니다.")
    @Min(value = 1, message = "평점은 최소 1이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5이어야 합니다.")
    private Integer rating;
    @NotBlank(message = "리뷰 내용은 필수 값입니다.")
    private String content;

    @NotNull(message = "읽기 상태는 필수 값입니다.")
    private ReadingStatus readingStatus; // Enum 타입 검증
}