package org.librats.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Nome de usuário para login

    @Column(nullable = false)
    private String password; // Senha (que será criptografada)

    @Column(unique = true, nullable = false)
    private String email;

    private String displayName; // Nome que aparece no ranking
    private String profilePictureUrl;

    // Relacionamento com as competições que o usuário participa
    @ManyToMany
    @JoinTable(
            name = "user_competitions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "competition_id")
    )
    private List<Competition> competitions;

    public User() {}

    // Construtor útil para os primeiros testes de login
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}