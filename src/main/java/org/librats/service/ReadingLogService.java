package org.librats.service;

import org.librats.model.ReadingLog;
import org.librats.model.Competition;
import org.librats.repository.ReadingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadingLogService {

    @Autowired
    private ReadingLogRepository readingLogRepository;

    public ReadingLog logReading(ReadingLog log) {
        // Buscamos as regras da competição ligada a esse log
        Competition comp = log.getCompetition();

        int totalPoints = 0;

        // 1. Pontos por página
        totalPoints += (log.getPagesRead() * comp.getPointsPerPage());

        // 2. Pontos por sessão de leitura
        totalPoints += comp.getPointsPerSession();

        // 3. Bônus por concluir o livro
        //if (Boolean.TRUE.equals(log.getFinishedBook())) {
        //    totalPoints += comp.getCompletedBookBonus();
        //}

        // Por enquanto, vamos imprimir no console para validar
        System.out.println("=== NEW LOG REGISTERED ===");
        System.out.println("Points earned in this session: " + totalPoints);

        return readingLogRepository.save(log);
    }
}