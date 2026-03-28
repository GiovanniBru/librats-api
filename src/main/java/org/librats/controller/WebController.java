package org.librats.controller;

import org.librats.dto.RankingDTO;
import org.librats.model.Competition;
import org.librats.model.ReadingLog;
import org.librats.model.User;
import org.librats.repository.BookRepository;
import org.librats.repository.CompetitionRepository;
import org.librats.repository.ReadingLogRepository;
import org.librats.repository.UserRepository;
import org.librats.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller // Note que aqui é @Controller, não @RestController
public class WebController {

    @Autowired
    private CompetitionRepository competitionRepository;

    @GetMapping("/")
    public String home(Model model) {
        // Passamos a lista de competições para a tela
        model.addAttribute("competitions", competitionRepository.findAll());
        return "index"; // Isso vai procurar um arquivo chamado index.html
    }

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/logar-leitura")
    public String logForm(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("competitions", competitionRepository.findAll());
        return "log-form"; // Vai procurar log-form.html
    }


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReadingLogRepository readingLogRepository;

    @GetMapping("/perfil")
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        List<ReadingLog> logs = readingLogRepository.findByUserIdOrderByLogDateDesc(user.getId());
        Integer totalPages = readingLogRepository.sumPagesByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("logs", logs);
        model.addAttribute("totalPages", totalPages != null ? totalPages : 0);

        return "profile";
    }

//    @GetMapping("/competicao/{id}/ranking")
//    public String viewRanking(@PathVariable Long id, Model model) {
//        Competition comp = competitionRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));
//
//        // Especificamos o tipo <RankingDTO> no stream para evitar o erro de "Object"
//        List<RankingDTO> ranking = comp.getParticipants().stream()
//                .<RankingDTO>map(user -> {
//                    // Filtramos os logs desta competição
//                    List<ReadingLog> compLogs = user.getLogs().stream()
//                            .filter(log -> log.getCompetition() != null && log.getCompetition().getId().equals(id))
//                            .toList();
//
//                    // SOMA DE PONTOS: Usamos Integer.valueOf() para evitar conflito de tipos
//                    Integer points = compLogs.stream()
//                            .mapToInt(log -> log.getPointsEarned() != null ? log.getPointsEarned() : 0)
//                            .sum();
//
//                    // SOMA DE PÁGINAS: .sum() retorna int, o Java converte para Integer sozinho
//                    Integer pages = compLogs.stream()
//                            .mapToInt(ReadingLog::getPagesRead)
//                            .sum();
//
//                    return new RankingDTO(user.getDisplayName(), points, pages);
//                })
//                // Aqui corrigimos o erro do u1/u2: especificamos que são RankingDTO
//                .sorted((RankingDTO r1, RankingDTO r2) -> r2.getTotalPoints().compareTo(r1.getTotalPoints()))
//                .toList();
//
//        model.addAttribute("competition", comp);
//        model.addAttribute("ranking", ranking);
//        return "ranking";
//    }

    @Autowired
    private RankingService rankingService;

    @GetMapping("/competicao/{id}/ranking")
    public String viewRanking(@PathVariable Long id, Model model) {
        Competition comp = competitionRepository.findById(id).orElseThrow();

        // Chama o serviço que acabamos de criar
        List<RankingDTO> ranking = rankingService.getRankingByCompetition(id);

        model.addAttribute("competition", comp);
        model.addAttribute("ranking", ranking);
        return "ranking";
    }
}