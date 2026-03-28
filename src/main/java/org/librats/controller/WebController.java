package org.librats.controller;

import org.librats.model.ReadingLog;
import org.librats.model.User;
import org.librats.repository.BookRepository;
import org.librats.repository.CompetitionRepository;
import org.librats.repository.ReadingLogRepository;
import org.librats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}