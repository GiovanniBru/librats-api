package org.librats.controller;

import org.librats.model.ReadingLog;
import org.librats.service.ReadingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ReadingLogController {

    @Autowired
    private ReadingLogService readingLogService;

    @PostMapping
    public ResponseEntity<ReadingLog> createLog(@RequestBody ReadingLog log) {
        // Chamamos o serviço que já tem a lógica de cálculo de pontos que fizemos ontem
        ReadingLog savedLog = readingLogService.logReading(log);
        return ResponseEntity.ok(savedLog);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadingLog>> getHistory(@PathVariable Long userId) {
        List<ReadingLog> history = readingLogService.getUserLogs(userId);
        return ResponseEntity.ok(history);
    }
}