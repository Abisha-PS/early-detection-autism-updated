package com.autismscreening.repository;

import com.autismscreening.model.AssessmentSession;
import com.autismscreening.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentSessionRepository extends JpaRepository<AssessmentSession, Long> {
    List<AssessmentSession> findByChildOrderBySessionDateDesc(Child child);
    Optional<AssessmentSession> findTopByChildOrderBySessionNumberDesc(Child child);
    long countByChild(Child child);
}
