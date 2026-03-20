package org.librats.repository;

import org.librats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // O Spring cria a lógica deste metodo apenas pelo nome dele!
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}