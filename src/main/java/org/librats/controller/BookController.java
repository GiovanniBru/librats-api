package org.librats.controller;

import org.librats.model.Book;
import org.librats.service.BookService;
import org.librats.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.librats.dto.BookResponseDTO;
import java.util.stream.Collectors;
import java.util.List;

@RestController // Diz ao Spring que esta classe responde via Web (JSON)
@RequestMapping("/api/books") // O endereço base será localhost:8080/api/books
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository; // Para listar direto, usamos o repo

    // Este método responde quando você acessa o endereço no navegador
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Um "endpoint" especial para mostrar o Score atual do jogador
    @GetMapping("/score")
    public String getScore() {
        int score = bookService.calculateTotalUserScore();
        return "Current Player Score: " + score;
    }

    @GetMapping("/filter")
    public List<Book> getBooksByCategory(@RequestParam String category) {
        return bookRepository.findByCategoryIgnoreCase(category);
    }

    @GetMapping("/summary")
    public List<BookResponseDTO> getBooksSummary() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponseDTO::new) // Transforma cada Book em um DTO
                .collect(Collectors.toList());
    }
}