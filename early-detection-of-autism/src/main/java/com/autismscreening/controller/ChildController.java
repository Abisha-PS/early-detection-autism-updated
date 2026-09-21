package com.autismscreening.controller;

import com.autismscreening.model.Child;
import com.autismscreening.model.User;
import com.autismscreening.repository.ChildRepository;
import com.autismscreening.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*")
public class ChildController {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Child>> getAllChildren() {
        return ResponseEntity.ok(childRepository.findAll());
    }

    @GetMapping("/{childId}")
    public ResponseEntity<?> getChildById(@PathVariable Long childId) {
        return childRepository.findById(childId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getChildrenByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(childRepository.findByParent(userOpt.get()));
        }
        return ResponseEntity.ok(childRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createChild(@RequestBody Map<String, Object> payload) {
        Object userIdValue = payload.get("userId");
        if (userIdValue == null) {
            return ResponseEntity.badRequest().body("A registered userId is required");
        }
        Optional<User> parentOpt = userRepository.findById(Long.valueOf(userIdValue.toString()));
        if (parentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Parent user not found");
        }
        User parent = parentOpt.get();

        int ageMonths = Integer.parseInt(payload.getOrDefault("ageMonths", 36).toString());
        String gender = payload.getOrDefault("gender", "MALE").toString();
        String language = payload.getOrDefault("preferredLanguage", "en").toString();
        String concerns = payload.getOrDefault("developmentalConcerns", "").toString();
        String commLevel = payload.getOrDefault("communicationLevel", "SINGLE_WORDS").toString();
        String prevNotes = payload.getOrDefault("previousAssessmentNotes", "").toString();

        String anonymousCode = "CH-" + (2026) + "-" + (1000 + new Random().nextInt(9000));

        Child child = new Child(anonymousCode, parent, ageMonths, gender, language, concerns, commLevel, prevNotes);
        Child saved = childRepository.save(child);
        return ResponseEntity.ok(saved);
    }
}

