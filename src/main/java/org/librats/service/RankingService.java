package org.librats.service;

import org.librats.dto.RankingDTO;
import org.librats.model.User;
import org.librats.model.Competition;
import org.librats.model.ReadingLog;
import org.librats.repository.ReadingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                //if (log.getPagesRead() > 0 || log.isBookFinished()) {
                //    totalPoints += competition.getPointsPerDayLogged();
                //}

                // Regra 3: Bônus por finalizar livro
                //if (log.isBookFinished()) {
                //    totalPoints += competition.getCompletedBookBonus();
                //}
                // No método logReading:
                if (Boolean.TRUE.equals(log.getFinished())) { // O Lombok gera getFinished() ou isFinished()
                    totalPoints += competition.getCompletedBookBonus();
                }
            }
        }

        return totalPoints;
    }

    public List<RankingDTO> getCompetitionRanking(Competition competition) {
        // Pegamos todos os usuários daquela competição
        List<User> participants = competition.getParticipants();

        return participants.stream()
                .map(user -> {
                    Integer points = readingLogRepository.sumPointsByUserAndCompetition(user, competition);
                    // Se o usuário não tiver logs, a soma volta null, então tratamos para 0
                    int totalPoints = (points != null) ? points : 0;
                    return new RankingDTO(user.getDisplayName(), totalPoints, 0L);
                })
                // Ordena do maior para o menor ponto
                .sorted((u1, u2) -> u2.getTotalPoints().compareTo(u1.getTotalPoints()))
                .collect(Collectors.toList());
    }
}