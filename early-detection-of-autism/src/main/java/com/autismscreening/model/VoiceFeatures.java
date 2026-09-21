package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voice_features")
public class VoiceFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voiceFeatureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AssessmentSession session;

    @Column(name = "stage_number", nullable = false)
    private Integer stageNumber;

    @Column(name = "vocalization_count", nullable = false)
    private Integer vocalizationCount = 0;

    @Column(name = "speech_duration_seconds", nullable = false)
    private Double speechDurationSeconds = 0.0;

    @Column(name = "response_latency_ms", nullable = false)
    private Integer responseLatencyMs = 0;

    @Column(name = "pause_duration_average", nullable = false)
    private Double pauseDurationAverage = 0.0;

    @Column(name = "word_count", nullable = false)
    private Integer wordCount = 0;

    @Column(name = "turn_taking_success_rate", nullable = false)
    private Double turnTakingSuccessRate = 0.0;

    @Column(name = "echolalia_repetition_index", nullable = false)
    private Double echolaliaRepetitionIndex = 0.0;

    @Column(name = "audio_clarity_score", nullable = false)
    private Double audioClarityScore = 0.0;

    private LocalDateTime createdAt = LocalDateTime.now();

    public VoiceFeatures() {}

    public Long getVoiceFeatureId() { return voiceFeatureId; }
    public void setVoiceFeatureId(Long voiceFeatureId) { this.voiceFeatureId = voiceFeatureId; }

    public AssessmentSession getSession() { return session; }
    public void setSession(AssessmentSession session) { this.session = session; }

    public Integer getStageNumber() { return stageNumber; }
    public void setStageNumber(Integer stageNumber) { this.stageNumber = stageNumber; }

    public Integer getVocalizationCount() { return vocalizationCount; }
    public void setVocalizationCount(Integer vocalizationCount) { this.vocalizationCount = vocalizationCount; }

    public Double getSpeechDurationSeconds() { return speechDurationSeconds; }
    public void setSpeechDurationSeconds(Double speechDurationSeconds) { this.speechDurationSeconds = speechDurationSeconds; }

    public Integer getResponseLatencyMs() { return responseLatencyMs; }
    public void setResponseLatencyMs(Integer responseLatencyMs) { this.responseLatencyMs = responseLatencyMs; }

    public Double getPauseDurationAverage() { return pauseDurationAverage; }
    public void setPauseDurationAverage(Double pauseDurationAverage) { this.pauseDurationAverage = pauseDurationAverage; }

    public Integer getWordCount() { return wordCount; }
    public void setWordCount(Integer wordCount) { this.wordCount = wordCount; }

    public Double getTurnTakingSuccessRate() { return turnTakingSuccessRate; }
    public void setTurnTakingSuccessRate(Double turnTakingSuccessRate) { this.turnTakingSuccessRate = turnTakingSuccessRate; }

    public Double getEcholaliaRepetitionIndex() { return echolaliaRepetitionIndex; }
    public void setEcholaliaRepetitionIndex(Double echolaliaRepetitionIndex) { this.echolaliaRepetitionIndex = echolaliaRepetitionIndex; }

    public Double getAudioClarityScore() { return audioClarityScore; }
    public void setAudioClarityScore(Double audioClarityScore) { this.audioClarityScore = audioClarityScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

