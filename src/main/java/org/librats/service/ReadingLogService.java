package org.librats.service;

import org.librats.model.Book;
import org.librats.model.ReadingLog;
import org.librats.model.Competition;
import org.librats.repository.ReadingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReadingLogService {

    @Autowired
    private ReadingLogRepository readingLogRepository;

    public ReadingLog logReading(ReadingLog log) {
        Competition comp = log.getCompetition();
        Book book = log.getBook();

        if (comp != null && book != null) {
            int basePoints = log.getPagesRead() * comp.getPointsPerPage();

            // Regra de Bônus: Se o livro for de "Fantasia", ganha +20% de bônus nas páginas
            if (book.getTags() != null && book.getTags().contains("Fantasia")) {
                basePoints = (int) (basePoints * 1.2);
                System.out.println("✨ Bônus de Fantasia aplicado!");
            }

            // Você pode somar os pontos finais aqui se tiver um campo no log
            // log.setPointsEarned(basePoints + comp.getPointsPerSession());
        }

        return readingLogRepository.save(log);
    }

    // --- NOVO MÉTODO PARA O RELATÓRIO ---
    public List<ReadingLog> getUserLogs(Long userId) {
        // Retorna o histórico de um usuário específico ordenado pelo mais recente
        return readingLogRepository.findByUserIdOrderByLogDateDesc(userId);
    }

}