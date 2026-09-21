package com.autismscreening.service;

import com.autismscreening.model.*;
import com.autismscreening.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MultimodalFusionService {

    @Autowired
    private BehaviourFeaturesRepository behaviourFeaturesRepository;

    @Autowired
    private CameraFeaturesRepository cameraFeaturesRepository;

    @Autowired
    private VoiceFeaturesRepository voiceFeaturesRepository;

    @Autowired
    private ParentQuestionnaireRepository questionnaireRepository;

    /**
     * Aggregates raw observations from camera, voice, and questionnaire into Categories A through J.
     */
    @Transactional
    public BehaviourFeatures computeAndSaveBehaviourFeatures(AssessmentSession session) {
        List<CameraFeatures> cameraList = cameraFeaturesRepository.findBySession(session);
        List<VoiceFeatures> voiceList = voiceFeaturesRepository.findBySession(session);
        Optional<ParentQuestionnaire> qOpt = questionnaireRepository.findTopByChildOrderByAssessmentDateDesc(session.getChild());

        // Default questionnaire baseline (normalized 0-100)
        double qSocial = 70.0, qComm = 70.0, qRepetitive = 20.0, qAttention = 70.0, qMotor = 75.0, qPlay = 75.0;
        if (qOpt.isPresent()) {
            ParentQuestionnaire q = qOpt.get();
            qSocial = ((q.getQ1SocialSmile() + q.getQ2EyeContact() + q.getQ3ResponseToName() + q.getQ5SharedEnjoyment()) / 8.0) * 100.0;
            qComm = ((q.getQ4PointingInterest() + q.getQ10LanguageMilestone()) / 4.0) * 100.0;
            qRepetitive = ((q.getQ7RepetitiveMovements() + q.getQ8UnusualSensoryInterests()) / 4.0) * 100.0;
            qAttention = ((q.getQ2EyeContact() + q.getQ3ResponseToName()) / 4.0) * 100.0;
            qPlay = ((q.getQ6Imitation() + q.getQ9PlayFlexibility()) / 4.0) * 100.0;
            qMotor = 80.0 - (qRepetitive * 0.3);
        }

        // Aggregate camera metrics
        double faceRatioSum = 0.0, gazeScreenSum = 0.0, imitationPoseSum = 0.0, smoothnessSum = 0.0;
        double repetitiveHandFreqSum = 0.0, cameraLatencySum = 0.0;
        boolean rockingObserved = false;
        int camCount = cameraList.size();

        for (CameraFeatures cf : cameraList) {
            faceRatioSum += cf.getFaceDetectedRatio();
            gazeScreenSum += cf.getGazeScreenDurationRatio();
            imitationPoseSum += cf.getImitationPoseSimilarity();
            smoothnessSum += cf.getMovementSmoothness();
            repetitiveHandFreqSum += cf.getRepetitiveHandFrequency();
            cameraLatencySum += cf.getResponseLatencyMs();
            if (Boolean.TRUE.equals(cf.getRockingMotionDetected())) {
                rockingObserved = true;
            }
        }

        double avgFaceRatio = camCount > 0 ? (faceRatioSum / camCount) : 0.8;
        double avgGazeScreen = camCount > 0 ? (gazeScreenSum / camCount) : 0.75;
        double avgImitationPose = camCount > 0 ? (imitationPoseSum / camCount) : 0.70;
        double avgSmoothness = camCount > 0 ? (smoothnessSum / camCount) : 0.75;
        double avgRepetitiveFreq = camCount > 0 ? (repetitiveHandFreqSum / camCount) : 1.5;
        double avgCameraLatencyMs = camCount > 0 ? (cameraLatencySum / camCount) : 1200.0;

        // Aggregate voice metrics
        int totalVocals = 0, totalWords = 0;
        double totalSpeechDuration = 0.0, voiceLatencySum = 0.0, echolaliaSum = 0.0, turnTakingSum = 0.0;
        int voiceCount = voiceList.size();

        for (VoiceFeatures vf : voiceList) {
            totalVocals += vf.getVocalizationCount();
            totalWords += vf.getWordCount();
            totalSpeechDuration += vf.getSpeechDurationSeconds();
            voiceLatencySum += vf.getResponseLatencyMs();
            echolaliaSum += vf.getEcholaliaRepetitionIndex();
            turnTakingSum += vf.getTurnTakingSuccessRate();
        }

        double avgVoiceLatencyMs = voiceCount > 0 ? (voiceLatencySum / voiceCount) : 1400.0;
        double avgTurnTaking = voiceCount > 0 ? (turnTakingSum / voiceCount) : 0.75;
        double avgEcholalia = voiceCount > 0 ? (echolaliaSum / voiceCount) : 0.15;

        // Compute Numerical Categories A through J (0.0 to 100.0 scale)
        // Category A: Social Interaction (gaze + face tracking + questionnaire + turn taking)
        double scoreA = clamp((qSocial * 0.4) + (avgFaceRatio * 30.0) + (avgTurnTaking * 30.0));

        // Category B: Communication (vocal count + words + questionnaire + turn taking)
        double commVocalIndex = Math.min(100.0, (totalWords * 10.0 + totalVocals * 5.0));
        double scoreB = clamp((qComm * 0.4) + (commVocalIndex * 0.3) + ((1.0 - avgEcholalia) * 30.0));

        // Category C: Attention (gaze screen ratio + face ratio + questionnaire)
        double scoreC = clamp((qAttention * 0.35) + (avgGazeScreen * 45.0) + (avgFaceRatio * 20.0));

        // Category D: Motor Behaviour (smoothness + hand stillness + questionnaire)
        double motorSteadiness = Math.max(0.0, 100.0 - (avgRepetitiveFreq * 10.0));
        double scoreD = clamp((qMotor * 0.3) + (avgSmoothness * 40.0) + (motorSteadiness * 0.3));

        // Category E: Imitation (pose similarity + questionnaire imitation)
        double scoreE = clamp((avgImitationPose * 65.0) + (qPlay * 0.35));

        // Category F: Play Behaviour (flexibility + task engagement)
        double scoreF = clamp((qPlay * 0.5) + (avgSmoothness * 25.0) + (avgGazeScreen * 25.0));

        // Category G: Repetitive Behaviour (Higher indicates more concern: hand flapping, rocking, echolalia)
        double repScore = (qRepetitive * 0.35) + (avgRepetitiveFreq * 8.0) + (avgEcholalia * 30.0) + (rockingObserved ? 20.0 : 0.0);
        double scoreG = clamp(repScore);

        // Category H: Response Latency (Promptness: <800ms -> 95+, 1500ms -> 70, 3000ms -> 40)
        double meanLatency = (avgCameraLatencyMs + avgVoiceLatencyMs) / 2.0;
        double scoreH = clamp(110.0 - (meanLatency / 35.0));

        // Category I: Voice/Communication Metric (audio engagement)
        double scoreI = clamp((avgTurnTaking * 50.0) + (Math.min(1.0, totalSpeechDuration / 15.0) * 30.0) + ((1.0 - avgEcholalia) * 20.0));

        // Category J: Camera-derived Movement Metric
        double scoreJ = clamp((avgSmoothness * 50.0) + (avgFaceRatio * 30.0) + (avgGazeScreen * 20.0));

        BehaviourFeatures bf = behaviourFeaturesRepository.findBySession(session)
                .orElse(new BehaviourFeatures());

        bf.setSession(session);
        bf.setSocialInteractionScore(round(scoreA));
        bf.setCommunicationScore(round(scoreB));
        bf.setAttentionScore(round(scoreC));
        bf.setMotorBehaviourScore(round(scoreD));
        bf.setImitationScore(round(scoreE));
        bf.setPlayBehaviourScore(round(scoreF));
        bf.setRepetitiveBehaviourScore(round(scoreG));
        bf.setResponseLatencyScore(round(scoreH));
        bf.setVoiceMetricScore(round(scoreI));
        bf.setCameraMovementScore(round(scoreJ));

        return behaviourFeaturesRepository.save(bf);
    }

    /**
     * Evaluates Multimodal Fusion across the 6 configurations requested:
     * 1. Questionnaire only
     * 2. Camera features only
     * 3. Voice features only
     * 4. Camera + Voice
     * 5. Questionnaire + Camera + Voice
     * 6. Full Multimodal Model
     */
    public Map<String, Double> computeModalityFusionAblation(BehaviourFeatures bf, ParentQuestionnaire q) {
        Map<String, Double> ablationScores = new LinkedHashMap<>();

        // Normalized risk metrics (0.0 to 1.0, where higher indicates higher observed concern)
        double qRisk = (q != null && q.getTotalScore() != null) ? (1.0 - (q.getTotalScore() / 20.0)) : 0.25;

        // Camera risk: inversely related to attention, imitation, camera movement, positively related to repetitive
        double camRisk = clamp01(1.0 - ((bf.getAttentionScore() * 0.3 + bf.getImitationScore() * 0.35 + bf.getCameraMovementScore() * 0.35) / 100.0)
                + (bf.getRepetitiveBehaviourScore() / 250.0));

        // Voice risk: inversely related to communication and voice metric
        double voiceRisk = clamp01(1.0 - ((bf.getCommunicationScore() * 0.5 + bf.getVoiceMetricScore() * 0.5) / 100.0));

        // 1. Questionnaire only
        ablationScores.put("1_QUESTIONNAIRE_ONLY", round3(qRisk));

        // 2. Camera features only
        ablationScores.put("2_CAMERA_ONLY", round3(camRisk));

        // 3. Voice features only
        ablationScores.put("3_VOICE_ONLY", round3(voiceRisk));

        // 4. Camera + Voice
        double camVoice = (camRisk * 0.55) + (voiceRisk * 0.45);
        ablationScores.put("4_CAMERA_PLUS_VOICE", round3(camVoice));

        // 5. Questionnaire + Camera + Voice
        double qCamVoice = (qRisk * 0.30) + (camRisk * 0.40) + (voiceRisk * 0.30);
        ablationScores.put("5_QUESTIONNAIRE_CAMERA_VOICE", round3(qCamVoice));

        // 6. Full Multimodal Model (integrates all 10 categories A-J + interaction latency)
        double positiveBehaviors = (bf.getSocialInteractionScore() + bf.getCommunicationScore() +
                bf.getAttentionScore() + bf.getMotorBehaviourScore() +
                bf.getImitationScore() + bf.getPlayBehaviourScore() +
                bf.getResponseLatencyScore()) / 7.0;
        double fullRisk = clamp01(1.0 - (positiveBehaviors / 100.0) + (bf.getRepetitiveBehaviourScore() / 200.0));
        ablationScores.put("6_FULL_MULTIMODAL", round3(fullRisk));

        return ablationScores;
    }

    private double clamp(double val) {
        return Math.max(0.0, Math.min(100.0, val));
    }

    private double clamp01(double val) {
        return Math.max(0.0, Math.min(1.0, val));
    }

    private double round(double val) {
        return Math.round(val * 10.0) / 10.0;
    }

    private double round3(double val) {
        return Math.round(val * 1000.0) / 1000.0;
    }
}

