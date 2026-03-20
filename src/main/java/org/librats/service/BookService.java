package org.librats.service;

import org.librats.model.Book;
import org.librats.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Isso diz ao Spring que esta classe contém a Lógica de Negócio
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void saveBook(Book book) {
        // Aqui poderíamos validar se o livro já existe antes de salvar
        bookRepository.save(book);
    }

    public int calculateTotalUserScore() {
        List<Book> allBooks = bookRepository.findAll();

        // Usando o que você aprendeu na Udacity: Loop para somar pontos
        int totalScore = 0;
        for (Book book : allBooks) {
            totalScore += book.calculatePoints();
        }
        return totalScore;
    }

    public long getBookCount() {
        return bookRepository.count();
    }
}