package com.autismscreening.controller;

import com.autismscreening.model.Child;
import com.autismscreening.model.ParentQuestionnaire;
import com.autismscreening.repository.ChildRepository;
import com.autismscreening.repository.ParentQuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/questionnaire")
@CrossOrigin(origins = "*")
public class QuestionnaireController {

    @Autowired
    private ParentQuestionnaireRepository questionnaireRepository;

    @Autowired
    private ChildRepository childRepository;

    @GetMapping("/child/{childId}")
    public ResponseEntity<?> getLatestQuestionnaire(@PathVariable Long childId) {
        Optional<Child> childOpt = childRepository.findById(childId);
        if (childOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return questionnaireRepository.findTopByChildOrderByAssessmentDateDesc(childOpt.get())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<?> submitQuestionnaire(@RequestBody Map<String, Object> payload) {
        Long childId = Long.valueOf(payload.get("childId").toString());
        Optional<Child> childOpt = childRepository.findById(childId);
        if (childOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Child not found");
        }

        Child child = childOpt.get();
        ParentQuestionnaire q = new ParentQuestionnaire();
        q.setChild(child);
        q.setAssessmentDate(LocalDate.now());

        q.setQ1SocialSmile(Integer.parseInt(payload.getOrDefault("q1SocialSmile", 1).toString()));
        q.setQ2EyeContact(Integer.parseInt(payload.getOrDefault("q2EyeContact", 1).toString()));
        q.setQ3ResponseToName(Integer.parseInt(payload.getOrDefault("q3ResponseToName", 1).toString()));
        q.setQ4PointingInterest(Integer.parseInt(payload.getOrDefault("q4PointingInterest", 1).toString()));
        q.setQ5SharedEnjoyment(Integer.parseInt(payload.getOrDefault("q5SharedEnjoyment", 1).toString()));
        q.setQ6Imitation(Integer.parseInt(payload.getOrDefault("q6Imitation", 1).toString()));
        q.setQ7RepetitiveMovements(Integer.parseInt(payload.getOrDefault("q7RepetitiveMovements", 0).toString()));
        q.setQ8UnusualSensoryInterests(Integer.parseInt(payload.getOrDefault("q8UnusualSensoryInterests", 0).toString()));
        q.setQ9PlayFlexibility(Integer.parseInt(payload.getOrDefault("q9PlayFlexibility", 1).toString()));
        q.setQ10LanguageMilestone(Integer.parseInt(payload.getOrDefault("q10LanguageMilestone", 1).toString()));

        // Positive items (high is good): q1, q2, q3, q4, q5, q6, q9, q10 (max 16)
        // Inverted items (high indicates repetitive concerns): q7, q8 (max 4)
        // Adjusted score out of 20:
        double positiveSum = q.getQ1SocialSmile() + q.getQ2EyeContact() + q.getQ3ResponseToName()
                + q.getQ4PointingInterest() + q.getQ5SharedEnjoyment() + q.getQ6Imitation()
                + q.getQ9PlayFlexibility() + q.getQ10LanguageMilestone();
        double invertedSum = (2 - q.getQ7RepetitiveMovements()) + (2 - q.getQ8UnusualSensoryInterests());
        double totalScore = positiveSum + invertedSum;
        q.setTotalScore(totalScore);

        String indicator;
        if (totalScore >= 15.0) {
            indicator = "LOW";
        } else if (totalScore >= 10.0) {
            indicator = "MODERATE";
        } else {
            indicator = "ELEVATED";
        }
        q.setQuestionnaireRiskIndicator(indicator);

        ParentQuestionnaire saved = questionnaireRepository.save(q);

        Map<String, Object> response = new HashMap<>();
        response.put("questionnaireId", saved.getQuestionnaireId());
        response.put("totalScore", saved.getTotalScore());
        response.put("questionnaireRiskIndicator", saved.getQuestionnaireRiskIndicator());
        response.put("message", "Stage 1 Questionnaire Recorded. Note: Questionnaire alone does not decide autism status. Proceeding to interactive assessment.");

        return ResponseEntity.ok(response);
    }
}

