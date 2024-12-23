package org.example.bookreview.book.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.book.repository.BookRepository;
import org.example.bookreview.booksearch.dto.NaverBookSearchResponse;
import org.example.bookreview.booksearch.service.NaverBookSearchService;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final NaverBookSearchService naverBookSearchService;
    private final BookRepository bookRepository;
    
    public Book getOrCreateBook(String isbn) {
        return bookRepository.findById(isbn)
            .orElseGet(() -> createBookFromNaverApi(isbn));
    }
    
    private Book createBookFromNaverApi(String isbn) {
        NaverBookSearchResponse response = naverBookSearchService.searchBooks(isbn, 1, 1, "sim");
        
        if (response.getItems().isEmpty()) {
            throw new BusinessException(ErrorType.BOOK_NOT_FOUND);
        }
        
        Book book = Book.fromNaverBook(response.getItems().get(0));
        return bookRepository.save(book);
    }
}