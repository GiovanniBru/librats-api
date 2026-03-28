package org.librats.controller;

import org.librats.model.ReadingLog;
import org.librats.model.User;
import org.librats.repository.UserRepository;
import org.librats.service.ReadingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller; // Usamos Controller para permitir Redirect
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/logs")
public class ReadingLogController {

    @Autowired
    private ReadingLogService readingLogService;

    @Autowired
    private UserRepository userRepository;

    // 1. O MÉTODO DO FORMULÁRIO (Retorna um redirecionamento de página)
    @PostMapping
    public String createLog(
            @ModelAttribute ReadingLog log,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

        log.setUser(currentUser);
        readingLogService.logReading(log);

        // Redireciona para a home após salvar
        return "redirect:/";
    }

    // 2. O MÉTODO DO HISTÓRICO (Precisa do @ResponseBody para retornar JSON)
    @GetMapping("/user/{userId}")
    @ResponseBody // <--- ISSO resolve o erro do ResponseEntity!
    public ResponseEntity<List<ReadingLog>> getHistory(@PathVariable Long userId) {
        List<ReadingLog> history = readingLogService.getUserLogs(userId);
        return ResponseEntity.ok(history);
    }
}