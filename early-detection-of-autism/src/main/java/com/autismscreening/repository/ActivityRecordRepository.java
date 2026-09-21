package com.autismscreening.repository;

import com.autismscreening.model.ActivityRecord;
import com.autismscreening.model.AssessmentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRecordRepository extends JpaRepository<ActivityRecord, Long> {
    List<ActivityRecord> findBySessionOrderByStageNumberAsc(AssessmentSession session);
}

