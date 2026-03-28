package org.librats.service;

import org.librats.model.Book;
import org.librats.model.ReadingLog;
import org.librats.model.Competition;
import org.librats.repository.BookRepository;
import org.librats.repository.CompetitionRepository;
import org.librats.repository.ReadingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReadingLogService {

    @Autowired
    private ReadingLogRepository readingLogRepository;

    @Autowired
    private BookRepository bookRepository; // Garanta que o repository esteja injetado

    @Autowired
    private CompetitionRepository competitionRepository;

    public ReadingLog logReading(ReadingLog log) {
        // 1. CARREGAR OS DADOS REAIS DO BANCO (O formulário só manda o ID)
        Book book = bookRepository.findById(log.getBook().getId())
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        Competition comp = competitionRepository.findById(log.getCompetition().getId())
                .orElseThrow(() -> new IllegalArgumentException("Competição não encontrada"));

        // Atualiza o objeto log com as entidades completas
        log.setBook(book);
        log.setCompetition(comp);

        // 2. AGORA AS VALIDAÇÕES FUNCIONAM
        if (log.getPagesRead() <= 0) {
            throw new IllegalArgumentException("Você precisa ler pelo menos uma página!");
        }

        if (book.getPages() != null && log.getPagesRead() > book.getPages()) {
            throw new IllegalArgumentException("Lido (" + log.getPagesRead() + ") excede o total do livro (" + book.getPages() + ").");
        }

        // 3. CÁLCULO DE PONTOS
        int totalSessionPoints = (log.getPagesRead() * comp.getPointsPerPage()) + comp.getPointsPerSession();
        if (Boolean.TRUE.equals(log.getFinished())) {
            totalSessionPoints += comp.getCompletedBookBonus();
        }

        System.out.println("Pontos calculados: " + totalSessionPoints);

        return readingLogRepository.save(log);
    }

    // --- NOVO MÉTODO PARA O RELATÓRIO ---
    public List<ReadingLog> getUserLogs(Long userId) {
        // Retorna o histórico de um usuário específico ordenado pelo mais recente
        return readingLogRepository.findByUserIdOrderByLogDateDesc(userId);
    }

}