package org.example.bookreview.review.domain;

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
import org.example.bookreview.base.BaseTimeEntity;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private int rating;
    private String content;

    @Enumerated(EnumType.STRING)
    private ReadingStatus readingStatus;

    @Builder
    public Review(Member member, Book book, int rating,
        String content, ReadingStatus readingStatus) {
        this.member = member;
        this.book = book;
        this.rating = rating;
        this.content = content;
        this.readingStatus = readingStatus;
    }

    public enum ReadingStatus {
        READING, READ, WANT_TO_READ
    }
}