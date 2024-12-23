package org.example.bookreview.book.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.dto.BookDetailResponse;
import org.example.bookreview.book.service.TransactionalBookService;
import org.example.bookreview.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final TransactionalBookService transactionalBookService;

    @GetMapping("/{isbn}")
    public ApiResponse<BookDetailResponse> getBookDetails(@PathVariable String isbn) {
        BookDetailResponse bookDetailResponse = transactionalBookService.getBookDetail(isbn);

        return ApiResponse.success(bookDetailResponse);
    }
}
