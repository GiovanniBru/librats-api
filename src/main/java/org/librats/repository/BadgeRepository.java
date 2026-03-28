package org.librats.repository;

import org.librats.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    // Busca uma badge pelo nome para não criarmos duplicadas no banco
    Optional<Badge> findByName(String name);
}