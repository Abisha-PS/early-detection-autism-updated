package com.autismscreening.repository;

import com.autismscreening.model.AssessmentSession;
import com.autismscreening.model.CameraFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraFeaturesRepository extends JpaRepository<CameraFeatures, Long> {
    List<CameraFeatures> findBySession(AssessmentSession session);
    List<CameraFeatures> findBySessionAndStageNumber(AssessmentSession session, Integer stageNumber);
}

