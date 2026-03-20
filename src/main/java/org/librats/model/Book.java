package org.librats.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Integer pages;
    private String category;

    public Book() {}

    public Book(String title, String author, Integer pages, String category) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.category = category;
    }

    // --- NOSSA REGRA DE NEGÓCIO (Business Logic) ---
    public int calculatePoints() {
        int basePoints = this.pages; // 1 page = 1 point

        if ("Fiction".equalsIgnoreCase(this.category)) {
            return basePoints + 10; // Bonus for Fiction!
        }

        return basePoints;
    }
}