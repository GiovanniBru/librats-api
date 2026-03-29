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
import java.util.Map;
import java.time.LocalDate;

@Service
public class RankingService {

    @Autowired
    private CompetitionRepository competitionRepository;
    public List<RankingDTO> getRankingByCompetition(Long competitionId) {
        Competition comp = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        return comp.getParticipants().stream()
                .map(user -> {
                    // Dentro do map(user -> { ... }) no seu RankingService
                    List<ReadingLog> userLogs = user.getReadingLogs().stream()
                            .filter(log -> log.getCompetition().getId().equals(competitionId))
                            .filter(log -> comp.getEndDate() == null || !log.getDate().isAfter(comp.getEndDate()))
                            .collect(Collectors.toList());

                    int totalPoints = 0;
                    int totalPages = userLogs.stream().mapToInt(ReadingLog::getPagesRead).sum();

                    if ("PER_DAY".equals(comp.getScoreType())) {
                        // REGRA 1: CONSTÂNCIA (Já fizemos)
                        Map<LocalDate, Integer> pagesPerDay = userLogs.stream()
                                .collect(Collectors.groupingBy(ReadingLog::getDate, Collectors.summingInt(ReadingLog::getPagesRead)));

                        int pointsPerDay = (comp.getPointsPerDayLogged() != null) ? comp.getPointsPerDayLogged() : 10;
                        int minPages = (comp.getMinPagesPerDay() != null) ? comp.getMinPagesPerDay() : 1;

                        for (int pagesInDay : pagesPerDay.values()) {
                            if (pagesInDay >= minPages) totalPoints += pointsPerDay;
                        }

                    } else if ("PER_PAGE".equals(comp.getScoreType())) {
                        // REGRA 2: VOLUME (Pontos por página)
                        // Se você não criou o campo 'pointsPerPage', pode usar 1 como padrão ou criar o campo na Entity
                        int multiplier = (comp.getPointsPerPage() != null) ? comp.getPointsPerPage() : 1;
                        totalPoints = totalPages * multiplier;

                    } else if ("PER_SESSION".equals(comp.getScoreType())) {
                        // REGRA 3: ENGAJAMENTO (Pontos por cada log enviado)
                        int pointsPerSession = (comp.getPointsPerSession() != null) ? comp.getPointsPerSession() : 5;
                        totalPoints = userLogs.size() * pointsPerSession;
                    }
                    return new RankingDTO(user.getDisplayName(), totalPoints, totalPages, 100);
                })
                .sorted(Comparator.comparingInt(RankingDTO::getTotalPoints).reversed())
                .collect(Collectors.toList());
    }
}