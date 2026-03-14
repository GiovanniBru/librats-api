package model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // "user" é palavra reservada no H2, por isso usamos "users"
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;
}