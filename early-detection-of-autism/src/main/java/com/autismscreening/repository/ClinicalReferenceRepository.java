package com.autismscreening.repository;

import com.autismscreening.model.Child;
import com.autismscreening.model.ClinicalReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicalReferenceRepository extends JpaRepository<ClinicalReference, Long> {
    List<ClinicalReference> findByChildOrderByEvaluationDateDesc(Child child);
    Optional<ClinicalReference> findTopByChildOrderByEvaluationDateDesc(Child child);
}

