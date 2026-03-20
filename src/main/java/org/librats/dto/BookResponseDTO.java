package org.librats.dto;

import lombok.Getter;
import org.librats.model.Book;

@Getter
public class BookResponseDTO {
    private String title;
    private String author;
    private int score; // Aqui já mandamos o cálculo pronto!

    public BookResponseDTO(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.score = book.calculatePoints();
    }
}