package com.autismscreening.repository;

import com.autismscreening.model.Child;
import com.autismscreening.model.ProgressRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRecordRepository extends JpaRepository<ProgressRecord, Long> {
    List<ProgressRecord> findByChildOrderBySessionSequenceAsc(Child child);
}

