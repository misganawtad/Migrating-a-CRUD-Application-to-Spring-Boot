package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminHomeRestController {

    @GetMapping
    public ResponseEntity<?> adminHome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Admin Panel");
        response.put("redirect", "/api/admin/users");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> adminDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin Dashboard");
        response.put("stats", Map.of(
                "totalUsers", 100,
                "activeUsers", 85,
                "adminUsers", 5
        ));
        return ResponseEntity.ok(response);
    }
}