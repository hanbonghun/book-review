package org.example.bookreview.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookreview.review.domain.Review.ReadingStatus;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String isbn;
    private String bookTitle;
    private String bookAuthors;
    private String publisher;
    private String content;
    private int rating;
    private ReadingStatus readingStatus;
    private String memberName; // 작성자 이름
    private LocalDateTime createdAt;
}
