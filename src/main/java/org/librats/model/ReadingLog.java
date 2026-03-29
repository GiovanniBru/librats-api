package org.librats.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_logs")
@Data
public class ReadingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    private Integer pagesRead;
    private Integer minutesRead;

    private Boolean finished;
    private String comment;
    private String photoUrl; // Para a foto que você planejou

    private LocalDateTime logDate;

    private LocalDate date; // Se estiver como 'data' no seu código, mude o get no Service para getData()

    public ReadingLog() {
        this.logDate = LocalDateTime.now();
    }

    private Integer pointsEarned = 0; // Para salvar quantos pontos esse log rendeu

    public LocalDate getDate() { return date; }
    public Competition getCompetition() { return competition; }
    public Integer getPagesRead() { return pagesRead; }
}