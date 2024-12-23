package org.example.bookreview.book.repository;

import org.example.bookreview.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}
