package org.librats.repository;

import org.librats.model.User;
import org.librats.model.Competition;
import org.librats.model.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {
    // Busca todos os registros de um usuário específico
    List<ReadingLog> findByUser(User user);

    // Busca todos os registros de um usuário dentro de uma competição específica
    // Isso será fundamental para o Ranking!
    List<ReadingLog> findByUserAndCompetition(User user, Competition competition);

    // Busca todos os registros de uma competição para montar o ranking geral
    List<ReadingLog> findByCompetition(Competition competition);

    // Busca os últimos logs do usuário para sugerir livros rápidos
    List<ReadingLog> findByUserOrderByLogDateDesc(User user);

    List<ReadingLog> findByUserIdOrderByLogDateDesc(Long userId);

    /**
     * Esta consulta soma os pontos do usuário em uma competição específica.
     * Estamos usando JPQL (Java Persistence Query Language).
     */
    @Query("SELECT SUM( (rl.pagesRead * c.pointsPerPage) + c.pointsPerSession + " +
            "CASE WHEN rl.finished = true THEN c.completedBookBonus ELSE 0 END ) " + // Ajustado aqui
            "FROM ReadingLog rl JOIN rl.competition c " +
            "WHERE rl.user = :user AND rl.competition = :competition")
    Integer sumPointsByUserAndCompetition(@Param("user") User user,
                                          @Param("competition") Competition competition);
}
