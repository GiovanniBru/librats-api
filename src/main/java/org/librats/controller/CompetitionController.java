package org.librats.controller;

import org.librats.dto.RankingDTO;
import org.librats.model.Competition;
import org.librats.model.User;
import org.librats.repository.CompetitionRepository;
import org.librats.repository.UserRepository;
import org.librats.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class CompetitionController {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinCompetition(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Competition comp = competitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!comp.getParticipants().contains(user)) {
            comp.getParticipants().add(user);
            competitionRepository.save(comp);
            return ResponseEntity.ok("Você entrou na competição: " + comp.getName());
        }

        return ResponseEntity.badRequest().body("Você já está participando desta competição!");
    }

    @GetMapping("/{id}/ranking")
    public String verRanking(@PathVariable Long id, Model model) {
        // 1. Busca a competição para mostrar os detalhes no topo (Título, Autor, etc)
        Competition comp = competitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        // 2. Busca os dados do ranking processados pelo Service
        List<RankingDTO> ranking = rankingService.getRankingByCompetition(id);

        model.addAttribute("competition", comp);
        model.addAttribute("ranking", ranking);

        return "ranking"; // Nome do seu arquivo ranking.html
    }

    @GetMapping("/competicao/nova")
    public String mostrarFormulario(Model model) {
        model.addAttribute("competition", new Competition());
        return "nova-competicao";
    }

    @PostMapping("/competicao/salvar")
    public String salvar(@ModelAttribute Competition competition) {
        // Se o usuário não escolheu tipo, podemos definir um padrão
        if (competition.getType() == null) competition.setType("SINGLE_BOOK");

        competitionRepository.save(competition);
        return "redirect:/";
    }
}