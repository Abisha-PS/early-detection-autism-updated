package com.autismscreening.repository;

import com.autismscreening.model.AssessmentSession;
import com.autismscreening.model.VoiceFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceFeaturesRepository extends JpaRepository<VoiceFeatures, Long> {
    List<VoiceFeatures> findBySession(AssessmentSession session);
    List<VoiceFeatures> findBySessionAndStageNumber(AssessmentSession session, Integer stageNumber);
}

