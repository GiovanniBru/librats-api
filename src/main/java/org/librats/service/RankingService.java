package org.librats.service;

import org.librats.dto.RankingDTO;
import org.librats.model.Competition;
import org.librats.model.ReadingLog;
import org.librats.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private CompetitionRepository competitionRepository;

    public List<RankingDTO> getRankingByCompetition(Long competitionId) {
        Competition comp = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        // USAR A META CADASTRADA: Se for SINGLE_BOOK, usa o total de páginas do livro
        int goal = (comp.getGoalPages() != null && comp.getGoalPages() > 0) ? comp.getGoalPages() : 1000;

        return comp.getParticipants().stream()
                .map(user -> {
                    // 1. Filtramos os logs desta competição
                    List<ReadingLog> compLogs = user.getLogs().stream()
                            .filter(log -> log.getCompetition() != null &&
                                    log.getCompetition().getId().equals(competitionId))
                            .collect(Collectors.toList());

                    // 2. Somamos pontos e páginas (usando long para evitar erros de tipo)
                    long pointsSum = compLogs.stream()
                            .mapToLong(log -> log.getPointsEarned() != null ? log.getPointsEarned().longValue() : 0L)
                            .sum();

                    long pagesSum = compLogs.stream()
                            .mapToLong(log -> log.getPagesRead() != null ? log.getPagesRead().longValue() : 0L)
                            .sum();

                    // 3. Cálculo da Porcentagem (Garante que não passe de 100%)
                    int percentage = (int) ((pagesSum * 100) / goal);
                    if (percentage > 100) percentage = 100;

                    // 4. Retornamos o DTO com o novo campo de progresso
                    return new RankingDTO(
                            user.getDisplayName(),
                            (int) pointsSum,
                            (int) pagesSum,
                            percentage
                    );
                })
                // 5. Ordenação por pontos (do maior para o menor)
                .sorted(Comparator.comparingInt(RankingDTO::getTotalPoints).reversed())
                .collect(Collectors.toList());
    }
}