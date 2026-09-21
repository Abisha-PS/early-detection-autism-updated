package com.autismscreening.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "camera_features")
public class CameraFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cameraFeatureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AssessmentSession session;

    @Column(name = "stage_number", nullable = false)
    private Integer stageNumber;

    @Column(name = "face_detected_ratio", nullable = false)
    private Double faceDetectedRatio = 0.0;

    @Column(name = "head_movement_variability", nullable = false)
    private Double headMovementVariability = 0.0;

    @Column(name = "gaze_screen_duration_ratio", nullable = false)
    private Double gazeScreenDurationRatio = 0.0;

    @Column(name = "gaze_switch_frequency", nullable = false)
    private Double gazeSwitchFrequency = 0.0;

    @Column(name = "imitation_pose_similarity", nullable = false)
    private Double imitationPoseSimilarity = 0.0;

    @Column(name = "movement_smoothness", nullable = false)
    private Double movementSmoothness = 0.0;

    @Column(name = "repetitive_hand_frequency", nullable = false)
    private Double repetitiveHandFrequency = 0.0;

    @Column(name = "rocking_motion_detected", nullable = false)
    private Boolean rockingMotionDetected = false;

    @Column(name = "response_latency_ms", nullable = false)
    private Integer responseLatencyMs = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    public CameraFeatures() {}

    public Long getCameraFeatureId() { return cameraFeatureId; }
    public void setCameraFeatureId(Long cameraFeatureId) { this.cameraFeatureId = cameraFeatureId; }

    public AssessmentSession getSession() { return session; }
    public void setSession(AssessmentSession session) { this.session = session; }

    public Integer getStageNumber() { return stageNumber; }
    public void setStageNumber(Integer stageNumber) { this.stageNumber = stageNumber; }

    public Double getFaceDetectedRatio() { return faceDetectedRatio; }
    public void setFaceDetectedRatio(Double faceDetectedRatio) { this.faceDetectedRatio = faceDetectedRatio; }

    public Double getHeadMovementVariability() { return headMovementVariability; }
    public void setHeadMovementVariability(Double headMovementVariability) { this.headMovementVariability = headMovementVariability; }

    public Double getGazeScreenDurationRatio() { return gazeScreenDurationRatio; }
    public void setGazeScreenDurationRatio(Double gazeScreenDurationRatio) { this.gazeScreenDurationRatio = gazeScreenDurationRatio; }

    public Double getGazeSwitchFrequency() { return gazeSwitchFrequency; }
    public void setGazeSwitchFrequency(Double gazeSwitchFrequency) { this.gazeSwitchFrequency = gazeSwitchFrequency; }

    public Double getImitationPoseSimilarity() { return imitationPoseSimilarity; }
    public void setImitationPoseSimilarity(Double imitationPoseSimilarity) { this.imitationPoseSimilarity = imitationPoseSimilarity; }

    public Double getMovementSmoothness() { return movementSmoothness; }
    public void setMovementSmoothness(Double movementSmoothness) { this.movementSmoothness = movementSmoothness; }

    public Double getRepetitiveHandFrequency() { return repetitiveHandFrequency; }
    public void setRepetitiveHandFrequency(Double repetitiveHandFrequency) { this.repetitiveHandFrequency = repetitiveHandFrequency; }

    public Boolean getRockingMotionDetected() { return rockingMotionDetected; }
    public void setRockingMotionDetected(Boolean rockingMotionDetected) { this.rockingMotionDetected = rockingMotionDetected; }

    public Integer getResponseLatencyMs() { return responseLatencyMs; }
    public void setResponseLatencyMs(Integer responseLatencyMs) { this.responseLatencyMs = responseLatencyMs; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

