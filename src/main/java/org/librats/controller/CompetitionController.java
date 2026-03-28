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
        Competition comp = competitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competition not found"));
        return rankingService.getCompetitionRanking(comp);
    }
}