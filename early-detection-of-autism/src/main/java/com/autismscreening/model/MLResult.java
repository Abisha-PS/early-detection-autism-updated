package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ml_results")
public class MLResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mlResultId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private AssessmentSession session;

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName = "Multimodal_Ensemble_RandomForest";

    @Column(name = "modality_configuration", nullable = false, length = 100)
    private String modalityConfiguration = "FULL_MULTIMODAL";

    @Column(name = "risk_score", nullable = false)
    private Double riskScore; // 0.0 to 1.0

    @Column(name = "screening_category", nullable = false, length = 50)
    private String screeningCategory; // LOWER_OBSERVED_CONCERN, MODERATE_OBSERVED_CONCERN, HIGHER_OBSERVED_CONCERN

    @Column(name = "model_confidence", nullable = false)
    private Double modelConfidence = 0.85;

    @Column(name = "feature_importance_summary", columnDefinition = "TEXT")
    private String featureImportanceSummary;

    @Column(name = "disclaimer_note", columnDefinition = "TEXT", nullable = false)
    private String disclaimerNote = "This AI system provides behavioural risk screening and does NOT provide a medical diagnosis. A clinical developmental evaluation by a qualified healthcare professional is recommended.";

    private LocalDateTime createdAt = LocalDateTime.now();

    public MLResult() {}

    public Long getMlResultId() { return mlResultId; }
    public void setMlResultId(Long mlResultId) { this.mlResultId = mlResultId; }

    public AssessmentSession getSession() { return session; }
    public void setSession(AssessmentSession session) { this.session = session; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getModalityConfiguration() { return modalityConfiguration; }
    public void setModalityConfiguration(String modalityConfiguration) { this.modalityConfiguration = modalityConfiguration; }

    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }

    public String getScreeningCategory() { return screeningCategory; }
    public void setScreeningCategory(String screeningCategory) { this.screeningCategory = screeningCategory; }

    public Double getModelConfidence() { return modelConfidence; }
    public void setModelConfidence(Double modelConfidence) { this.modelConfidence = modelConfidence; }

    public String getFeatureImportanceSummary() { return featureImportanceSummary; }
    public void setFeatureImportanceSummary(String featureImportanceSummary) { this.featureImportanceSummary = featureImportanceSummary; }

    public String getDisclaimerNote() { return disclaimerNote; }
    public void setDisclaimerNote(String disclaimerNote) { this.disclaimerNote = disclaimerNote; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

