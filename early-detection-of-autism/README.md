# Early Detection of Autism in Children
### Multimodal Behavioural Learning, Computer Vision, Voice Analysis and ADOS-2 Validation

A complete, innovative, beginner-to-intermediate final-year engineering project for AI-assisted early neurodevelopmental screening in preschool children (ages 2–6).

---

## 🌟 Project Highlights
- **100% Non-Invasive Sensing:** Uses standard consumer webcams and microphones—zero expensive eye trackers or uncomfortable wearable sensors.
- **Child-Friendly Interactive Game Arena (Stages 2–9):** 8 engaging mini-activities led by virtual mascot **"Pip the Penguin"** with real-time Web Speech TTS guidance and synthesized audio chimes.
- **Privacy-by-Design (Stage 19):** Browser-based feature extraction computes mathematical kinematic and acoustic metrics in real time. **Raw video and audio are NEVER stored or transmitted.**
- **Multimodal Behavioural Matrix (Stage 10):** Categorizes observations into 10 structured dimensions (Categories A through J: Social, Communication, Attention, Motor, Imitation, Play, Repetitive, Latency, Voice, Movement).
- **Sensitivity-Optimized Machine Learning (Stage 11):** 6-classifier benchmark (Logistic Regression, Decision Tree, Random Forest, SVM, KNN, XGBoost) prioritizing sensitivity (>92%) so no at-risk child is overlooked.
- **Multimodal Fusion Ablation (Stage 12):** Compares 6 modality combinations proving superior predictive performance of fused data over single modalities.
- **ADOS-2 Clinical Validation Reference (Stage 13):** Statistically evaluates concordance with clinician-administered ADOS-2 benchmarks without recreating copyrighted materials.
- **Parent & Clinician Portal (Stages 14–17):** Non-stigmatizing risk tiers ("Lower", "Moderate", "Higher observed concern"), interactive Chart.js radar charts, longitudinal progress monitoring across assessments, and actionable home play guidance.
- **Professional Clinical PDF Report (Stage 18):** Instant server-side PDF generation via OpenPDF with complete audit trail and clinical disclaimers.

---

## 📁 Project Folder Structure

```
early-detection-of-autism/
├── pom.xml                                      # Maven config (Spring Boot 3, JPA, H2, MySQL, OpenPDF)
├── mvnw.cmd / mvnw                              # Maven wrapper scripts
├── schema.sql                                   # MySQL DDL for all 12 database tables
├── README.md                                    # Setup, run, and usage manual
│
├── src/main/java/com/autismscreening/
│   ├── AutismScreeningApplication.java          # Spring Boot main application class
│   ├── config/
│   │   └── DemoDataInitializer.java             # Pre-seeds demo child & historical assessment sessions
│   ├── controller/
│   │   ├── AuthController.java                  # Session login & Stage 19 parent consent
│   │   ├── ChildController.java                 # Anonymized child demographic profiles
│   │   ├── QuestionnaireController.java         # Stage 1 Parent questionnaire API
│   │   ├── AssessmentSessionController.java     # Stages 2-9 activity coordination & completion
│   │   ├── FeatureExtractionController.java     # Numerical camera & voice feature ingestion
│   │   ├── ScreeningResultController.java       # ML prediction, fusion scores, ADOS comparison
│   │   ├── ProgressMonitoringController.java    # Longitudinal progress tracking API
│   │   └── ProfessionalReportController.java    # Downloadable clinical PDF report
│   ├── model/
│   │   ├── User.java                            # System users with consent timestamps
│   │   ├── Child.java                           # Anonymized child records (CH-2026-XXXX)
│   │   ├── ParentQuestionnaire.java             # Stage 1 questionnaire items
│   │   ├── AssessmentSession.java               # Assessment session coordinator
│   │   ├── ActivityRecord.java                  # Activity completion registry
│   │   ├── CameraFeatures.java                  # Vision kinematic metrics
│   │   ├── VoiceFeatures.java                   # Acoustic turn-taking metrics
│   │   ├── BehaviourFeatures.java               # Categories A through J
│   │   ├── MLResult.java                        # ML screening prediction & disclaimer
│   │   ├── ClinicalReference.java               # ADOS-2 clinical validation reference
│   │   ├── ProgressRecord.java                  # Longitudinal progress records
│   │   └── Recommendation.java                  # Supportive developmental guidance
│   ├── repository/                              # Spring Data JPA Repositories (11 interfaces)
│   └── service/
│       ├── MultimodalFusionService.java         # Feature aggregation & 6 ablation configurations
│       ├── MLClassificationService.java         # Sensitivity-weighted random forest ensemble
│       ├── ADOSValidationService.java           # ADOS-2 clinical reference comparison
│       ├── GuidanceRecommendationService.java   # Personalized supportive guidance generator
│       └── ReportGenerationService.java         # OpenPDF formal screening report builder
│
├── src/main/resources/
│   ├── application.properties                   # Default H2 configuration
│   ├── application-mysql.properties             # Environment-based MySQL profile
│   └── static/                                  # Child-Friendly Frontend
│       ├── css/
│       │   ├── child-theme.css                  # Cheerful cartoon palette, mascot styling, animations
│       │   └── dashboard.css                    # Modern parent/clinician dashboard styling
│       ├── js/
│       │   ├── mascot.js                        # Pip the Penguin TTS speech & sound synthesizers
│       │   ├── camera-vision.js                 # Webcam non-invasive face & gaze tracking proxy
│       │   ├── voice-processor.js               # Microphone Web Speech & acoustic analyzer
│       │   ├── activity-engine.js               # Interactive state machine for Stages 2 to 9
│       │   └── parent-dashboard.js              # Chart.js radar, line, and bar chart renderer
│       ├── index.html                           # Landing page with portal navigation
│       ├── consent.html                         # Stage 19: Privacy & Parent Consent
│       ├── questionnaire.html                   # Stage 1: Parent Questionnaire & Child Profile
│       ├── child-session.html                   # Stages 2-9: Child Interactive Game Arena
│       ├── completion.html                      # Stage 14: Child-friendly celebration ("Great Job! 🌟")
│       ├── parent-dashboard.html                # Stages 15-17: Parent Analytics & Guidance
│       └── professional-report.html             # Stage 18: Downloadable Clinical Screening Report
│
├── python_ml/                                   # Offline Research & ML Evaluation Module
│   ├── requirements.txt                         # Python dependencies
│   ├── generate_synthetic_dataset.py            # Generates realistic synthetic benchmark dataset (500 samples)
│   ├── train_models.py                          # Compares 6 ML classifiers with sensitivity prioritization
│   ├── multimodal_ablation_study.py             # Compares 6 modality configurations
│   └── ados_validation.py                       # Computes clinical agreement, Cohen's Kappa, and matrices
│
└── docs/
    ├── FINAL_PROJECT_REPORT.md                  # Complete Academic Project Report (Abstract to References)
    └── FINAL_PROJECT_PRESENTATION_SLIDES.md     # 24 Presentation Slides with Speaker Notes
```

---

## 🚀 Quickstart: Running the Application

### 1. Requirements
- **Java 17 or higher** (Java 21 / 25 fully supported)
- Modern Web Browser (Google Chrome, Microsoft Edge, or Mozilla Firefox)

### 2. Launch the Java Spring Boot Backend
Open PowerShell or Command Prompt in the project root:
```powershell
# Using the Maven wrapper:
.\mvnw.cmd spring-boot:run
```
*(Or if you have Apache Maven installed on PATH: `mvn spring-boot:run`)*

### 3. Open in Browser
Once the console prints:
```
=================================================================
   Early Detection of Autism Screening System Started!
   Access Web Application at: http://localhost:8080/
   Parent / Clinician Dashboard: http://localhost:8080/parent-dashboard.html
   Interactive Child Game Arena: http://localhost:8080/child-session.html
   H2 Embedded Database Console: http://localhost:8080/h2-console
=================================================================
```

Visit:
- **Landing Page:** [http://localhost:8080/](http://localhost:8080/)
- **Parent & Clinician Dashboard:** [http://localhost:8080/parent-dashboard.html](http://localhost:8080/parent-dashboard.html)
- **Interactive Child Game Arena:** [http://localhost:8080/child-session.html](http://localhost:8080/child-session.html)
- **H2 Embedded Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:file:./data/autism_db`, User: `sa`, Password: empty)

---

## 🗄️ Database Setup (Dual Mode)

### Default: H2 Embedded Database (Zero Configuration)
The application runs using an embedded file-backed H2 database located in `./data/autism_db`. New users, children, questionnaires, sessions, and results are persisted through the application APIs.

### Production: MySQL 8.0+ Database
1. Open MySQL Workbench or MySQL Command Line and run `schema.sql`:
   ```sql
   source /path/to/early-detection-of-autism/schema.sql;
   ```
2. Set the MySQL connection environment variables in PowerShell:
   ```powershell
   $env:SPRING_PROFILES_ACTIVE="mysql"
   $env:DB_URL="jdbc:mysql://localhost:3306/autism_screening_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
   $env:DB_USERNAME="root"
   $env:DB_PASSWORD="your_password"
   .\mvnw.cmd spring-boot:run
   ```
   `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` are read by `application-mysql.properties`; credentials are not stored in source control.

---

## 📷 Webcam & Microphone Access Instructions
1. When you enter the **Child Play Arena** (`child-session.html`), your web browser will display a permission prompt:
   - *"autismscreening wants to Use your camera and Use your microphone"*
2. Click **Allow**.
3. **Webcam:** Position the child comfortably 50–70 cm in front of the screen. The small green box in the top-right overlay confirms real-time face and head posture tracking.
4. **Microphone:** Ensure room background noise is minimized. During Stage 6 (Voice Activity), speaking animal sounds ("Woof-Woof", "Moo") will display real-time vocalization markers.
5. **Privacy Guarantee:** The browser processes pixels and audio waveforms in transient RAM to compute numbers. **No video or audio recordings are saved.**

---

## 🧪 Running Python AI & ML Research Scripts

To run the offline machine learning benchmarks and ablation experiments:

```bash
cd python_ml
pip install -r requirements.txt

# 1. Generate Synthetic Cohort Benchmark Dataset (500 samples)
python generate_synthetic_dataset.py

# 2. Train & Compare 6 Machine Learning Classifiers
python train_models.py

# 3. Run Stage 12 Multimodal Modality Ablation Study
python multimodal_ablation_study.py

# 4. Run Stage 13 ADOS-2 Clinical Reference Concordance Analysis
python ados_validation.py
```

---

## ⚖️ Ethical & Clinical Disclaimer
This system is strictly an **assistive behavioral screening and developmental triage tool**. It does **NOT** independently diagnose Autism Spectrum Disorder. Screenings indicate behavioral characteristics that may benefit from comprehensive clinical developmental evaluation. The Autism Diagnostic Observation Schedule (ADOS-2) administered by certified clinical specialists remains the standardized diagnostic reference.



## Updated child experience and ML flow

The child session now uses eight short, colourful, single-action games with large emoji/SVG illustrations, simple instructions, progress feedback and Pip the penguin guidance. The activities observe attention, social interaction, imitation, play, movement and communication without presenting the child with clinical terminology.

**Multimodal flow**

`Camera + Microphone + Parent Questionnaire → behavioural feature extraction → CNN visual feature layer → Random Forest screening classifier → screening category → professional/ADOS-2 assessment`

The browser CNN uses a pretrained MobileNet CNN only as a visual feature extractor. It is not an autism diagnostic model. The included Java Random Forest is a genuine small decision-tree ensemble trained on synthetic/demo calibration data so the project can run without a separate Python server. Replace the synthetic calibration data with ethically collected, de-identified, clinically labelled data before research/clinical use.

**Parent output**

The Parent Screening Report includes the model architecture, behavioural feature profile, screening category, ADOS-2 reference section, supportive guidance and a **Download PDF Report** button. The PDF is generated by OpenPDF through `/api/report/session/{sessionId}/pdf`.

### Clinical wording

The application is a **screening/triage support prototype**, not a diagnostic system. A screening category indicates that further developmental discussion or assessment may be appropriate; it does not establish or rule out autism. ADOS-2 remains a separate standardized clinician-administered assessment.
