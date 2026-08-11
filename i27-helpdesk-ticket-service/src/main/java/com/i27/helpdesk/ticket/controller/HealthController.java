package com.i27.helpdesk.ticket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/healthz")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(
            Map.of("status", "UP")
        );
    }

    @GetMapping("/readyz")
    public ResponseEntity<?> ready() {
        // later we can add DB checks here
        return ResponseEntity.ok(
            Map.of("status", "READY")
        );
    }
}
