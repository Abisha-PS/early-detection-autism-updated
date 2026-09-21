package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parent_questionnaire")
public class ParentQuestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionnaireId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate = LocalDate.now();

    // 0: Rarely/Never, 1: Sometimes, 2: Usually/Always
    private Integer q1SocialSmile;
    private Integer q2EyeContact;
    private Integer q3ResponseToName;
    private Integer q4PointingInterest;
    private Integer q5SharedEnjoyment;
    private Integer q6Imitation;
    private Integer q7RepetitiveMovements; // Inverted scoring or noted
    private Integer q8UnusualSensoryInterests;
    private Integer q9PlayFlexibility;
    private Integer q10LanguageMilestone;

    private Double totalScore;

    @Column(name = "questionnaire_risk_indicator", length = 50)
    private String questionnaireRiskIndicator; // LOW, MODERATE, ELEVATED

    private LocalDateTime createdAt = LocalDateTime.now();

    public ParentQuestionnaire() {}

    public Long getQuestionnaireId() { return questionnaireId; }
    public void setQuestionnaireId(Long questionnaireId) { this.questionnaireId = questionnaireId; }

    public Child getChild() { return child; }
    public void setChild(Child child) { this.child = child; }

    public LocalDate getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDate assessmentDate) { this.assessmentDate = assessmentDate; }

    public Integer getQ1SocialSmile() { return q1SocialSmile; }
    public void setQ1SocialSmile(Integer q1SocialSmile) { this.q1SocialSmile = q1SocialSmile; }

    public Integer getQ2EyeContact() { return q2EyeContact; }
    public void setQ2EyeContact(Integer q2EyeContact) { this.q2EyeContact = q2EyeContact; }

    public Integer getQ3ResponseToName() { return q3ResponseToName; }
    public void setQ3ResponseToName(Integer q3ResponseToName) { this.q3ResponseToName = q3ResponseToName; }

    public Integer getQ4PointingInterest() { return q4PointingInterest; }
    public void setQ4PointingInterest(Integer q4PointingInterest) { this.q4PointingInterest = q4PointingInterest; }

    public Integer getQ5SharedEnjoyment() { return q5SharedEnjoyment; }
    public void setQ5SharedEnjoyment(Integer q5SharedEnjoyment) { this.q5SharedEnjoyment = q5SharedEnjoyment; }

    public Integer getQ6Imitation() { return q6Imitation; }
    public void setQ6Imitation(Integer q6Imitation) { this.q6Imitation = q6Imitation; }

    public Integer getQ7RepetitiveMovements() { return q7RepetitiveMovements; }
    public void setQ7RepetitiveMovements(Integer q7RepetitiveMovements) { this.q7RepetitiveMovements = q7RepetitiveMovements; }

    public Integer getQ8UnusualSensoryInterests() { return q8UnusualSensoryInterests; }
    public void setQ8UnusualSensoryInterests(Integer q8UnusualSensoryInterests) { this.q8UnusualSensoryInterests = q8UnusualSensoryInterests; }

    public Integer getQ9PlayFlexibility() { return q9PlayFlexibility; }
    public void setQ9PlayFlexibility(Integer q9PlayFlexibility) { this.q9PlayFlexibility = q9PlayFlexibility; }

    public Integer getQ10LanguageMilestone() { return q10LanguageMilestone; }
    public void setQ10LanguageMilestone(Integer q10LanguageMilestone) { this.q10LanguageMilestone = q10LanguageMilestone; }

    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }

    public String getQuestionnaireRiskIndicator() { return questionnaireRiskIndicator; }
    public void setQuestionnaireRiskIndicator(String questionnaireRiskIndicator) { this.questionnaireRiskIndicator = questionnaireRiskIndicator; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

