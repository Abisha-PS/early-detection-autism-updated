package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_reference")
public class ClinicalReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(name = "ados_module", nullable = false, length = 50)
    private String adosModule = "Module 1"; // Toddler, Module 1, Module 2, Module 3

    @Column(name = "ados_social_affect_score", nullable = false)
    private Integer adosSocialAffectScore;

    @Column(name = "ados_rrb_score", nullable = false)
    private Integer adosRrbScore;

    @Column(name = "ados_total_cutoff_score", nullable = false)
    private Integer adosTotalCutoffScore;

    @Column(name = "ados_classification", nullable = false, length = 50)
    private String adosClassification; // NON_SPECTRUM, AUTISM_SPECTRUM, AUTISM

    @Column(name = "clinician_notes", columnDefinition = "TEXT")
    private String clinicianNotes;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate = LocalDate.now();

    private LocalDateTime createdAt = LocalDateTime.now();

    public ClinicalReference() {}

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public Child getChild() { return child; }
    public void setChild(Child child) { this.child = child; }

    public String getAdosModule() { return adosModule; }
    public void setAdosModule(String adosModule) { this.adosModule = adosModule; }

    public Integer getAdosSocialAffectScore() { return adosSocialAffectScore; }
    public void setAdosSocialAffectScore(Integer adosSocialAffectScore) { this.adosSocialAffectScore = adosSocialAffectScore; }

    public Integer getAdosRrbScore() { return adosRrbScore; }
    public void setAdosRrbScore(Integer adosRrbScore) { this.adosRrbScore = adosRrbScore; }

    public Integer getAdosTotalCutoffScore() { return adosTotalCutoffScore; }
    public void setAdosTotalCutoffScore(Integer adosTotalCutoffScore) { this.adosTotalCutoffScore = adosTotalCutoffScore; }

    public String getAdosClassification() { return adosClassification; }
    public void setAdosClassification(String adosClassification) { this.adosClassification = adosClassification; }

    public String getClinicianNotes() { return clinicianNotes; }
    public void setClinicianNotes(String clinicianNotes) { this.clinicianNotes = clinicianNotes; }

    public LocalDate getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDate evaluationDate) { this.evaluationDate = evaluationDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

