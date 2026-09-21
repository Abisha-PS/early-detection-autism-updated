package com.autismscreening.controller;

import com.autismscreening.model.*;
import com.autismscreening.repository.*;
import com.autismscreening.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ScreeningResultController {

    @Autowired
    private AssessmentSessionRepository sessionRepository;

    @Autowired
    private MLResultRepository mlResultRepository;

    @Autowired
    private BehaviourFeaturesRepository behaviourFeaturesRepository;

    @Autowired
    private ParentQuestionnaireRepository questionnaireRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private ADOSValidationService adosValidationService;

    @Autowired
    private MultimodalFusionService multimodalFusionService;

    @Autowired
    private MLClassificationService mlClassificationService;

    @Autowired
    private GuidanceRecommendationService guidanceRecommendationService;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getScreeningResults(@PathVariable Long sessionId) {
        Optional<AssessmentSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AssessmentSession session = sessionOpt.get();
        Child child = session.getChild();

        Optional<MLResult> mlOpt = mlResultRepository.findBySession(session);
        Optional<BehaviourFeatures> bfOpt = behaviourFeaturesRepository.findBySession(session);
        Optional<ParentQuestionnaire> qOpt = questionnaireRepository.findTopByChildOrderByAssessmentDateDesc(child);
        BehaviourFeatures bf = bfOpt.orElseGet(() -> multimodalFusionService.computeAndSaveBehaviourFeatures(session));
        MLResult ml = mlOpt.orElseGet(() -> mlClassificationService.evaluateScreening(session, bf, qOpt.orElse(null)));
        List<Recommendation> recs = recommendationRepository.findBySession(session);
        if (recs.isEmpty()) {
            recs = guidanceRecommendationService.generatePersonalizedGuidance(session, bf);
        }

        // 6-modality ablation comparison
        Map<String, Double> ablation = multimodalFusionService.computeModalityFusionAblation(bf, qOpt.orElse(null));

        // ADOS-2 Clinical reference comparison
        Map<String, Object> adosValidation = adosValidationService.compareWithClinicalReference(child, ml);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sessionId", session.getSessionId());
        payload.put("sessionNumber", session.getSessionNumber());
        payload.put("sessionDate", session.getSessionDate().toString());
        payload.put("child", Map.of(
            "childId", child.getChildId(),
            "anonymousCode", child.getAnonymousCode(),
            "ageMonths", child.getAgeMonths(),
            "gender", child.getGender(),
            "preferredLanguage", child.getPreferredLanguage(),
            "communicationLevel", child.getCommunicationLevel()
        ));

        // Child friendly display (Stage 14)
        payload.put("childFriendlyDisplay", Map.of(
            "title", "Great Job! 🌟",
            "message", "Activity Completed! Thank You!",
            "starsEarned", 5
        ));

        // Clinical / Parent Dashboard display (Stage 15)
        payload.put("screeningOutcome", Map.of(
            "riskScore", ml.getRiskScore(),
            "screeningCategory", ml.getScreeningCategory(),
            "confidence", ml.getModelConfidence(),
            "modelName", ml.getModelName(),
            "featureImportance", ml.getFeatureImportanceSummary(),
            "disclaimer", ml.getDisclaimerNote()
        ));

        // Numerical Behavioural Matrix (Stage 10: Categories A-J)
        payload.put("behaviouralCategories", Map.of(
            "socialInteraction", bf.getSocialInteractionScore(),
            "communication", bf.getCommunicationScore(),
            "attention", bf.getAttentionScore(),
            "motorBehaviour", bf.getMotorBehaviourScore(),
            "imitation", bf.getImitationScore(),
            "playBehaviour", bf.getPlayBehaviourScore(),
            "repetitiveBehaviour", bf.getRepetitiveBehaviourScore(),
            "responseLatency", bf.getResponseLatencyScore(),
            "voiceMetric", bf.getVoiceMetricScore(),
            "cameraMovement", bf.getCameraMovementScore()
        ));

        // Multimodal Fusion Ablation Study (Stage 12)
        payload.put("multimodalFusionAblation", ablation);

        // ADOS-2 Clinical Comparison (Stage 13)
        payload.put("clinicalReferenceComparison", adosValidation);

        // Personalized Guidance (Stage 16)
        payload.put("recommendations", recs);

        return ResponseEntity.ok(payload);
    }
}

