package com.autismscreening.service;

import com.autismscreening.model.*;
import com.autismscreening.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GuidanceRecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Transactional
    public List<Recommendation> generatePersonalizedGuidance(AssessmentSession session, BehaviourFeatures bf) {
        // Clear previous recommendations for this session if re-running
        List<Recommendation> existing = recommendationRepository.findBySession(session);
        if (!existing.isEmpty()) {
            recommendationRepository.deleteAll(existing);
        }

        List<Recommendation> list = new ArrayList<>();

        // 1. Communication Guidance
        if (bf.getCommunicationScore() < 65.0) {
            list.add(new Recommendation(
                session,
                "COMMUNICATION",
                "Fostering Shared Turn-Taking and Expressive Language",
                "Encourage simple back-and-forth conversational games during daily routines. " +
                "Utilize picture-based cues (visual schedules) to reinforce vocabulary. " +
                "Allow ample pause time (5-8 seconds) for your child to process and vocalize responses. " +
                "If speech delays persist, consult a certified Speech-Language Pathologist (SLP).",
                bf.getCommunicationScore() < 45.0 ? "HIGH" : "MEDIUM"
            ));
        }

        // 2. Social Interaction Guidance
        if (bf.getSocialInteractionScore() < 65.0) {
            list.add(new Recommendation(
                session,
                "SOCIAL_INTERACTION",
                "Strengthening Joint Attention & Shared Delight",
                "Engage in face-to-face floor playtime such as peek-a-boo, rolling a ball back-and-forth, or singing action songs. " +
                "Point out interesting objects in the room and encourage following your finger ('Look at the bird!'). " +
                "Celebrate spontaneous eye gaze and shared smiles with warm positive praise.",
                bf.getSocialInteractionScore() < 45.0 ? "HIGH" : "MEDIUM"
            ));
        }

        // 3. Motor and Repetitive Behaviour Guidance
        if (bf.getRepetitiveBehaviourScore() > 40.0 || bf.getMotorBehaviourScore() < 60.0) {
            list.add(new Recommendation(
                session,
                "MOTOR",
                "Sensory Environment Awareness & Rhythmic Play",
                "Observe the settings and emotional triggers when repetitive hand movements or rocking occur (e.g. excitement, sensory overload). " +
                "Provide a calm, predictable sensory corner with soft cushions and calming textures. " +
                "Introduce fun motor imitation activities like clapping to a rhythm, marching, or gentle animal poses.",
                bf.getRepetitiveBehaviourScore() > 65.0 ? "HIGH" : "MEDIUM"
            ));
        }

        // 4. Attention and Focus Guidance
        if (bf.getAttentionScore() < 65.0) {
            list.add(new Recommendation(
                session,
                "ATTENTION",
                "Structured Micro-Activities & Minimizing Distraction",
                "Divide playtime into short, 3-to-5 minute interactive bursts rather than prolonged tasks. " +
                "Reduce background noise, television, and cluttered visual stimuli in the play area. " +
                "Gradually increase the duration of shared activities as your child's engagement deepens.",
                bf.getAttentionScore() < 45.0 ? "HIGH" : "MEDIUM"
            ));
        }

        // Default supportive reinforcement if all categories are in healthy range
        if (list.isEmpty()) {
            list.add(new Recommendation(
                session,
                "GENERAL_DEVELOPMENT",
                "Continuing Enriching Interactive Play",
                "Your child displayed strong social engagement and attentive responses across the activities! " +
                "Continue daily reading, shared storytelling, active outdoor play, and imaginative role-playing games.",
                "LOW"
            ));
        }

        return recommendationRepository.saveAll(list);
    }
}

