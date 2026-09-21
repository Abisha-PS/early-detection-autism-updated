package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AssessmentSession session;

    @Column(nullable = false, length = 50)
    private String category; // 'COMMUNICATION', 'SOCIAL_INTERACTION', 'MOTOR', 'ATTENTION'

    @Column(name = "guidance_title", nullable = false, length = 200)
    private String guidanceTitle;

    @Column(name = "guidance_text", columnDefinition = "TEXT", nullable = false)
    private String guidanceText;

    @Column(name = "priority_level", nullable = false, length = 20)
    private String priorityLevel = "MEDIUM"; // 'LOW', 'MEDIUM', 'HIGH'

    private LocalDateTime createdAt = LocalDateTime.now();

    public Recommendation() {}

    public Recommendation(AssessmentSession session, String category, String guidanceTitle, String guidanceText, String priorityLevel) {
        this.session = session;
        this.category = category;
        this.guidanceTitle = guidanceTitle;
        this.guidanceText = guidanceText;
        this.priorityLevel = priorityLevel;
    }

    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }

    public AssessmentSession getSession() { return session; }
    public void setSession(AssessmentSession session) { this.session = session; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getGuidanceTitle() { return guidanceTitle; }
    public void setGuidanceTitle(String guidanceTitle) { this.guidanceTitle = guidanceTitle; }

    public String getGuidanceText() { return guidanceText; }
    public void setGuidanceText(String guidanceText) { this.guidanceText = guidanceText; }

    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

