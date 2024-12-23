package org.example.bookreview.review.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSearchCondition {
    private String isbn;          // 책 ISBN
    private Long memberId;        // 회원 ID
    private String sortBy;        // 정렬 기준 (latest, rating-desc 등)
    private LocalDateTime cursor; // 페이징 커서
    private Integer size;         // 페이지 크기

    public static ReviewSearchCondition ofRecent(ReviewPaginationRequest request) {
        return ReviewSearchCondition.builder()
            .cursor(request.getCursor() != null ? LocalDateTime.parse(request.getCursor()) : null)
            .size(request.getSize())
            .sortBy("latest")
            .build();
    }

    public static ReviewSearchCondition ofBook(String isbn, ReviewPaginationRequest request, String sort) {
        return ReviewSearchCondition.builder()
            .isbn(isbn)
            .cursor(request.getCursor() != null ? LocalDateTime.parse(request.getCursor()) : null)
            .size(request.getSize())
            .sortBy(sort)
            .build();
    }

    public static ReviewSearchCondition ofMember(Long memberId, ReviewPaginationRequest request) {
        return ReviewSearchCondition.builder()
            .memberId(memberId)
            .cursor(request.getCursor() != null ? LocalDateTime.parse(request.getCursor()) : null)
            .size(request.getSize())
            .sortBy("latest")
            .build();
    }
}