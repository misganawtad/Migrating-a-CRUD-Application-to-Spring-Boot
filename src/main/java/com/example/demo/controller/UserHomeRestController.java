package com.example.demo.controller;

import com.example.demo.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserHomeRestController {

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User loggedInUser) {
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        return ResponseEntity.ok(loggedInUser);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(@AuthenticationPrincipal User loggedInUser) {
        return ResponseEntity.ok(Map.of(
                "user", loggedInUser,
                "message", "Welcome to your dashboard"
        ));
    }
}