package com.autismscreening.service;

import com.autismscreening.model.BehaviourFeatures;
import com.autismscreening.model.ParentQuestionnaire;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MultimodalFusionServiceTest {

    @Test
    void testComputeModalityFusionAblationHasAllSixConfigurations() {
        MultimodalFusionService service = new MultimodalFusionService();

        BehaviourFeatures bf = new BehaviourFeatures();
        bf.setSocialInteractionScore(75.0);
        bf.setCommunicationScore(70.0);
        bf.setAttentionScore(80.0);
        bf.setMotorBehaviourScore(85.0);
        bf.setImitationScore(78.0);
        bf.setPlayBehaviourScore(82.0);
        bf.setRepetitiveBehaviourScore(15.0);
        bf.setResponseLatencyScore(72.0);
        bf.setVoiceMetricScore(68.0);
        bf.setCameraMovementScore(75.0);

        ParentQuestionnaire q = new ParentQuestionnaire();
        q.setTotalScore(18.0);

        Map<String, Double> ablation = service.computeModalityFusionAblation(bf, q);

        assertNotNull(ablation);
        assertEquals(6, ablation.size());
        assertTrue(ablation.containsKey("1_QUESTIONNAIRE_ONLY"));
        assertTrue(ablation.containsKey("2_CAMERA_ONLY"));
        assertTrue(ablation.containsKey("3_VOICE_ONLY"));
        assertTrue(ablation.containsKey("4_CAMERA_PLUS_VOICE"));
        assertTrue(ablation.containsKey("5_QUESTIONNAIRE_CAMERA_VOICE"));
        assertTrue(ablation.containsKey("6_FULL_MULTIMODAL"));

        for (Map.Entry<String, Double> entry : ablation.entrySet()) {
            double score = entry.getValue();
            assertTrue(score >= 0.0 && score <= 1.0,
                    "Ablation score for " + entry.getKey() + " must be between 0.0 and 1.0, but got " + score);
        }
    }

    @Test
    void testAblationWithNullQuestionnaireDefaultsSafely() {
        MultimodalFusionService service = new MultimodalFusionService();

        BehaviourFeatures bf = new BehaviourFeatures();
        bf.setSocialInteractionScore(50.0);
        bf.setCommunicationScore(50.0);
        bf.setAttentionScore(50.0);
        bf.setMotorBehaviourScore(50.0);
        bf.setImitationScore(50.0);
        bf.setPlayBehaviourScore(50.0);
        bf.setRepetitiveBehaviourScore(40.0);
        bf.setResponseLatencyScore(50.0);
        bf.setVoiceMetricScore(50.0);
        bf.setCameraMovementScore(50.0);

        Map<String, Double> ablation = service.computeModalityFusionAblation(bf, null);

        assertNotNull(ablation);
        assertTrue(ablation.containsKey("1_QUESTIONNAIRE_ONLY"));
        assertEquals(0.25, ablation.get("1_QUESTIONNAIRE_ONLY"));
    }
}

