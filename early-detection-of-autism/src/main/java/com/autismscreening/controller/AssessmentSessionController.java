package com.autismscreening.controller;

import com.autismscreening.model.*;
import com.autismscreening.repository.*;
import com.autismscreening.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class AssessmentSessionController {

    @Autowired
    private AssessmentSessionRepository sessionRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ActivityRecordRepository activityRecordRepository;

    @Autowired
    private ParentQuestionnaireRepository questionnaireRepository;

    @Autowired
    private MultimodalFusionService multimodalFusionService;

    @Autowired
    private MLClassificationService mlClassificationService;

    @Autowired
    private GuidanceRecommendationService guidanceRecommendationService;

    @Autowired
    private ProgressRecordRepository progressRecordRepository;

    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody Map<String, Object> payload) {
        Long childId = Long.valueOf(payload.get("childId").toString());
        Optional<Child> childOpt = childRepository.findById(childId);
        if (childOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Child not found");
        }

        Child child = childOpt.get();
        long prevCount = sessionRepository.countByChild(child);
        int sessionSeq = (int) prevCount + 1;

        AssessmentSession session = new AssessmentSession(child, sessionSeq);
        AssessmentSession saved = sessionRepository.save(session);

        return ResponseEntity.ok(Map.of(
            "sessionId", saved.getSessionId(),
            "childId", child.getChildId(),
            "anonymousCode", child.getAnonymousCode(),
            "sessionNumber", saved.getSessionNumber(),
            "status", saved.getStatus()
        ));
    }

    @PostMapping("/{sessionId}/activity")
    public ResponseEntity<?> recordActivity(@PathVariable Long sessionId, @RequestBody Map<String, Object> payload) {
        Optional<AssessmentSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AssessmentSession session = sessionOpt.get();
        int stageNumber = Integer.parseInt(payload.getOrDefault("stageNumber", 2).toString());
        String stageName = payload.getOrDefault("stageName", "ACTIVITY_STAGE").toString();
        int duration = Integer.parseInt(payload.getOrDefault("durationSeconds", 15).toString());
        int interactions = Integer.parseInt(payload.getOrDefault("interactionCount", 5).toString());

        ActivityRecord record = new ActivityRecord(session, stageNumber, stageName);
        record.setCompleted(true);
        record.setDurationSeconds(duration);
        record.setInteractionCount(interactions);

        activityRecordRepository.save(record);

        return ResponseEntity.ok(Map.of("status", "ACTIVITY_RECORDED", "stageNumber", stageNumber));
    }

    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<?> completeSession(@PathVariable Long sessionId) {
        Optional<AssessmentSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AssessmentSession session = sessionOpt.get();
        session.setStatus("COMPLETED");
        session.setCompletedAt(LocalDateTime.now());
        sessionRepository.save(session);

        // Compute categories A through J
        BehaviourFeatures bf = multimodalFusionService.computeAndSaveBehaviourFeatures(session);

        // Fetch questionnaire if available
        ParentQuestionnaire q = questionnaireRepository.findTopByChildOrderByAssessmentDateDesc(session.getChild()).orElse(null);

        // Execute ML evaluation
        MLResult mlResult = mlClassificationService.evaluateScreening(session, bf, q);

        // Generate supportive guidance
        guidanceRecommendationService.generatePersonalizedGuidance(session, bf);

        // Save progress record for longitudinal monitoring
        ProgressRecord pr = new ProgressRecord();
        pr.setChild(session.getChild());
        pr.setSession(session);
        pr.setSessionSequence(session.getSessionNumber());
        pr.setRecordDate(LocalDate.now());
        pr.setOverallRiskScore(mlResult.getRiskScore());
        pr.setAttentionTrend(bf.getAttentionScore());
        pr.setCommunicationTrend(bf.getCommunicationScore());
        pr.setSocialTrend(bf.getSocialInteractionScore());
        pr.setMotorTrend(bf.getMotorBehaviourScore());
        pr.setRepetitiveTrend(bf.getRepetitiveBehaviourScore());
        pr.setPlayTrend(bf.getPlayBehaviourScore());
        pr.setNotes("Assessment session #" + session.getSessionNumber() + " completed.");
        progressRecordRepository.save(pr);

        Map<String, Object> resp = new HashMap<>();
        resp.put("sessionId", session.getSessionId());
        resp.put("sessionNumber", session.getSessionNumber());
        resp.put("status", "COMPLETED");
        resp.put("screeningCategory", mlResult.getScreeningCategory());
        resp.put("riskScore", mlResult.getRiskScore());
        resp.put("confidence", mlResult.getModelConfidence());
        resp.put("childFriendlyLabel", "Activity Completed! Great Job! 🌟");

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSession(@PathVariable Long sessionId) {
        return sessionRepository.findById(sessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<?> getSessionsForChild(@PathVariable Long childId) {
        return childRepository.findById(childId)
                .map(child -> ResponseEntity.ok(sessionRepository.findByChildOrderBySessionDateDesc(child)))
                .orElse(ResponseEntity.notFound().build());
    }
}

