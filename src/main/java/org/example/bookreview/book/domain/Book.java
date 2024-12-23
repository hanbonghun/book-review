package org.example.bookreview.book.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookreview.base.BaseTimeEntity;
import org.example.bookreview.booksearch.dto.NaverBookSearchResponse;
import org.example.bookreview.review.domain.Review;
import org.example.bookreview.utils.DateParserUtils;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Book extends BaseTimeEntity {

    @Id
    private String isbn;

    private String title;
    private String author;
    private String publisher;
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate publishDate;

    @OneToMany(mappedBy = "book")
    private List<Review> reviews;

    private LocalDateTime lastSyncedAt;

    public static Book fromNaverBook(NaverBookSearchResponse.BookSearchItem item) {
        return Book.builder()
            .isbn(item.getIsbn())
            .title(item.getTitle())
            .author(item.getAuthor())
            .publisher(item.getPublisher())
            .imageUrl(item.getImage())
            .description(item.getDescription())
            .publishDate(DateParserUtils.parsePubDate(item.getPubdate()))
            .lastSyncedAt(LocalDateTime.now())
            .build();
    }
}