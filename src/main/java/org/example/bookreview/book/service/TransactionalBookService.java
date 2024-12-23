package org.example.bookreview.book.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.book.domain.Book;
import org.example.bookreview.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionalBookService {
   
   private final BookRepository bookRepository;
   
   @Transactional(readOnly = true)
   public Optional<Book> findBookOptionally(String isbn) {
       return bookRepository.findById(isbn);
   }
   
   @Transactional
   public Book saveBook(Book book) {
       return bookRepository.save(book);
   }
}