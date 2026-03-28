package org.librats.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "competitions")
@Data
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate; // Pode ser null para "sem data de término"

    // Regras básicas
    private int pointsPerPage;
    private int pointsPerSession;
    private int finishBookBonus;
    private int pointsPerDayLogged;   // Gera o getPointsPerDayLogged()
    private int completedBookBonus;   // Gera o getCompletedBookBonus()

    // Este é o "elo" que estava faltando para o getParticipants() funcionar
    @ManyToMany
    @JoinTable(
            name = "competition_participants",
            joinColumns = @JoinColumn(name = "competition_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    // Construtor padrão para o JPA
    public Competition() {}

    // Construtor rápido para testes
    public Competition(String name, int pointsPerPage, int finishBookBonus) {
        this.name = name;
        this.pointsPerPage = pointsPerPage;
        this.finishBookBonus = finishBookBonus;
        this.startDate = LocalDate.now();
    }

    private Integer goalPages = 1000; // Valor padrão de 1000 páginas como meta
}