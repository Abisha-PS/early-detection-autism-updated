package com.autismscreening.repository;

import com.autismscreening.model.Child;
import com.autismscreening.model.ParentQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentQuestionnaireRepository extends JpaRepository<ParentQuestionnaire, Long> {
    List<ParentQuestionnaire> findByChildOrderByAssessmentDateDesc(Child child);
    Optional<ParentQuestionnaire> findTopByChildOrderByAssessmentDateDesc(Child child);
}

