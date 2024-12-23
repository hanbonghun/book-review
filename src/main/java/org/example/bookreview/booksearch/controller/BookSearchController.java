package org.example.bookreview.booksearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.booksearch.dto.NaverBookSearchResponse;
import org.example.bookreview.booksearch.service.NaverBookSearchService;
import org.example.bookreview.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookSearchController {

    private final NaverBookSearchService bookSearchService;

    @GetMapping("/search")
    public ApiResponse<NaverBookSearchResponse> searchBooks(
        @RequestParam String query,
        @RequestParam(defaultValue = "10") int display,
        @RequestParam(defaultValue = "1") int start,
        @RequestParam(defaultValue = "sim") String sort
    ) {
        NaverBookSearchResponse response = bookSearchService.searchBooks(query, display, start,
            sort);
        return ApiResponse.success(response);
    }
}