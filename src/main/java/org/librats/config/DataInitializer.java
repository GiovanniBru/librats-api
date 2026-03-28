package org.librats.config;

import org.librats.model.*;
import org.librats.repository.*;
import org.librats.service.ReadingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private ReadingLogService readingLogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("🚀 Iniciando carga de dados do LibRats...");

        // 1. Criar Livros de Exemplo
        Book b1 = new Book("O Hobbit", "J.R.R. Tolkien", 310, "Fiction");
        b1.setTags(List.of("Fantasia"));
        bookRepository.save(b1);
        Book b2 = new Book("Código Limpo", "Robert C. Martin", 464, "Education");
        bookRepository.save(b2);

        // No método run:
        User u1 = new User("giovanni", passwordEncoder.encode("123"), "gio@email.com");
        u1.setDisplayName("Giovanni Carvalho");
        userRepository.save(u1);

        User u2 = new User("leitora_vip", "senha456", "namorada@email.com");
        u2.setDisplayName("Leitora VIP ✨");

        userRepository.saveAll(Arrays.asList(u1, u2));

        // 3. Criar a Competição com Regras Personalizadas
        // Regras: 2 pts por página, 10 pts por sessão, 50 pts bônus por terminar
        Competition comp = new Competition();
        comp.setName("Maratona de Outono 2026");
        comp.setPointsPerPage(2);
        comp.setPointsPerSession(10);
        comp.setCompletedBookBonus(50);
        comp.setStartDate(LocalDate.now());
        comp.setParticipants(Arrays.asList(u1, u2));

        competitionRepository.save(comp);

        // 4. Simular Logs de Leitura (Onde a mágica da pontuação acontece)

        // Log do Giovanni: Leu 30 páginas, mas não terminou o livro.
        // Cálculo: (30 páginas * 2) + 10 por sessão = 70 pontos.
        ReadingLog logGio = new ReadingLog();
        logGio.setUser(u1);
        logGio.setBook(b1);
        logGio.setCompetition(comp);
        logGio.setPagesRead(30);
        logGio.setFinished(false);
        readingLogService.logReading(logGio);

        // Log da Leitora VIP: Leu apenas 5 páginas, mas TERMINOU o livro.
        // Cálculo: (5 páginas * 2) + 10 por sessão + 50 bônus = 70 pontos.
        // (Empate técnico para testar o ranking!)
        ReadingLog logNami = new ReadingLog();
        logNami.setUser(u2);
        logNami.setBook(b2);
        logNami.setCompetition(comp);
        logNami.setPagesRead(5);
        logNami.setFinished(true);
        readingLogService.logReading(logNami);

        System.out.println("✅ Dados carregados! Acesse http://localhost:8080/api/competitions/1/ranking");
    }

}