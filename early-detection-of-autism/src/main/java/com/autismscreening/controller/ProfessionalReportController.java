package com.autismscreening.controller;

import com.autismscreening.model.AssessmentSession;
import com.autismscreening.repository.AssessmentSessionRepository;
import com.autismscreening.service.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ProfessionalReportController {

    @Autowired
    private AssessmentSessionRepository sessionRepository;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @GetMapping("/session/{sessionId}/pdf")
    public ResponseEntity<byte[]> downloadPdfReport(@PathVariable Long sessionId) {
        Optional<AssessmentSession> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AssessmentSession session = sessionOpt.get();
        byte[] pdfBytes = reportGenerationService.generatePdfReport(session);

        String filename = "Autism_Screening_Report_" + session.getChild().getAnonymousCode() + "_Session" + session.getSessionNumber() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

