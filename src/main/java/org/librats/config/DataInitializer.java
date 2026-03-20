package org.librats.config;

import org.librats.model.Book;
import org.librats.service.BookService; // Importamos o serviço agora
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BookService bookService; // Injetamos o Service em vez do Repository

    @Override
    public void run(String... args) throws Exception {
        // Criando livros (English names!)
        bookService.saveBook(new Book("The Hobbit", "J.R.R. Tolkien", 310, "Fiction"));
        bookService.saveBook(new Book("Java Fundamentals", "Udacity", 150, "Education"));
        bookService.saveBook(new Book("Clean Code", "Robert C. Martin", 464, "Education"));

        System.out.println("\n\n=======================================");
        System.out.println("   LIB RATS - GLOBAL RANKING           ");
        System.out.println("   Player: Giovanni Carvalho           ");
        System.out.println("   Books in Library: " + bookService.getBookCount());
        System.out.println("   Total Player Score: " + bookService.calculateTotalUserScore());
        System.out.println("=======================================\n\n");
    }
}