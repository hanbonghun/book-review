package org.example.bookreview.book.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.book.dto.BookDetailResponse;
import org.example.bookreview.book.repository.BookRepository;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionalBookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookDetailResponse getBookDetail(String isbn) {
        Book book = bookRepository.findById(isbn)
            .orElseThrow(() -> new BusinessException(ErrorType.BOOK_NOT_FOUND));

        return BookDetailResponse.builder()
            .isbn(book.getIsbn())
            .title(book.getTitle())
            .description(book.getDescription())
            .imageUrl(book.getImageUrl())
            .author(book.getAuthor())
            .publisher(book.getPublisher())
            .publishedDate(book.getPublishDate())
            .build();
    }
}