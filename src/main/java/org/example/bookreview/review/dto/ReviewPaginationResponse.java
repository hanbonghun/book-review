package org.example.bookreview.review.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewPaginationResponse {
    private List<ReviewResponse> reviews;
    private String nextCursor;
    private boolean hasNext;
}