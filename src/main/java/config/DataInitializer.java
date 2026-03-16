package config;

import model.*;
import repository.*;
import service.RankingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   CompetitionRepository competitionRepository,
                                   ReadingLogRepository readingLogRepository,
                                   RankingService rankingService) {
        return args -> {
            // 1. Criando o seu usuário
            User giovanni = new User();
            giovanni.setUsername("giovanni");
            giovanni.setEmail("giovanni@email.com");
            giovanni.setPassword("123456");
            userRepository.save(giovanni);

            // 2. Criando a competição "LibRats Alpha"
            Competition comp = new Competition();
            comp.setName("LibRats Alpha Challenge");
            comp.setPointsPerPage(1);       // 1 ponto por página
            comp.setPointsPerDayLogged(5);  // 5 pontos por dia lido
            comp.setCompletedBookBonus(50); // 50 pontos por terminar o livro
            competitionRepository.save(comp);

            // 3. Criando um log de leitura: Giovanni leu 20 páginas e terminou o livro hoje
            ReadingLog log1 = new ReadingLog();
            log1.setUser(giovanni);
            log1.setCompetition(comp);
            log1.setPagesRead(20);
            log1.setBookFinished(true);
            log1.setLogDate(LocalDate.now());
            readingLogRepository.save(log1);

            // 4. Testando o cálculo de pontos no Console
            int points = rankingService.calculateUserPoints(giovanni.getId(), comp);

            System.out.println("--------------------------------------");
            System.out.println("LIB RATS - TESTE DE PONTUAÇÃO");
            System.out.println("Usuário: " + giovanni.getUsername());
            System.out.println("Pontos Totais: " + points);
            // Cálculo esperado: (20 pag * 1) + 5 (dia logado) + 50 (bônus livro) = 75 pontos
            System.out.println("--------------------------------------");
        };
    }
}