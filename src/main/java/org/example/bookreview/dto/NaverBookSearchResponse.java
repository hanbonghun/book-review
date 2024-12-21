package org.example.bookreview.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverBookSearchResponse {
    private List<BookSearchItem> items;
    private int total;
    private int start;
    private int display;

    @Getter
    @NoArgsConstructor
    public static class BookSearchItem {
        private String title;
        private String link;
        private String image;
        private String author;
        private int discount;
        private String publisher;
        private String isbn;
        private String description;
        private String pubdate;
    }
}