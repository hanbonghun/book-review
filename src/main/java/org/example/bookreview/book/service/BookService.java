package org.example.bookreview.book.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.book.dto.BookDetailResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final RetryableBookService retryableBookService;
    private final TransactionalBookService transactionalBookService;

    public BookDetailResponse getBookDetail(String isbn) {
        // 1. 먼저 책 정보 가져오거나 생성 (트랜잭션 분리)
        Book book = retryableBookService.getOrCreateBookWithRetry(isbn);

        // 2. 조회 로직 실행 (읽기 전용 트랜잭션)
        return transactionalBookService.getBookDetail(book.getIsbn());
    }
}