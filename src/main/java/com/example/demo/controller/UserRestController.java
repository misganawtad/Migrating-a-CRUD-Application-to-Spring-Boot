package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService service;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserRestController(UserService service, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = service.findById(id);
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> payload) {
        try {
            User user = new User();
            user.setFirstName((String) payload.get("firstName"));
            user.setLastName((String) payload.get("lastName"));
            user.setAge((Integer) payload.get("age"));
            user.setEmail((String) payload.get("email"));
            // Encode password
            String rawPassword = (String) payload.get("password");
            user.setPassword(passwordEncoder.encode(rawPassword));
            // Set role
            String roleName = (String) payload.get("roleName");
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));
            user.getRoles().add(role);
            User savedUser = service.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists"));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody Map<String, Object> payload) {
        try {
            User existing = service.findById(id);
            if (existing == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }


            if (payload.containsKey("firstName")) {
                existing.setFirstName((String) payload.get("firstName"));
            }
            if (payload.containsKey("lastName")) {
                existing.setLastName((String) payload.get("lastName"));
            }
            if (payload.containsKey("age")) {
                existing.setAge((Integer) payload.get("age"));
            }
            if (payload.containsKey("email")) {
                existing.setEmail((String) payload.get("email"));
            }


            if (payload.containsKey("password")) {
                String password = (String) payload.get("password");
                if (password != null && !password.isBlank()) {
                    existing.setPassword(passwordEncoder.encode(password));
                }
            }

            if (payload.containsKey("roleName")) {
                String roleName = (String) payload.get("roleName");
                existing.getRoles().clear();
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));
                existing.getRoles().add(role);
            }

            User updatedUser = service.save(existing);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User existing = service.findById(id);
        if (existing == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        service.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }
}