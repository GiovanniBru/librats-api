package org.librats.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // Ex: "Rato Noturno"
    private String icon;        // Ex: "🌙" ou "📚"
    private String description; // Ex: "Leu após as 22:00"

    @ManyToMany(mappedBy = "badges")
    private List<User> users;
}