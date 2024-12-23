package org.example.bookreview.booksearch.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverBookSearchResponse {

    private String lastBuildDate;
    private int total;
    private List<BookSearchItem> items;
    private int start;
    private int display;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookSearchItem {

        private String title;
        private String link;
        private String image;
        private String author;
        private String discount;
        private String publisher;
        private String pubdate;
        private String isbn;
        private String description;
    }
}