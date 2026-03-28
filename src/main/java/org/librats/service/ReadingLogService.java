package org.librats.service;

import org.librats.model.*;
import org.librats.repository.*;
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
        // 1. CARREGA O LIVRO E COMPETIÇÃO REAIS (Evita NullPointerException)
        Book book = bookRepository.findById(log.getBook().getId())
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        Competition comp = competitionRepository.findById(log.getCompetition().getId())
                .orElseThrow(() -> new IllegalArgumentException("Competição não encontrada"));

        log.setBook(book);
        log.setCompetition(comp);

        // 2. VALIDAÇÃO ANTI-CHEAT (A mesma que fizemos antes)
        if (book.getPages() != null && log.getPagesRead() > book.getPages()) {
            throw new IllegalArgumentException("Calma lá! Você leu mais páginas do que o livro tem.");
        }

        // 3. CÁLCULO DOS PONTOS
        int points = (log.getPagesRead() * comp.getPointsPerPage()) + comp.getPointsPerSession();
        if (Boolean.TRUE.equals(log.getFinished())) {
            points += comp.getCompletedBookBonus();
        }

        // 4. O PULO DO GATO: Salva os pontos no objeto ANTES de ir para o banco
        log.setPointsEarned(points);

        // 5. SALVA O LOG NO BANCO
        ReadingLog savedLog = readingLogRepository.save(log);

        // 6. CHECA SE GANHOU CONQUISTAS (Badges)
        checkAndAssignBadges(log.getUser(), savedLog);

        System.out.println("✅ Leitura registrada! Pontos ganhos: " + points);

        return savedLog;
    }

    // --- NOVO MÉTODO PARA O RELATÓRIO ---
    public List<ReadingLog> getUserLogs(Long userId) {
        // Retorna o histórico de um usuário específico ordenado pelo mais recente
        return readingLogRepository.findByUserIdOrderByLogDateDesc(userId);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    public void checkAndAssignBadges(User user, ReadingLog log) {
        // Regra 1: Rato Noturno (Leitura após as 22h)
        if (log.getLogDate().getHour() >= 22 || log.getLogDate().getHour() < 5) {
            assignBadge(user, "Rato Noturno", "🌙", "Leitura na calada da noite!");
        }

        // Regra 2: Finalizador (Terminou o primeiro livro)
        if (Boolean.TRUE.equals(log.getFinished())) {
            assignBadge(user, "Finalizador", "🚩", "Terminou seu primeiro livro no LibRats!");
        }
    }

    private void assignBadge(User user, String name, String icon, String desc) {
        // 1. Verifica se a Badge já existe no sistema, se não, cria.
        Badge badge = badgeRepository.findByName(name)
                .orElseGet(() -> {
                    Badge newBadge = new Badge();
                    newBadge.setName(name);
                    newBadge.setIcon(icon);
                    newBadge.setDescription(desc);
                    return badgeRepository.save(newBadge);
                });

        // 2. Verifica se o usuário já possui essa conquista específica
        if (!user.getBadges().contains(badge)) {
            user.getBadges().add(badge);
            userRepository.save(user); // Salva a relação na tabela de junção
            System.out.println("🏆 CONQUISTA DESBLOQUEADA: " + name);
        }
    }

}