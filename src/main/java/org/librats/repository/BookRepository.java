package org.librats.repository;

import org.librats.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // O Spring entende que deve buscar pelo campo 'category' da Entity Book
    List<Book> findByCategoryIgnoreCase(String category);
}