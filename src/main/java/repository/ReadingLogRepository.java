package repository;

import model.User;
import model.Competition;
import model.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
