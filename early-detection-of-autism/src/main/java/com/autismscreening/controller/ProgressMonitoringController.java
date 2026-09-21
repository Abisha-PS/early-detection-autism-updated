package com.autismscreening.controller;

import com.autismscreening.model.Child;
import com.autismscreening.model.ProgressRecord;
import com.autismscreening.repository.ChildRepository;
import com.autismscreening.repository.ProgressRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class ProgressMonitoringController {

    @Autowired
    private ProgressRecordRepository progressRecordRepository;

    @Autowired
    private ChildRepository childRepository;

    @GetMapping("/child/{childId}")
    public ResponseEntity<?> getProgressForChild(@PathVariable Long childId) {
        Optional<Child> childOpt = childRepository.findById(childId);
        if (childOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ProgressRecord> records = progressRecordRepository.findByChildOrderBySessionSequenceAsc(childOpt.get());

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("childId", childId);
        resp.put("anonymousCode", childOpt.get().getAnonymousCode());
        resp.put("recordsCount", records.size());
        resp.put("records", records);
        resp.put("clinicalDisclaimer", "Longitudinal monitoring observes behavioural patterns across repeated sessions. " +
                "Do not interpret improvement or fluctuations as clinical proof of treatment or intervention effectiveness.");

        return ResponseEntity.ok(resp);
    }
}

