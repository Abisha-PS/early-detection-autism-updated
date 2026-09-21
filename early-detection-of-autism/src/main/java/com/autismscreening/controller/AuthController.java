package com.autismscreening.controller;

import com.autismscreening.model.User;
import com.autismscreening.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password == null || !password.equals(user.getPasswordHash())) {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
            Map<String, Object> resp = new HashMap<>();
            resp.put("userId", user.getUserId());
            resp.put("username", user.getUsername());
            resp.put("fullName", user.getFullName());
            resp.put("role", user.getRole());
            resp.put("consentGiven", user.getConsentGiven());
            return ResponseEntity.ok(resp);
        }

        return ResponseEntity.status(401).body("User not found. Please register before starting an assessment.");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email = payload.get("email");
        String password = payload.get("password");
        String fullName = payload.get("fullName");
        if (username == null || email == null || password == null || fullName == null
                || username.isBlank() || email.isBlank() || password.isBlank() || fullName.isBlank()) {
            return ResponseEntity.badRequest().body("Username, email, password, and full name are required");
        }
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body("Username or email is already registered");
        }
        User newUser = new User(username, email, password, fullName, "PARENT", false);
        userRepository.save(newUser);
        Map<String, Object> resp = new HashMap<>();
        resp.put("userId", newUser.getUserId());
        resp.put("username", newUser.getUsername());
        resp.put("fullName", newUser.getFullName());
        resp.put("role", newUser.getRole());
        resp.put("consentGiven", newUser.getConsentGiven());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/consent")
    public ResponseEntity<?> recordConsent(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            u.setConsentGiven(true);
            userRepository.save(u);
            return ResponseEntity.ok(Map.of("status", "CONSENT_RECORDED", "userId", u.getUserId()));
        }
        return ResponseEntity.badRequest().body("User not found");
    }
}

