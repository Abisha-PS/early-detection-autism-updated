package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childId;

    @Column(name = "anonymous_code", unique = true, nullable = false, length = 64)
    private String anonymousCode; // e.g. "CH-2026-8912"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User parent;

    @Column(name = "age_months", nullable = false)
    private Integer ageMonths; // 24 to 72 months

    @Column(nullable = false, length = 20)
    private String gender; // MALE, FEMALE, OTHER

    @Column(name = "preferred_language", nullable = false, length = 50)
    private String preferredLanguage = "en"; // 'en', 'ml' (Malayalam)

    @Column(name = "developmental_concerns", columnDefinition = "TEXT")
    private String developmentalConcerns;

    @Column(name = "communication_level", nullable = false, length = 50)
    private String communicationLevel; // NON_VERBAL, SINGLE_WORDS, PHRASES, FLUENT

    @Column(name = "previous_assessment_notes", columnDefinition = "TEXT")
    private String previousAssessmentNotes;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Child() {}

    public Child(String anonymousCode, User parent, Integer ageMonths, String gender,
                 String preferredLanguage, String developmentalConcerns,
                 String communicationLevel, String previousAssessmentNotes) {
        this.anonymousCode = anonymousCode;
        this.parent = parent;
        this.ageMonths = ageMonths;
        this.gender = gender;
        this.preferredLanguage = preferredLanguage;
        this.developmentalConcerns = developmentalConcerns;
        this.communicationLevel = communicationLevel;
        this.previousAssessmentNotes = previousAssessmentNotes;
    }

    // Getters and Setters
    public Long getChildId() { return childId; }
    public void setChildId(Long childId) { this.childId = childId; }

    public String getAnonymousCode() { return anonymousCode; }
    public void setAnonymousCode(String anonymousCode) { this.anonymousCode = anonymousCode; }

    public User getParent() { return parent; }
    public void setParent(User parent) { this.parent = parent; }

    public Integer getAgeMonths() { return ageMonths; }
    public void setAgeMonths(Integer ageMonths) { this.ageMonths = ageMonths; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public String getDevelopmentalConcerns() { return developmentalConcerns; }
    public void setDevelopmentalConcerns(String developmentalConcerns) { this.developmentalConcerns = developmentalConcerns; }

    public String getCommunicationLevel() { return communicationLevel; }
    public void setCommunicationLevel(String communicationLevel) { this.communicationLevel = communicationLevel; }

    public String getPreviousAssessmentNotes() { return previousAssessmentNotes; }
    public void setPreviousAssessmentNotes(String previousAssessmentNotes) { this.previousAssessmentNotes = previousAssessmentNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

