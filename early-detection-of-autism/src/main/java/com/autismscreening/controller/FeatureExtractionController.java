package com.autismscreening.controller;

import com.autismscreening.model.*;
import com.autismscreening.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/features")
@CrossOrigin(origins = "*")
public class FeatureExtractionController {

    @Autowired
    private CameraFeaturesRepository cameraFeaturesRepository;

    @Autowired
    private VoiceFeaturesRepository voiceFeaturesRepository;

    @Autowired
    private AssessmentSessionRepository sessionRepository;

    @Autowired
    private BehaviourFeaturesRepository behaviourFeaturesRepository;

    @PostMapping("/camera")
    public ResponseEntity<?> recordCameraFeatures(@RequestBody Map<String, Object> payload) {
        Long sessionId = Long.valueOf(payload.get("sessionId").toString());
        Optional<AssessmentSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Session not found");
        }

        CameraFeatures cf = new CameraFeatures();
        cf.setSession(sessionOpt.get());
        cf.setStageNumber(Integer.parseInt(payload.getOrDefault("stageNumber", 2).toString()));
        cf.setFaceDetectedRatio(Double.parseDouble(payload.getOrDefault("faceDetectedRatio", 0.85).toString()));
        cf.setHeadMovementVariability(Double.parseDouble(payload.getOrDefault("headMovementVariability", 0.15).toString()));
        cf.setGazeScreenDurationRatio(Double.parseDouble(payload.getOrDefault("gazeScreenDurationRatio", 0.80).toString()));
        cf.setGazeSwitchFrequency(Double.parseDouble(payload.getOrDefault("gazeSwitchFrequency", 4.0).toString()));
        cf.setImitationPoseSimilarity(Double.parseDouble(payload.getOrDefault("imitationPoseSimilarity", 0.75).toString()));
        cf.setMovementSmoothness(Double.parseDouble(payload.getOrDefault("movementSmoothness", 0.80).toString()));
        cf.setRepetitiveHandFrequency(Double.parseDouble(payload.getOrDefault("repetitiveHandFrequency", 1.2).toString()));
        cf.setRockingMotionDetected(Boolean.parseBoolean(payload.getOrDefault("rockingMotionDetected", false).toString()));
        cf.setResponseLatencyMs(Integer.parseInt(payload.getOrDefault("responseLatencyMs", 1100).toString()));

        CameraFeatures saved = cameraFeaturesRepository.save(cf);
        return ResponseEntity.ok(Map.of("status", "CAMERA_FEATURES_RECORDED", "id", saved.getCameraFeatureId()));
    }

    @PostMapping("/voice")
    public ResponseEntity<?> recordVoiceFeatures(@RequestBody Map<String, Object> payload) {
        Long sessionId = Long.valueOf(payload.get("sessionId").toString());
        Optional<AssessmentSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Session not found");
        }

        VoiceFeatures vf = new VoiceFeatures();
        vf.setSession(sessionOpt.get());
        vf.setStageNumber(Integer.parseInt(payload.getOrDefault("stageNumber", 6).toString()));
        vf.setVocalizationCount(Integer.parseInt(payload.getOrDefault("vocalizationCount", 3).toString()));
        vf.setSpeechDurationSeconds(Double.parseDouble(payload.getOrDefault("speechDurationSeconds", 4.5).toString()));
        vf.setResponseLatencyMs(Integer.parseInt(payload.getOrDefault("responseLatencyMs", 1300).toString()));
        vf.setPauseDurationAverage(Double.parseDouble(payload.getOrDefault("pauseDurationAverage", 1.2).toString()));
        vf.setWordCount(Integer.parseInt(payload.getOrDefault("wordCount", 4).toString()));
        vf.setTurnTakingSuccessRate(Double.parseDouble(payload.getOrDefault("turnTakingSuccessRate", 0.75).toString()));
        vf.setEcholaliaRepetitionIndex(Double.parseDouble(payload.getOrDefault("echolaliaRepetitionIndex", 0.10).toString()));
        vf.setAudioClarityScore(Double.parseDouble(payload.getOrDefault("audioClarityScore", 0.85).toString()));

        VoiceFeatures saved = voiceFeaturesRepository.save(vf);
        return ResponseEntity.ok(Map.of("status", "VOICE_FEATURES_RECORDED", "id", saved.getVoiceFeatureId()));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getSessionFeatures(@PathVariable Long sessionId) {
        Optional<AssessmentSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AssessmentSession session = sessionOpt.get();
        Map<String, Object> data = new HashMap<>();
        data.put("cameraFeatures", cameraFeaturesRepository.findBySession(session));
        data.put("voiceFeatures", voiceFeaturesRepository.findBySession(session));
        data.put("behaviourSummary", behaviourFeaturesRepository.findBySession(session).orElse(null));

        return ResponseEntity.ok(data);
    }
}

