package com.autismscreening.repository;

import com.autismscreening.model.AssessmentSession;
import com.autismscreening.model.MLResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MLResultRepository extends JpaRepository<MLResult, Long> {
    Optional<MLResult> findBySession(AssessmentSession session);
}

