package org.librats.controller;

import org.librats.dto.RankingDTO;
import org.librats.model.Competition;
import org.librats.repository.CompetitionRepository;
import org.librats.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private CompetitionRepository competitionRepository;

    @GetMapping("/{id}/ranking")
    public List<RankingDTO> getRanking(@PathVariable Long id) {
        Competition comp = competitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competition not found"));
        return rankingService.getCompetitionRanking(comp);
    }
}