package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "behaviour_features")
public class BehaviourFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long behaviourFeatureId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private AssessmentSession session;

    // Category A: Social Interaction (0 - 100)
    @Column(name = "social_interaction_score", nullable = false)
    private Double socialInteractionScore = 0.0;

    // Category B: Communication (0 - 100)
    @Column(name = "communication_score", nullable = false)
    private Double communicationScore = 0.0;

    // Category C: Attention (0 - 100)
    @Column(name = "attention_score", nullable = false)
    private Double attentionScore = 0.0;

    // Category D: Motor Behaviour (0 - 100)
    @Column(name = "motor_behaviour_score", nullable = false)
    private Double motorBehaviourScore = 0.0;

    // Category E: Imitation (0 - 100)
    @Column(name = "imitation_score", nullable = false)
    private Double imitationScore = 0.0;

    // Category F: Play Behaviour (0 - 100)
    @Column(name = "play_behaviour_score", nullable = false)
    private Double playBehaviourScore = 0.0;

    // Category G: Repetitive Behaviour (0 - 100, where higher indicates more repetitive flags)
    @Column(name = "repetitive_behaviour_score", nullable = false)
    private Double repetitiveBehaviourScore = 0.0;

    // Category H: Response Latency (0 - 100, where higher is faster/more prompt)
    @Column(name = "response_latency_score", nullable = false)
    private Double responseLatencyScore = 0.0;

    // Category I: Voice/Communication Features (0 - 100)
    @Column(name = "voice_metric_score", nullable = false)
    private Double voiceMetricScore = 0.0;

    // Category J: Camera-derived Movement Features (0 - 100)
    @Column(name = "camera_movement_score", nullable = false)
    private Double cameraMovementScore = 0.0;

    private LocalDateTime createdAt = LocalDateTime.now();

    public BehaviourFeatures() {}

    public Long getBehaviourFeatureId() { return behaviourFeatureId; }
    public void setBehaviourFeatureId(Long behaviourFeatureId) { this.behaviourFeatureId = behaviourFeatureId; }

    public AssessmentSession getSession() { return session; }
    public void setSession(AssessmentSession session) { this.session = session; }

    public Double getSocialInteractionScore() { return socialInteractionScore; }
    public void setSocialInteractionScore(Double socialInteractionScore) { this.socialInteractionScore = socialInteractionScore; }

    public Double getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(Double communicationScore) { this.communicationScore = communicationScore; }

    public Double getAttentionScore() { return attentionScore; }
    public void setAttentionScore(Double attentionScore) { this.attentionScore = attentionScore; }

    public Double getMotorBehaviourScore() { return motorBehaviourScore; }
    public void setMotorBehaviourScore(Double motorBehaviourScore) { this.motorBehaviourScore = motorBehaviourScore; }

    public Double getImitationScore() { return imitationScore; }
    public void setImitationScore(Double imitationScore) { this.imitationScore = imitationScore; }

    public Double getPlayBehaviourScore() { return playBehaviourScore; }
    public void setPlayBehaviourScore(Double playBehaviourScore) { this.playBehaviourScore = playBehaviourScore; }

    public Double getRepetitiveBehaviourScore() { return repetitiveBehaviourScore; }
    public void setRepetitiveBehaviourScore(Double repetitiveBehaviourScore) { this.repetitiveBehaviourScore = repetitiveBehaviourScore; }

    public Double getResponseLatencyScore() { return responseLatencyScore; }
    public void setResponseLatencyScore(Double responseLatencyScore) { this.responseLatencyScore = responseLatencyScore; }

    public Double getVoiceMetricScore() { return voiceMetricScore; }
    public void setVoiceMetricScore(Double voiceMetricScore) { this.voiceMetricScore = voiceMetricScore; }

    public Double getCameraMovementScore() { return cameraMovementScore; }
    public void setCameraMovementScore(Double cameraMovementScore) { this.cameraMovementScore = cameraMovementScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

