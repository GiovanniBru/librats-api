package repository;

import model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    // Caso queira buscar competições pelo nome
    List<Competition> findByNameContainingIgnoreCase(String name);
}

