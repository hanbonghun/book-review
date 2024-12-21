package org.example.bookreview.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;
    private String bookTitle;
    private String bookAuthors;
    private String publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int rating;
    private String content;

    @Enumerated(EnumType.STRING)
    private ReadingStatus readingStatus;

    @Builder
    public Review(String isbn, String bookTitle, String bookAuthors, String publisher,
        Member member, int rating, String content, ReadingStatus readingStatus) {
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.bookAuthors = bookAuthors;
        this.publisher = publisher;
        this.member = member;
        this.rating = rating;
        this.content = content;
        this.readingStatus = readingStatus;
    }

    public enum ReadingStatus {
        READING, READ, WANT_TO_READ
    }
}