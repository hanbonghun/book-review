package org.example.bookreview.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookreview.review.domain.Review.ReadingStatus;
import org.springframework.stereotype.Service;

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
    private int likeCount;
    @Setter
    private boolean likedByCurrentUser;
}
