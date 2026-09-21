package com.autismscreening.service;

import com.autismscreening.model.*;
import com.autismscreening.repository.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class ReportGenerationService {

    @Autowired
    private BehaviourFeaturesRepository behaviourFeaturesRepository;

    @Autowired
    private MLResultRepository mlResultRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private ClinicalReferenceRepository clinicalReferenceRepository;

    public byte[] generatePdfReport(AssessmentSession session) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Child child = session.getChild();
            Optional<BehaviourFeatures> bfOpt = behaviourFeaturesRepository.findBySession(session);
            Optional<MLResult> mlOpt = mlResultRepository.findBySession(session);
            Optional<ClinicalReference> refOpt = clinicalReferenceRepository.findTopByChildOrderByEvaluationDateDesc(child);
            List<Recommendation> recommendations = recommendationRepository.findBySession(session);

            // Color Palette
            Color primaryBlue = new Color(30, 64, 175);
            Color headerBg = new Color(241, 245, 249);
            Color alertYellow = new Color(254, 243, 199);

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, primaryBlue);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, primaryBlue);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);
            Font disclaimerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.DARK_GRAY);

            // Header Banner
            Paragraph title = new Paragraph("MULTIMODAL BEHAVIOURAL SCREENING REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph sub = new Paragraph("Early AI-Assisted Developmental Screening & Clinical Reference Validation", subtitleFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            sub.setSpacingAfter(15);
            document.add(sub);

            // 1. Child & Session Metadata Table
            PdfPTable metaTable = new PdfPTable(4);
            metaTable.setWidthPercentage(100);
            metaTable.setSpacingAfter(12);

            addCell(metaTable, "Child Code:", boldFont, headerBg);
            addCell(metaTable, child.getAnonymousCode(), bodyFont, Color.WHITE);
            addCell(metaTable, "Assessment Date:", boldFont, headerBg);
            addCell(metaTable, session.getSessionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), bodyFont, Color.WHITE);

            addCell(metaTable, "Age:", boldFont, headerBg);
            addCell(metaTable, child.getAgeMonths() + " months (" + (child.getAgeMonths() / 12) + " yrs)", bodyFont, Color.WHITE);
            addCell(metaTable, "Session Sequence:", boldFont, headerBg);
            addCell(metaTable, "Assessment #" + session.getSessionNumber(), bodyFont, Color.WHITE);

            addCell(metaTable, "Gender / Language:", boldFont, headerBg);
            addCell(metaTable, child.getGender() + " / " + child.getPreferredLanguage().toUpperCase(), bodyFont, Color.WHITE);
            addCell(metaTable, "Communication Level:", boldFont, headerBg);
            addCell(metaTable, child.getCommunicationLevel().replace("_", " "), bodyFont, Color.WHITE);

            document.add(metaTable);

            // 2. Screening Result & Risk Category Callout
            if (mlOpt.isPresent()) {
                MLResult ml = mlOpt.get();
                PdfPTable resTable = new PdfPTable(1);
                resTable.setWidthPercentage(100);
                resTable.setSpacingAfter(12);

                PdfPCell resCell = new PdfPCell();
                resCell.setBackgroundColor(alertYellow);
                resCell.setPadding(10);
                resCell.setBorderColor(new Color(245, 158, 11));

                Paragraph resP = new Paragraph("AI BEHAVIOURAL SCREENING OUTCOME: " + ml.getScreeningCategory().replace("_", " "), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(180, 83, 9)));
                resP.setAlignment(Element.ALIGN_CENTER);
                resCell.addElement(resP);

                Paragraph descP = new Paragraph("Screening Risk Metric: " + String.format("%.2f", ml.getRiskScore()) + " (Scale: 0.00 to 1.00) | Model Confidence: " + Math.round(ml.getModelConfidence() * 100) + "% | Architecture: " + ml.getModelName(), bodyFont);
                descP.setAlignment(Element.ALIGN_CENTER);
                resCell.addElement(descP);

                resTable.addCell(resCell);
                document.add(resTable);
            }

            // 3. Multimodal Behaviour Categories Table (Categories A - J)
            if (bfOpt.isPresent()) {
                BehaviourFeatures bf = bfOpt.get();
                Paragraph catHeader = new Paragraph("Multimodal Behavioural Feature Matrix (Categories A through J)", sectionFont);
                catHeader.setSpacingAfter(6);
                document.add(catHeader);

                PdfPTable featTable = new PdfPTable(4);
                featTable.setWidthPercentage(100);
                featTable.setSpacingAfter(12);

                addCell(featTable, "A. Social Interaction:", boldFont, headerBg);
                addCell(featTable, bf.getSocialInteractionScore() + " / 100", bodyFont, Color.WHITE);
                addCell(featTable, "B. Communication:", boldFont, headerBg);
                addCell(featTable, bf.getCommunicationScore() + " / 100", bodyFont, Color.WHITE);

                addCell(featTable, "C. Visual Attention:", boldFont, headerBg);
                addCell(featTable, bf.getAttentionScore() + " / 100", bodyFont, Color.WHITE);
                addCell(featTable, "D. Motor Behaviour:", boldFont, headerBg);
                addCell(featTable, bf.getMotorBehaviourScore() + " / 100", bodyFont, Color.WHITE);

                addCell(featTable, "E. Imitation Accuracy:", boldFont, headerBg);
                addCell(featTable, bf.getImitationScore() + " / 100", bodyFont, Color.WHITE);
                addCell(featTable, "F. Object Play Flexibility:", boldFont, headerBg);
                addCell(featTable, bf.getPlayBehaviourScore() + " / 100", bodyFont, Color.WHITE);

                addCell(featTable, "G. Repetitive Behaviour Index:", boldFont, headerBg);
                addCell(featTable, bf.getRepetitiveBehaviourScore() + " / 100", bodyFont, Color.WHITE);
                addCell(featTable, "H. Response Latency Score:", boldFont, headerBg);
                addCell(featTable, bf.getResponseLatencyScore() + " / 100", bodyFont, Color.WHITE);

                addCell(featTable, "I. Voice Metrics Index:", boldFont, headerBg);
                addCell(featTable, bf.getVoiceMetricScore() + " / 100", bodyFont, Color.WHITE);
                addCell(featTable, "J. Camera Movement Metric:", boldFont, headerBg);
                addCell(featTable, bf.getCameraMovementScore() + " / 100", bodyFont, Color.WHITE);

                document.add(featTable);
            }

            // 4. Model Architecture & Interpretation
            Paragraph modelHeader = new Paragraph("AI Screening Architecture & Interpretation", sectionFont);
            modelHeader.setSpacingBefore(4);
            modelHeader.setSpacingAfter(6);
            document.add(modelHeader);
            PdfPTable modelTable = new PdfPTable(2);
            modelTable.setWidthPercentage(100);
            modelTable.setSpacingAfter(12);
            addCell(modelTable, "Visual input:", boldFont, headerBg);
            addCell(modelTable, "Camera sensor → visual/behavioural feature extraction (CNN layer in the proposed multimodal pipeline)", bodyFont, Color.WHITE);
            addCell(modelTable, "Classifier:", boldFont, headerBg);
            addCell(modelTable, "Random Forest screening classifier using fused behavioural features", bodyFont, Color.WHITE);
            addCell(modelTable, "Interpretation:", boldFont, headerBg);
            addCell(modelTable, "Lower / Moderate / Higher observed concern indicates screening priority, not an autism diagnosis.", bodyFont, Color.WHITE);
            addCell(modelTable, "Data limitation:", boldFont, headerBg);
            addCell(modelTable, "Prototype Random Forest calibration uses synthetic/demo data. Clinical validation with ethically collected labelled data is required.", bodyFont, Color.WHITE);
            document.add(modelTable);

            // 4. Clinical Reference / ADOS-2 Section
            Paragraph adosHeader = new Paragraph("Standardized ADOS-2 Clinical Reference Comparison", sectionFont);
            adosHeader.setSpacingAfter(6);
            document.add(adosHeader);

            PdfPTable adosTable = new PdfPTable(2);
            adosTable.setWidthPercentage(100);
            adosTable.setSpacingAfter(12);

            if (refOpt.isPresent()) {
                ClinicalReference ref = refOpt.get();
                addCell(adosTable, "ADOS-2 Module Administered:", boldFont, headerBg);
                addCell(adosTable, ref.getAdosModule() + " (Evaluated: " + ref.getEvaluationDate() + ")", bodyFont, Color.WHITE);

                addCell(adosTable, "Social Affect (SA) / RRB Scores:", boldFont, headerBg);
                addCell(adosTable, "SA: " + ref.getAdosSocialAffectScore() + " | RRB: " + ref.getAdosRrbScore() + " | Total: " + ref.getAdosTotalCutoffScore(), bodyFont, Color.WHITE);

                addCell(adosTable, "Clinician ADOS Classification:", boldFont, headerBg);
                addCell(adosTable, ref.getAdosClassification().replace("_", " "), bodyFont, Color.WHITE);

                addCell(adosTable, "AI / ADOS Relationship:", boldFont, headerBg);
                addCell(adosTable, "Shown as a reference comparison only; the AI screening result does not replace clinician-administered ADOS-2.", bodyFont, Color.WHITE);
            } else {
                addCell(adosTable, "Clinical Reference Status:", boldFont, headerBg);
                addCell(adosTable, "No formal clinician ADOS-2 recorded for this profile yet. Screening provides triage indicators.", bodyFont, Color.WHITE);
            }
            document.add(adosTable);

            // 5. Supportive Non-Diagnostic Recommendations
            if (!recommendations.isEmpty()) {
                Paragraph recHeader = new Paragraph("Supportive Developmental Guidance (Non-Diagnostic)", sectionFont);
                recHeader.setSpacingAfter(6);
                document.add(recHeader);

                for (Recommendation r : recommendations) {
                    Paragraph rP = new Paragraph("• [" + r.getCategory() + "] " + r.getGuidanceTitle() + " (" + r.getPriorityLevel() + " Priority)", boldFont);
                    document.add(rP);
                    Paragraph rText = new Paragraph("  " + r.getGuidanceText(), bodyFont);
                    rText.setSpacingAfter(4);
                    document.add(rText);
                }
            }

            // 6. Mandatory Ethical Disclaimer & Limitations
            Paragraph disHeader = new Paragraph("Clinical Disclaimers, Limitations, & Next Steps", sectionFont);
            disHeader.setSpacingBefore(10);
            disHeader.setSpacingAfter(4);
            document.add(disHeader);

            String disclaimerText = "IMPORTANT NOTICE: This screening system is strictly an assistive behavioral observation tool designed for early triage. " +
                    "It does NOT independently diagnose Autism Spectrum Disorder (ASD). Ordinary webcam and microphone sensors provide behavioral proxies, not medical-grade physiological telemetry. " +
                    "Observed behavioural patterns are screening indicators. A qualified developmental professional can decide whether further assessment is appropriate. " +
                    "ADOS-2 is a standardized clinician-administered assessment and should be interpreted by trained professionals.";
            Paragraph disP = new Paragraph(disclaimerText, disclaimerFont);
            document.add(disP);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private void addCell(PdfPTable table, String text, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(6);
        cell.setBorderColor(new Color(226, 232, 240));
        table.addCell(cell);
    }
}

