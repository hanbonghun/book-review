package org.example.bookreview.book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.booksearch.dto.NaverBookSearchResponse;
import org.example.bookreview.booksearch.service.NaverBookSearchService;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryableBookService {

    private final NaverBookSearchService naverBookSearchService;
    private final TransactionalBookService transactionalBookService;

    @Retryable(
        retryFor = {BusinessException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public Book getOrCreateBookWithRetry(String isbn) {
        return transactionalBookService.findBookOptionally(isbn)
            .orElseGet(() -> createBookFromNaverApi(isbn));
    }

    private Book createBookFromNaverApi(String isbn) {
        NaverBookSearchResponse response = naverBookSearchService.searchBooks(isbn, 1, 1, "sim");

        if (response.getItems().isEmpty()) {
            throw new BusinessException(ErrorType.BOOK_NOT_FOUND);
        }

        Book book = Book.fromNaverBook(response.getItems().get(0));
        return transactionalBookService.saveBook(book);
    }

    @Recover
    public Book recoverCreateBook(BusinessException e, String isbn) {
        log.error("ISBN: {}에 대한 모든 재시도 시도가 실패했습니다.", isbn, e);
        throw new BusinessException(ErrorType.EXTERNAL_API_ERROR);
    }
}
