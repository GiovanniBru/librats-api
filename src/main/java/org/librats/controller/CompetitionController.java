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

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
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
    public List<RankingDTO> getRanking(@PathVariable Long id) {
        // 1. Opcional: Você pode manter a busca se quiser validar que a competição existe
        competitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competition not found"));

        // 2. CORREÇÃO: Chama o método certo passando o ID (que já recebemos no @PathVariable)
        return rankingService.getRankingByCompetition(id);
    }
}