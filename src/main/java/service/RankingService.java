package service;

import model.Competition;
import model.ReadingLog;
import repository.ReadingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    @Autowired
    private ReadingLogRepository readingLogRepository;

    /**
     * Calcula o total de pontos de um usuário em uma competição específica.
     * Note como usamos as regras da própria Competition para o cálculo.
     */
    public int calculateUserPoints(Long userId, Competition competition) {
        // 1. Buscamos todos os registros de leitura desse usuário nessa competição
        // (Aqui precisaríamos do objeto User, vamos simplificar a lógica por agora)
        List<ReadingLog> logs = readingLogRepository.findByCompetition(competition);

        int totalPoints = 0;

        for (ReadingLog log : logs) {
            // Filtramos apenas o usuário que queremos
            if (log.getUser().getId().equals(userId)) {

                // Regra 1: Pontos por página
                totalPoints += log.getPagesRead() * competition.getPointsPerPage();

                // Regra 2: Pontos fixos por dia logado (se leu algo, ganha o fixo)
                if (log.getPagesRead() > 0 || log.isBookFinished()) {
                    totalPoints += competition.getPointsPerDayLogged();
                }

                // Regra 3: Bônus por finalizar livro
                if (log.isBookFinished()) {
                    totalPoints += competition.getCompletedBookBonus();
                }
            }
        }

        return totalPoints;
    }
}