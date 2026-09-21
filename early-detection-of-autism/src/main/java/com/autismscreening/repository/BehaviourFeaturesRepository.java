package com.autismscreening.repository;

import com.autismscreening.model.AssessmentSession;
import com.autismscreening.model.BehaviourFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BehaviourFeaturesRepository extends JpaRepository<BehaviourFeatures, Long> {
    Optional<BehaviourFeatures> findBySession(AssessmentSession session);
}

