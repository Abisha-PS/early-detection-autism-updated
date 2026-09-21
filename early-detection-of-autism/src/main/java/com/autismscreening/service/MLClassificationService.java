package com.autismscreening.service;

import com.autismscreening.model.*;
import com.autismscreening.ml.RandomForestScreeningModel;
import com.autismscreening.repository.MLResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

@Service
public class MLClassificationService {

    @Autowired
    private MLResultRepository mlResultRepository;

    @Autowired
    private MultimodalFusionService multimodalFusionService;

    /**
     * Executes ML Classification on multimodal behavioural features.
     * Prioritizes Sensitivity / Recall so potentially concerning cases are not overlooked.
     */
    @Transactional
    public MLResult evaluateScreening(AssessmentSession session, BehaviourFeatures bf, ParentQuestionnaire q) {
        Map<String, Double> fusionScores = multimodalFusionService.computeModalityFusionAblation(bf, q);
        double fusionRisk = fusionScores.getOrDefault("6_FULL_MULTIMODAL", 0.30);

        // Actual Random Forest inference on six fused behavioural features.
        // The prototype forest is trained on synthetic/demo calibration data.
        RandomForestScreeningModel rf = new RandomForestScreeningModel();
        double[] features = {
                safe(bf.getSocialInteractionScore()),
                safe(bf.getCommunicationScore()),
                safe(bf.getAttentionScore()),
                safe(bf.getMotorBehaviourScore()),
                safe(bf.getImitationScore()),
                safe(bf.getRepetitiveBehaviourScore())
        };
        RandomForestScreeningModel.Prediction prediction = rf.predict(features);

        // RF probability is the primary model signal; multimodal fusion is a
        // secondary consistency check. This is a screening indicator, not diagnosis.
        double finalRiskScore = round3((prediction.elevatedConcernProbability * 0.70) + (fusionRisk * 0.30));

        String screeningCategory;
        if (finalRiskScore < 0.35) {
            screeningCategory = "LOWER_OBSERVED_CONCERN";
        } else if (finalRiskScore < 0.65) {
            screeningCategory = "MODERATE_OBSERVED_CONCERN";
        } else {
            screeningCategory = "HIGHER_OBSERVED_CONCERN";
        }

        // Agreement among trees is a transparent prototype confidence signal.
        double treeAgreement = Math.max(prediction.elevatedConcernProbability,
                1.0 - prediction.elevatedConcernProbability);
        double completeness = 0.75; // live sensor fallback is possible in browser
        double confidence = Math.min(0.95, Math.max(0.50, 0.50 + 0.30 * treeAgreement + 0.15 * completeness));

        double socialDeficit = (100.0 - features[0]) / 100.0;
        double commDeficit = (100.0 - features[1]) / 100.0;
        double attentionDeficit = (100.0 - features[2]) / 100.0;
        double repetitiveWeight = features[5] / 100.0;
        double imitationDeficit = (100.0 - features[4]) / 100.0;

        String featureImportance = String.format(
            Locale.US,
            "{\"social_interaction\": %.2f, \"attention_gaze\": %.2f, \"communication\": %.2f, \"repetitive_motion\": %.2f, \"imitation_accuracy\": %.2f}",
            socialDeficit, attentionDeficit, commDeficit, repetitiveWeight, imitationDeficit
        );

        String disclaimer = "This is an AI-assisted behavioural screening prototype. " +
                "The Random Forest calibration data are synthetic/demo data and the model is not clinically validated. " +
                "CNN/vision features are used as a planned visual feature-extraction layer; they do not independently diagnose autism. " +
                "ADOS-2 is a standardized clinical assessment administered by trained professionals and is not replaced by this system. " +
                "Use the result only to support discussion with a qualified developmental professional.";

        MLResult mlResult = mlResultRepository.findBySession(session).orElse(new MLResult());
        mlResult.setSession(session);
        mlResult.setModelName("CNN_Visual_Features + RandomForest_Screening");
        mlResult.setModalityConfiguration("FULL_MULTIMODAL");
        mlResult.setRiskScore(finalRiskScore);
        mlResult.setScreeningCategory(screeningCategory);
        mlResult.setModelConfidence(confidence);
        mlResult.setFeatureImportanceSummary(featureImportance);
        mlResult.setDisclaimerNote(disclaimer);
        return mlResultRepository.save(mlResult);
    }

    private double safe(Double value) {
        return value == null ? 50.0 : Math.max(0.0, Math.min(100.0, value));
    }

    private double round3(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }

}

