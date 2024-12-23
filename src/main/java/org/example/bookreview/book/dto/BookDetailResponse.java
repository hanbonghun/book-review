package org.example.bookreview.book.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookDetailResponse {
    private String isbn;
    private String title;
    private String description;
    private String imageUrl;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
}