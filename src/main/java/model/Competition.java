package model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Scoring Rules
    private int pointsPerPage;      // 0 if the competition only counts days
    private int pointsPerDayLogged; // Fixed points for any reading activity
    private int completedBookBonus; // Extra points when finishing a book

    // Streak Rules
    private boolean streakActive;
    private int daysToStreak;       // e.g., 5 consecutive days
    private int streakBonusPoints;  // e.g., +10 points
}