package org.example.bookreview.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookreview.domain.Review.ReadingStatus;

@Getter
@NoArgsConstructor
public class CreateReviewRequest {
    private String isbn;
    private String bookTitle;
    private String bookAuthors;
    private String publisher;
    private int rating;
    private String content;
    private ReadingStatus readingStatus;
}