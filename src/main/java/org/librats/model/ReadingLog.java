package org.librats.model;

import jakarta.persistence.*;
import lombok.Data;
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

    private Boolean bookFinished;
    private String comment;
    private String photoUrl; // Para a foto que você planejou

    private LocalDateTime logDate;

    public ReadingLog() {
        this.logDate = LocalDateTime.now();
    }
}