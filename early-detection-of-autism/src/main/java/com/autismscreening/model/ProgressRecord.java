package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_records")
public class ProgressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AssessmentSession session;

    @Column(name = "session_sequence", nullable = false)
    private Integer sessionSequence; // 1, 2, 3...

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate = LocalDate.now();

    @Column(name = "overall_risk_score", nullable = false)
    private Double overallRiskScore;

    @Column(name = "attention_trend", nullable = false)
    private Double attentionTrend;

    @Column(name = "communication_trend", nullable = false)
    private Double communicationTrend;

    @Column(name = "social_trend", nullable = false)
    private Double socialTrend;

    @Column(name = "motor_trend", nullable = false)
    private Double motorTrend;

    @Column(name = "repetitive_trend", nullable = false)
    private Double repetitiveTrend;

    @Column(name = "play_trend", nullable = false)
    private Double playTrend;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ProgressRecord() {}

    public Long getProgressId() { return progressId; }
    public void setProgressId(Long progressId) { this.progressId = progressId; }

    public Child getChild() { return child; }
    public void setChild(Child child) { this.child = child; }

    public AssessmentSession getSession() { return session; }
    public void setSession(AssessmentSession session) { this.session = session; }

    public Integer getSessionSequence() { return sessionSequence; }
    public void setSessionSequence(Integer sessionSequence) { this.sessionSequence = sessionSequence; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public Double getOverallRiskScore() { return overallRiskScore; }
    public void setOverallRiskScore(Double overallRiskScore) { this.overallRiskScore = overallRiskScore; }

    public Double getAttentionTrend() { return attentionTrend; }
    public void setAttentionTrend(Double attentionTrend) { this.attentionTrend = attentionTrend; }

    public Double getCommunicationTrend() { return communicationTrend; }
    public void setCommunicationTrend(Double communicationTrend) { this.communicationTrend = communicationTrend; }

    public Double getSocialTrend() { return socialTrend; }
    public void setSocialTrend(Double socialTrend) { this.socialTrend = socialTrend; }

    public Double getMotorTrend() { return motorTrend; }
    public void setMotorTrend(Double motorTrend) { this.motorTrend = motorTrend; }

    public Double getRepetitiveTrend() { return repetitiveTrend; }
    public void setRepetitiveTrend(Double repetitiveTrend) { this.repetitiveTrend = repetitiveTrend; }

    public Double getPlayTrend() { return playTrend; }
    public void setPlayTrend(Double playTrend) { this.playTrend = playTrend; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

