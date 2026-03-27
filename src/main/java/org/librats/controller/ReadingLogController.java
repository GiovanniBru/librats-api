package org.librats.controller;

import org.librats.model.User;
import org.librats.repository.UserRepository;
import org.librats.model.ReadingLog;
import org.librats.service.ReadingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ReadingLogController {

    @Autowired
    private ReadingLogService readingLogService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ReadingLog> createLog(
            @RequestBody ReadingLog log,
            @AuthenticationPrincipal UserDetails userDetails) { // Pega o usuário da sessão

        // 1. Busca o objeto User real do banco usando o username da sessão
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

        // 2. Força o log a pertencer ao usuário logado (Segurança!)
        log.setUser(currentUser);

        ReadingLog savedLog = readingLogService.logReading(log);
        return ResponseEntity.ok(savedLog);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadingLog>> getHistory(@PathVariable Long userId) {
        List<ReadingLog> history = readingLogService.getUserLogs(userId);
        return ResponseEntity.ok(history);
    }
}