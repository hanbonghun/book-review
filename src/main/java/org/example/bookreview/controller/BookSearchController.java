package org.example.bookreview.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.dto.NaverBookSearchResponse;
import org.example.bookreview.service.NaverBookSearchService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<NaverBookSearchResponse> searchBooks(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int display,
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "sim") String sort
    ) {
        NaverBookSearchResponse response = bookSearchService.searchBooks(query, display, start, sort);
        return ResponseEntity.ok(response);
    }
}