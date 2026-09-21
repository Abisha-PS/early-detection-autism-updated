package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
public class ActivityRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AssessmentSession session;

    @Column(name = "stage_number", nullable = false)
    private Integer stageNumber; // 2 through 9

    @Column(name = "stage_name", nullable = false, length = 100)
    private String stageName;

    @Column(nullable = false)
    private Boolean completed = false;

    @Column(name = "duration_seconds")
    private Integer durationSeconds = 0;

    @Column(name = "interaction_count")
    private Integer interactionCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ActivityRecord() {}

    public ActivityRecord(AssessmentSession session, Integer stageNumber, String stageName) {
        this.session = session;
        this.stageNumber = stageNumber;
        this.stageName = stageName;
    }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public AssessmentSession getSession() { return session; }
    public void setSession(AssessmentSession session) { this.session = session; }

    public Integer getStageNumber() { return stageNumber; }
    public void setStageNumber(Integer stageNumber) { this.stageNumber = stageNumber; }

    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

    public Integer getInteractionCount() { return interactionCount; }
    public void setInteractionCount(Integer interactionCount) { this.interactionCount = interactionCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

