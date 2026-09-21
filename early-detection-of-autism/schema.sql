-- =============================================================================
-- DATABASE SCHEMA: Early Detection of Autism in Children
-- Multimodal Behavioural Learning, Computer Vision, Voice Analysis and ADOS
-- Compatible with MySQL 8.0+ and MariaDB / H2 Database (with MySQL mode)
-- =============================================================================

CREATE DATABASE IF NOT EXISTS autism_screening_db;
USE autism_screening_db;

-- Table 1: users (Parents, Clinicians, Administrators)
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'PARENT', -- 'PARENT', 'CLINICIAN', 'ADMIN'
    consent_given BOOLEAN NOT NULL DEFAULT FALSE,
    consent_timestamp DATETIME NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table 2: children (Anonymized / Protected Child Demographic Record)
CREATE TABLE IF NOT EXISTS children (
    child_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    anonymous_code VARCHAR(64) NOT NULL UNIQUE, -- e.g. 'CH-2026-8912'
    user_id BIGINT NOT NULL,
    age_months INT NOT NULL,                     -- Age in months (e.g. 24 to 72 months)
    gender VARCHAR(20) NOT NULL,                 -- 'MALE', 'FEMALE', 'OTHER'
    preferred_language VARCHAR(50) NOT NULL DEFAULT 'en', -- 'en', 'ml' (Malayalam), etc.
    developmental_concerns TEXT NULL,
    communication_level VARCHAR(50) NOT NULL,    -- 'NON_VERBAL', 'SINGLE_WORDS', 'PHRASES', 'FLUENT'
    previous_assessment_notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Table 3: parent_questionnaire (Stage 1: Standardized Developmental Questionnaire)
CREATE TABLE IF NOT EXISTS parent_questionnaire (
    questionnaire_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL,
    assessment_date DATE NOT NULL,
    q1_social_smile INT NOT NULL,                -- 0: Rarely, 1: Sometimes, 2: Usually
    q2_eye_contact INT NOT NULL,
    q3_response_to_name INT NOT NULL,
    q4_pointing_interest INT NOT NULL,           -- Joint attention pointing
    q5_shared_enjoyment INT NOT NULL,
    q6_imitation INT NOT NULL,                   -- Copies sounds/actions
    q7_repetitive_movements INT NOT NULL,       -- Hand flapping, spinning
    q8_unusual_sensory_interests INT NOT NULL,  -- Smelling/staring at objects
    q9_play_flexibility INT NOT NULL,           -- Rigid vs flexible play
    q10_language_milestone INT NOT NULL,        -- Age-appropriate speech
    total_score DOUBLE NOT NULL,
    questionnaire_risk_indicator VARCHAR(50) NOT NULL, -- 'LOW', 'MODERATE', 'ELEVATED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE
);

-- Table 4: assessment_sessions (Multi-Stage Assessment Workflow Coordinator)
CREATE TABLE IF NOT EXISTS assessment_sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL,
    session_date DATETIME NOT NULL,
    session_number INT NOT NULL DEFAULT 1,      -- 1, 2, 3... for longitudinal progress
    status VARCHAR(50) NOT NULL DEFAULT 'IN_PROGRESS', -- 'IN_PROGRESS', 'COMPLETED', 'ABORTED'
    total_duration_seconds INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME NULL,
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE
);

-- Table 5: activities (Activity Registry for Stages 2 to 9)
CREATE TABLE IF NOT EXISTS activities (
    activity_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    stage_number INT NOT NULL,                   -- 2 through 9
    stage_name VARCHAR(100) NOT NULL,            -- 'WARM_UP', 'VISUAL_ATTENTION', 'IMITATION', etc.
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    duration_seconds INT DEFAULT 0,
    interaction_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES assessment_sessions(session_id) ON DELETE CASCADE
);

-- Table 6: camera_features (Computer Vision / Pose / Facial Tracking Metrics)
CREATE TABLE IF NOT EXISTS camera_features (
    camera_feature_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    stage_number INT NOT NULL,
    face_detected_ratio DOUBLE NOT NULL,        -- Fraction of time face was detected (0.0 to 1.0)
    head_movement_variability DOUBLE NOT NULL,  -- Head tilt/yaw standard deviation
    gaze_screen_duration_ratio DOUBLE NOT NULL, -- Screen looking time vs looking away
    gaze_switch_frequency DOUBLE NOT NULL,      -- Switches per minute
    imitation_pose_similarity DOUBLE NOT NULL,  -- Pose cosine similarity (0.0 to 1.0)
    movement_smoothness DOUBLE NOT NULL,        -- Jerk / acceleration variance
    repetitive_hand_frequency DOUBLE NOT NULL,  -- Oscillations per minute
    rocking_motion_detected BOOLEAN NOT NULL DEFAULT FALSE,
    response_latency_ms INT NOT NULL,           -- Average response reaction time
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES assessment_sessions(session_id) ON DELETE CASCADE
);

-- Table 7: voice_features (Acoustic and Communication Behavioural Metrics)
CREATE TABLE IF NOT EXISTS voice_features (
    voice_feature_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    stage_number INT NOT NULL,
    vocalization_count INT NOT NULL,            -- Number of speech/sound utterances
    speech_duration_seconds DOUBLE NOT NULL,    -- Total vocal time
    response_latency_ms INT NOT NULL,           -- Silence before speaking
    pause_duration_average DOUBLE NOT NULL,     -- Mean inter-utterance pause (seconds)
    word_count INT NOT NULL,                    -- Recognized words count
    turn_taking_success_rate DOUBLE NOT NULL,   -- Coordinated turn taking (0.0 to 1.0)
    echolalia_repetition_index DOUBLE NOT NULL, -- Immediate repetition pattern metric
    audio_clarity_score DOUBLE NOT NULL,        -- Signal-to-noise ratio / intelligibility proxy
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES assessment_sessions(session_id) ON DELETE CASCADE
);

-- Table 8: behaviour_features (Categorized Aggregated Behaviour Matrix A to J)
CREATE TABLE IF NOT EXISTS behaviour_features (
    behaviour_feature_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL UNIQUE,
    social_interaction_score DOUBLE NOT NULL,   -- Category A: 0.0 to 100.0
    communication_score DOUBLE NOT NULL,        -- Category B: 0.0 to 100.0
    attention_score DOUBLE NOT NULL,            -- Category C: 0.0 to 100.0
    motor_behaviour_score DOUBLE NOT NULL,      -- Category D: 0.0 to 100.0
    imitation_score DOUBLE NOT NULL,            -- Category E: 0.0 to 100.0
    play_behaviour_score DOUBLE NOT NULL,       -- Category F: 0.0 to 100.0
    repetitive_behaviour_score DOUBLE NOT NULL, -- Category G: 0.0 to 100.0
    response_latency_score DOUBLE NOT NULL,     -- Category H: 0.0 to 100.0
    voice_metric_score DOUBLE NOT NULL,         -- Category I: 0.0 to 100.0
    camera_movement_score DOUBLE NOT NULL,      -- Category J: 0.0 to 100.0
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES assessment_sessions(session_id) ON DELETE CASCADE
);

-- Table 9: ml_results (Machine Learning Multi-algorithm & Fusion Results)
CREATE TABLE IF NOT EXISTS ml_results (
    ml_result_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL UNIQUE,
    model_name VARCHAR(100) NOT NULL,           -- 'RandomForest_Ensemble', 'XGBoost', etc.
    modality_configuration VARCHAR(100) NOT NULL, -- 'FULL_MULTIMODAL', 'CAMERA_ONLY', etc.
    risk_score DOUBLE NOT NULL,                 -- Continuous score: 0.0 to 1.0
    screening_category VARCHAR(50) NOT NULL,    -- 'LOWER_OBSERVED_CONCERN', 'MODERATE_OBSERVED_CONCERN', 'HIGHER_OBSERVED_CONCERN'
    model_confidence DOUBLE NOT NULL,           -- 0.0 to 1.0
    feature_importance_summary TEXT NULL,       -- Top contributing behavioral indicators (JSON)
    disclaimer_note TEXT NOT NULL,              -- Mandatory non-diagnostic disclaimer
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES assessment_sessions(session_id) ON DELETE CASCADE
);

-- Table 10: clinical_reference (ADOS-2 Standardized Clinical Reference Labels)
CREATE TABLE IF NOT EXISTS clinical_reference (
    reference_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL,
    ados_module VARCHAR(50) NOT NULL,           -- 'Module T', 'Module 1', 'Module 2', 'Module 3'
    ados_social_affect_score INT NOT NULL,
    ados_rrb_score INT NOT NULL,                -- Restricted & Repetitive Behaviour
    ados_total_cutoff_score INT NOT NULL,
    ados_classification VARCHAR(50) NOT NULL,   -- 'NON_SPECTRUM', 'AUTISM_SPECTRUM', 'AUTISM'
    clinician_notes TEXT NULL,
    evaluation_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE
);

-- Table 11: progress_records (Longitudinal Behavioural Tracking Across Sessions)
CREATE TABLE IF NOT EXISTS progress_records (
    progress_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL,
    session_id BIGINT NOT NULL,
    session_sequence INT NOT NULL,              -- Assessment 1, 2, 3...
    record_date DATE NOT NULL,
    overall_risk_score DOUBLE NOT NULL,
    attention_trend DOUBLE NOT NULL,
    communication_trend DOUBLE NOT NULL,
    social_trend DOUBLE NOT NULL,
    motor_trend DOUBLE NOT NULL,
    repetitive_trend DOUBLE NOT NULL,
    play_trend DOUBLE NOT NULL,
    notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE,
    FOREIGN KEY (session_id) REFERENCES assessment_sessions(session_id) ON DELETE CASCADE
);

-- Table 12: recommendations (Actionable Supportive Non-Diagnostic Guidance)
CREATE TABLE IF NOT EXISTS recommendations (
    recommendation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,              -- 'COMMUNICATION', 'SOCIAL_INTERACTION', 'MOTOR', 'ATTENTION'
    guidance_title VARCHAR(200) NOT NULL,
    guidance_text TEXT NOT NULL,
    priority_level VARCHAR(20) NOT NULL DEFAULT 'MEDIUM', -- 'LOW', 'MEDIUM', 'HIGH'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES assessment_sessions(session_id) ON DELETE CASCADE
);

