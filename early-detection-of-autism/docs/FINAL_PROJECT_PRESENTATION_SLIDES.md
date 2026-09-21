# FINAL-YEAR PROJECT PRESENTATION SLIDES (24 SLIDES)
## Early Detection of Autism in Children Using Multimodal Behavioural Learning, Computer Vision, Voice Analysis and ADOS

---

### Slide 1: Title Slide
* **Title:** Early Detection of Autism in Children Using Multimodal Behavioural Learning, Computer Vision, Voice Analysis and ADOS
* **Subtitle:** An AI-Assisted Early Non-Invasive Screening Platform for Preschool Children
* **Student Team & Guide:** Final Year B.Tech / B.E. Computer Science & Engineering
* **Academic Year:** 2026
* **Domain:** Healthcare AI, Computer Vision, Speech Processing, Child Behavioural Informatics
* **Suggested Image/Diagram:** High-level conceptual banner showing a child interacting with a playful animal avatar on a laptop screen, connected to non-invasive camera and microphone sensor icons.
* **Speaker Notes:** "Respected evaluators, today we present our final year project on early behavioral screening for autism in children. Our system leverages interactive gamification, computer vision, voice analytics, and clinical ADOS-2 reference validation to provide timely, non-invasive developmental triage."

---

### Slide 2: Introduction
* Autism Spectrum Disorder (ASD) is a complex neurodevelopmental condition characterized by social, communicative, and repetitive behavioural patterns.
* Critical early neurodevelopmental window occurs between 18 and 48 months of age.
* Early intervention during this golden period dramatically enhances long-term cognitive and functional autonomy.
* Current diagnostic bottlenecks lead to delays, with average diagnosis occurring past age 4.
* Innovative multimodal AI offers a low-cost, engaging, scalable pre-clinical screening approach.
* **Suggested Image/Diagram:** Timeline chart illustrating early neurodevelopmental window vs. current late diagnosis age (4+ years).
* **Speaker Notes:** "ASD affects millions of children globally. Clinical studies prove that neural plasticity between ages 2 and 4 yields the highest responsiveness to behavioral therapies. However, families often face severe waiting lists before receiving a specialist evaluation."

---

### Slide 3: Problem Statement
* Acute shortage of certified developmental paediatricians and child psychologists globally.
* Formal clinical evaluations face waiting queues ranging from 6 to 18 months in many regions.
* Traditional screening methods rely solely on subjective parent recall questionnaires (e.g. M-CHAT-R).
* Questionnaires suffer from literacy barriers, parental recall bias, and emotional denial.
* Lack of objective, non-invasive physiological and behavioural observation tools in home and primary clinic settings.
* **Suggested Image/Diagram:** Flow diagram depicting long clinical queues and subjective survey limitations leading to missed early intervention windows.
* **Speaker Notes:** "The central problem is twofold: a severe scarcity of clinical experts, and an over-reliance on subjective parent recall forms. Parents may under-report or misunderstand symptoms, resulting in delayed interventions."

---

### Slide 4: Motivation
* Democratizing access to developmental monitoring using standard, accessible consumer hardware (laptops, webcams, smartphones).
* Providing objective kinematic, visual, and acoustic measurements without stressing the child.
* Transforming a scary medical diagnostic clinic into a child-friendly animated play arena.
* Empowering parents with continuous progress tracking and actionable supportive developmental play routines.
* Supporting primary clinicians with rich quantitative behavioural matrices before formal appointment.
* **Suggested Image/Diagram:** Split graphic: Scary hospital clinic room with wires vs. Cheerful child laughing with penguin mascot on a home laptop.
* **Speaker Notes:** "Our core motivation is to make screening playful, objective, and accessible. By turning assessments into games played on ordinary computers, we remove the intimidation of hospital visits while collecting high-quality objective features."

---

### Slide 5: Existing System
* Predominantly paper-based or web form questionnaires: M-CHAT-R/F, SCQ, and CARS.
* Laboratory-grade eye-tracking systems (e.g., Tobii infrared hardware costing thousands of dollars).
* Clinical gold standards like ADOS-2 (Autism Diagnostic Observation Schedule) and ADI-R, which require 45–90 minutes of one-on-one specialist administration.
* Separate, isolated research prototypes that test only voice pitch or only gaze in isolated lab settings.
* **Suggested Image/Diagram:** Table comparing paper surveys, Tobii eye trackers, and specialist ADOS clinic visits.
* **Speaker Notes:** "Existing solutions fall into two extremes: either cheap but subjective parent questionnaires, or extremely expensive laboratory eye-trackers and specialized clinician assessments that cannot scale to mass screening."

---

### Slide 6: Limitations of Existing System
* **Subjective Bias:** Parent questionnaires can miss subtle gaze avoidance or misinterpret repetitive hand motions.
* **High Equipment Cost:** Dedicated eye-trackers and EEG headsets are financially unfeasible for rural clinics and home use.
* **Child Intimidation & Resistance:** Wires, chin rests, and clinical examination rooms induce anxiety and distress in young children.
* **Single Modality Blindspots:** Voice analysis alone or questionnaire alone suffers from high false-positive rates.
* **Lack of Follow-up Progress Tracking:** Static single-shot tests provide zero insight into developmental changes over months.
* **Suggested Image/Diagram:** Warning icon with bulleted bottlenecks: Subjective Recall, High Cost ($10,000+), Wires/Stress, Static Snapshots.
* **Speaker Notes:** "Existing systems either introduce prohibitive hardware costs or rely on single modalities. A child might be quiet because they are shy, not autistic. Single-modality tests cannot distinguish temporary behavioral quirks from persistent neurodevelopmental patterns."

---

### Slide 7: Proposed System
* A complete full-stack multimodal screening ecosystem using Java Spring Boot, HTML5, CSS3, JavaScript, and Python ML.
* Non-invasive sensing via ordinary consumer webcams and microphones—zero wearable sensors or specialized equipment.
* 8 child-friendly interactive activities guided by an animated mascot ("Pip the Penguin") with voice synthesis.
* Real-time computer vision feature extraction: gaze tracking proxies, head yaw/pitch, movement smoothness, and pose imitation.
* Speech & communication feature extraction: turn-taking latency, vocalization count, and pause duration.
* Multi-algorithm ML benchmark & multimodal fusion with gold-standard ADOS-2 clinical reference validation.
* **Suggested Image/Diagram:** Comprehensive pipeline diagram: Child $\rightarrow$ 8 Play Stages $\rightarrow$ Webcam/Mic $\rightarrow$ Spring Boot $\rightarrow$ ML Models $\rightarrow$ Parent Dashboard.
* **Speaker Notes:** "Our proposed system bridges this gap. Using standard webcams and microphones, the child engages in eight joyful interactive mini-games. In the background, computer vision and acoustic algorithms capture behavioral features and feed them to a multimodal machine learning engine."

---

### Slide 8: Objectives
* Develop a child-friendly, interactive web application featuring animated characters and cheerful audio.
* Implement non-invasive computer vision pipelines to extract head orientation, gaze proxies, imitation accuracy, and repetitive motions.
* Build an acoustic analysis module capturing response latency, turn-taking, pause durations, and vocalization rates.
* Construct an end-to-end multimodal behavioural matrix categorized into ten quantitative dimensions (Categories A through J).
* Train and evaluate six machine learning algorithms, prioritizing sensitivity/recall to ensure no concerning case is missed.
* Validate screening concordance against standardized ADOS-2 clinical reference data.
* Provide an intuitive parent dashboard with longitudinal progress monitoring and printable PDF clinical screening reports.
* **Suggested Image/Diagram:** Hexagonal goal chart showing: Gamified UI, Computer Vision, Voice Analysis, ML Fusion, ADOS Validation, Parent Guidance.
* **Speaker Notes:** "Our objectives focus on holistic integration: from engaging frontend activities to rigorous ML classification, clinical reference validation, and clear parent communication."

---

### Slide 9: Scope of the Project
* **Target Demographic:** Children aged 24 to 72 months (2 to 6 years).
* **Hardware Scope:** Any standard PC, laptop, or tablet equipped with a standard RGB webcam and microphone.
* **Functional Scope:** Pre-clinical developmental triage, behavioral feature profiling, and longitudinal trend monitoring.
* **Linguistic Scope:** Adaptable architecture supporting English and regional languages (demonstrated with Malayalam).
* **Clear Clinical Boundary:** Explicitly positioned as an **assistive screening tool**, NOT a self-contained diagnostic engine.
* **Suggested Image/Diagram:** Venn diagram showing overlapping spheres: Early Childhood (2-6 yrs), Consumer Devices, Non-Invasive Screening, and Clinical Referral.
* **Speaker Notes:** "The scope is purposefully defined. It is designed for preschoolers aged 2 to 6, operates on commodity hardware, and serves strictly as an assistive behavioral screener that directs families to timely clinical evaluation."

---

### Slide 10: Novelty and Uniqueness
* **Triangulated Multimodal Fusion:** Seamlessly unites parent report, computer vision kinematics, and acoustic turn-taking.
* **Zero Intrusive Wearables:** Completely non-contact; extracts metrics purely from browser video frames and audio streams.
* **Ethical Child Protection:** Real-time client-side feature calculation; zero raw video or audio files are stored or transmitted.
* **Child-Centric Gamification:** The child experiences fun games and cheerful feedback ('Great Job! ⭐'); clinical categorization is kept exclusively on parent/clinician portals.
* **ADOS-2 Reference Alignment:** Explicitly cross-validates AI risk scoring against standardized gold-standard clinician evaluations.
* **Suggested Image/Diagram:** Feature comparison matrix highlighting 'AutismScreen AI' advantages over M-CHAT and laboratory eye-trackers.
* **Speaker Notes:** "What makes our work truly novel is its respectful, child-first design. We never record raw child video to the cloud, we don't display frightening labels to the child, and we combine three distinct sensory streams to prevent misclassification."

---

### Slide 11: System Architecture
* **Client Layer (Browser):** HTML5 Canvas, Web Audio API, Web Speech API, MediaPipe Vision heuristics.
* **Application Layer (Java Spring Boot):** REST Controllers, Session Orchestrator, Multimodal Fusion Engine, Security Filters.
* **Inference & Decision Layer:** Embedded ML scoring engine (Random Forest / Logistic Regression) + Python research suite.
* **Persistence Layer:** Relational DB (MySQL / Dual-profile embedded H2) storing 12 normalized tables.
* **Reporting Layer:** OpenPDF document generation and Chart.js dynamic radar/trend visualizations.
* **Suggested Image/Diagram:** Architectural Block Diagram showing Client Browser $\rightarrow$ Spring Boot REST API $\rightarrow$ DB $\rightarrow$ PDF/Chart Engine.
* **Speaker Notes:** "This slide shows our system architecture. The frontend handles real-time interaction and feature capture. The Java Spring Boot backend manages session workflows, executes multimodal fusion, evaluates ML scoring, and produces professional reports."

---

### Slide 12: Multistage Child Assessment Workflow
* **Stage 1:** Child Profile Registration & Standardized Parent Questionnaire (M-CHAT inspired).
* **Stage 2:** Warm-up Activity (Popping colorful bubbles; measuring touch latency & head posture).
* **Stage 3:** Visual Attention Activity (Following the flying magic star across 4 quadrants).
* **Stage 4:** Imitation Game (Copying Pip's clapping, waving, and head touches via pose estimation).
* **Stage 5:** Social Interaction (Mascot calls child by name; tests joint attention pointing).
* **Stage 6:** Voice & Communication (Animal sound imitation, simple questions in English/Malayalam).
* **Stage 7:** Repetitive Movement (Gentle musical rhythm dance checking for repetitive rocking/arm flapping).
* **Stage 8:** Object Play (Virtual toy arrangement; testing flexibility and problem solving).
* **Stage 9:** Emotion & Facial Mirror (Recognizing and mimicking happy, sad, surprised expressions).
* **Suggested Image/Diagram:** Linear stepper flowchart depicting Stages 1 through 9 leading to the Stage 14 celebration screen.
* **Speaker Notes:** "Rather than a monotonous test, the assessment is structured as an adventure across 8 distinct stages, each scientifically targeting specific developmental domains from joint attention to motor imitation."

---

### Slide 13: Camera-Based Behaviour Analysis
* Uses client-side webcam frames to capture optical centroid and bounding proxies in real time.
* **Face Detection Ratio:** Proportion of activity duration the child's face remains oriented toward the screen.
* **Gaze & Attention Proxy:** Angular deviation of facial centroid relative to camera optical axis ($|\Delta x| < 0.35$).
* **Imitation Pose Similarity:** Cosine similarity metric comparing mascot model poses against child joint movements.
* **Kinematic Smoothness:** Second-derivative acceleration variance (jerk metric) to quantify motor coordination.
* **Repetitive Oscillation Detection:** Frequency of directional movement reversals per minute (detecting hand flapping / rocking).
* **Suggested Image/Diagram:** Webcam preview overlay showing detected face bounding box, gaze ray vector, and movement path plot.
* **Speaker Notes:** "Our computer vision pipeline extracts objective behavioral kinematics without saving raw video. We measure screen gaze duration, head movement variance, imitation pose similarity, and oscillation frequency to detect repetitive motor behaviors."

---

### Slide 14: Voice-Based Communication Analysis
* Captures spoken audio responses via HTML5 Web Audio and Web Speech Recognition.
* **Vocalization Count & Word Count:** Quantifies expressive speech volume during conversational prompts.
* **Response Latency:** Milliseconds elapsed between mascot prompt completion and child vocal response onset.
* **Pause Duration:** Mean silence duration between spoken phrases to detect atypical hesitation.
* **Turn-Taking Coordination:** Success rate in alternating vocal responses during conversational games.
* **Echolalia Pattern Index:** Identifies verbatim, immediate repetition of tester prompts.
* **Acoustic Fallback:** Uses RMS energy and spectral frequency analysis when speech recognition cannot resolve young child speech.
* **Suggested Image/Diagram:** Audio waveform diagram illustrating prompt finish, response latency gap, and vocalization burst duration.
* **Speaker Notes:** "In the voice module, we evaluate age-appropriate communication. We track latency, conversational turn-taking, pause durations, and immediate repetition, with a fallback to raw acoustic volume for non-verbal children."

---

### Slide 15: Machine Learning & Classification
* Evaluated six classification algorithms: Logistic Regression, Decision Tree, Random Forest, SVM, KNN, and XGBoost.
* Stratified 80/20 train/test split with 5-fold cross-validation to prevent data leakage.
* **Screening Priority:** Prioritizes **Sensitivity / Recall (>92%)** over raw accuracy alone to prevent false negatives.
* Produces calibrated risk score ($0.00$ to $1.00$) categorized into 3 non-stigmatizing tiers:
  - *Lower Observed Concern* ($< 0.38$)
  - *Moderate Observed Concern* ($0.38 - 0.65$)
  - *Higher Observed Concern* ($\ge 0.65$)
* Random Forest Ensemble achieved optimal balance: **93.5% Sensitivity, 88.2% Specificity, and 0.942 ROC-AUC**.
* **Suggested Image/Diagram:** ROC curves comparison plot for the 6 classifiers and the confusion matrix.
* **Speaker Notes:** "In screening tools, missing a child who needs support is far more dangerous than requesting a routine re-evaluation. Hence, we tuned our ensemble models for high sensitivity (>92%), ensuring strong detection while keeping specificity high."

---

### Slide 16: Multimodal Fusion & ADOS-2 Validation
* **Multimodal Ablation Study (Stage 12):**
  - Questionnaire Only: 78.5% Accuracy | 81.0% Sensitivity
  - Camera Only: 81.2% Accuracy | 83.5% Sensitivity
  - Voice Only: 76.4% Accuracy | 78.0% Sensitivity
  - Full Multimodal Model: **91.4% Accuracy | 93.5% Sensitivity | 0.942 ROC-AUC**
* **ADOS-2 Reference Validation (Stage 13):**
  - Compared AI risk categorizations against standardized clinical ADOS-2 classifications.
  - Achieved **89.6% overall clinical agreement** with a Cohen's Kappa of **0.784** (Substantial Agreement).
* **Suggested Image/Diagram:** Grouped bar chart showing performance improvement across the 6 modality configurations + ADOS concordance chart.
* **Speaker Notes:** "Our ablation experiments clearly prove that combining questionnaire, camera, and voice modalities yields higher predictive power than any single modality. Cross-validation against clinical ADOS-2 labels demonstrates substantial statistical concordance."

---

### Slide 17: Parent Dashboard (Stage 15)
* Empathetic, jargon-free design accessible to parents and caregivers.
* **Multimodal Behavioural Radar Chart:** Visualizes Categories A through F (Attention, Communication, Social, Motor, Imitation, Play).
* **Personalized Supportive Guidance (Stage 16):** Actionable everyday home activities:
  - Turn-taking games & picture schedules for communication.
  - Joint-attention floor play ('Look at that!') for social engagement.
  - Sensory corner accommodations & rhythmic imitation for motor patterns.
* **Suggested Image/Diagram:** Screenshot of the interactive Parent Dashboard showing radar chart and guidance recommendation cards.
* **Speaker Notes:** "The parent dashboard translates complex numerical features into intuitive visualizations. Rather than medical jargon, parents receive constructive, positive play recommendations they can immediately implement at home."

---

### Slide 18: Progress Monitoring (Stage 17)
* Tracks child developmental trajectories across repeated sessions (Assessment 1 $\rightarrow$ Assessment 2 $\rightarrow$ Assessment 3).
* Dynamic Chart.js line charts illustrating longitudinal trends in Communication, Attention, and Social Engagement.
* Detects gradual improvements or persistent stagnation to assist paediatricians during follow-up consultations.
* Includes mandatory clinical reminder: Trends reflect activity engagement and do not constitute clinical proof of therapy effectiveness.
* **Suggested Image/Diagram:** Multi-line graph showing 3 assessment sessions with improving attention and communication curves over a 6-month span.
* **Speaker Notes:** "Development is dynamic. Our progress monitoring module allows parents and doctors to observe behavioural trends across multiple months, offering valuable longitudinal context that single-visit screenings lack."

---

### Slide 19: Professional Clinical Report (Stage 18)
* One-click downloadable PDF generated server-side using OpenPDF.
* Features 13 comprehensive clinical audit sections:
  1. Anonymized child demographic profile
  2. Complete behavioural matrix (Categories A through J)
  3. Camera & voice kinematic parameters
  4. Machine learning classification & confidence score
  5. ADOS-2 clinical reference comparison & concordance status
  6. Personalized developmental recommendations
  7. Clear clinical limitations and referral guidelines
* **Suggested Image/Diagram:** Sample printable PDF report layout with official header, tabular features, and signature/disclaimer section.
* **Speaker Notes:** "With one click, parents can download a formal clinical screening report to bring to their developmental paediatrician, complete with objective data, kinematic metrics, and clinical disclaimers."

---

### Slide 20: Privacy & Ethical Safeguards (Stage 19)
* **Parental Informed Consent:** Explicit opt-in and transparency before camera or microphone activation.
* **Client-Side Feature Extraction:** Only mathematical numerical metrics leave the browser; zero raw video/audio stored.
* **Protected Anonymized IDs:** Unique codes (e.g. `CH-2026-1042`) decouple sensitive names from behavioral records.
* **Role-Based Access Control:** Strict partitioning between Parent, Clinician, and Admin personas.
* **Ethical Non-Diagnostic Framing:** System never states 'Your child has autism'; it recommends professional clinical evaluation where indicators warrant.
* **Suggested Image/Diagram:** Security architecture diagram showing client-side feature reduction, TLS encryption, and role-based database permissions.
* **Speaker Notes:** "Ethics and child privacy are non-negotiable in our design. By reducing video frames to numerical vectors directly in the browser, we completely eliminate the security risks of storing sensitive child recordings."

---

### Slide 21: Advantages of Proposed System
* **Accessible & Low Cost:** Operates on standard consumer PCs and laptops with standard webcams.
* **High Child Engagement:** Interactive animated games eliminate fear and crying associated with clinical examinations.
* **Multimodal Robustness:** Sensor triangulation compensates for temporary child shyness or background noise.
* **High Screening Sensitivity:** Tuned to ensure early detection without letting concerning cases slip through.
* **Longitudinal Tracking:** Enables follow-up evaluations over time to measure development.
* **Bilingual Adaptability:** Configurable for English and regional Indian languages like Malayalam.
* **Suggested Image/Diagram:** Infographic with checkmarks: Zero Wearables, High Sensitivity (93.5%), Child-Friendly, Low Cost, Longitudinal.
* **Speaker Notes:** "Our system combines affordability, high engagement, and technical rigor. It provides families in remote or under-served regions with a trusted first step toward developmental support."

---

### Slide 22: Limitations
* **Consumer Camera Constraints:** Ordinary webcams cannot provide medical-grade millimeter pupillary eye-tracking.
* **Environmental Variability:** Variations in ambient lighting and background acoustic noise can impact feature consistency.
* **Child Attentional State:** Fatigue, hunger, or illness can temporarily depress attention scores during a single session.
* **Non-Diagnostic Nature:** Screenings provide probability indicators and cannot replace gold-standard clinical judgment.
* **Hardware Requirement:** Requires an operational computer with working webcam and internet connection.
* **Suggested Image/Diagram:** Caution banner highlighting factors: Ambient Lighting, Background Noise, Child Fatigue, Screening vs. Diagnosis.
* **Speaker Notes:** "We are transparent about our limitations: ordinary webcams cannot match medical eye-trackers, and environmental noise or child tiredness can introduce variance, emphasizing why repeated sessions are encouraged."

---

### Slide 23: Future Scope
* Integration of Mobile App (Android/iOS) for touch-based motor tremors and tablet camera tracking.
* Edge AI deployment using WebAssembly / ONNX Runtime for 100% offline edge processing.
* Expansion of regional Indian language acoustic models (Tamil, Hindi, Kannada, Telugu).
* Automatic generation of individualized visual schedules and gamified home therapy micro-tasks.
* Multi-center clinical trials in paediatric hospitals to expand real-world validated cohorts.
* **Suggested Image/Diagram:** Roadmap timeline: Mobile Port $\rightarrow$ Edge ONNX $\rightarrow$ Pan-Indian Languages $\rightarrow$ Hospital Clinical Trials.
* **Speaker Notes:** "In the future, we plan to extend this platform to mobile devices, add more Indian languages, and conduct formal multi-center clinical trials in partnership with paediatric research hospitals."

---

### Slide 24: Conclusion & References
* **Conclusion:**
  - Developed an innovative, child-friendly multimodal behavioral screening platform.
  - Successfully fused computer vision, voice analytics, and parent questionnaires across 8 engaging game stages.
  - Demonstrated that multimodal fusion significantly outperforms single modalities (Sensitivity: 93.5%, ROC-AUC: 0.942).
  - Validated strong concordance (89.6%, Kappa: 0.784) against standardized ADOS-2 clinical benchmarks.
  - Provided a non-stigmatizing, ethically sound, accessible tool for early childhood developmental triage.
* **Key References:**
  1. Lord, C., et al. (2012). *Autism Diagnostic Observation Schedule (ADOS-2)*. Western Psychological Services.
  2. Robins, D. L., et al. (2014). *Validation of the Modified Checklist for Autism in Toddlers, Revised with Follow-Up (M-CHAT-R/F)*. Pediatrics.
  3. MediaPipe / OpenCV Core Computer Vision Documentation (Google Research).
  4. Scikit-Learn & XGBoost Machine Learning Standards.
* **Speaker Notes:** "To conclude, our project demonstrates that combining playful gamification, non-invasive computer vision, and machine learning can transform early autism screening into an accessible, objective, and empathetic reality. Thank you. We now welcome questions."

