"""
SYNTHETIC MULTIMODAL AUTISM SCREENING BENCHMARK GENERATOR
=============================================================================
NOTICE: This script generates clearly labelled SYNTHETIC/DEMO data designed
for software development, algorithm evaluation, and pipeline testing.
It is NOT real patient clinical data. For genuine medical research, ethically
collected and institutional-review-board (IRB) approved data with explicit
parental informed consent would be strictly required.
=============================================================================
"""

import numpy as np
import pandas as pd

def generate_synthetic_dataset(num_samples=500, random_seed=42):
    np.random.seed(random_seed)

    records = []
    for i in range(num_samples):
        child_id = f"CH-2026-{1000 + i}"
        age_months = int(np.random.choice(range(24, 73))) # 2 to 6 years old
        gender = np.random.choice(['MALE', 'FEMALE', 'OTHER'], p=[0.75, 0.23, 0.02])
        language = np.random.choice(['en', 'ml'], p=[0.70, 0.30])

        # Underlying latent screening status: 0 = Typical / Lower concern (60%), 1 = Elevated concern (40%)
        is_concern = np.random.choice([0, 1], p=[0.60, 0.40])

        # Stage 1: Questionnaire Features
        if is_concern == 1:
            q_social = np.clip(np.random.normal(0.8, 0.5), 0, 2)
            q_eye = np.clip(np.random.normal(0.7, 0.5), 0, 2)
            q_name = np.clip(np.random.normal(0.6, 0.5), 0, 2)
            q_point = np.clip(np.random.normal(0.7, 0.5), 0, 2)
            q_shared = np.clip(np.random.normal(0.8, 0.5), 0, 2)
            q_imit = np.clip(np.random.normal(0.9, 0.5), 0, 2)
            q_rep = np.clip(np.random.normal(1.6, 0.4), 0, 2) # elevated
            q_sensory = np.clip(np.random.normal(1.4, 0.5), 0, 2) # elevated
            q_play = np.clip(np.random.normal(0.7, 0.5), 0, 2)
            q_lang = np.clip(np.random.normal(0.8, 0.5), 0, 2)
        else:
            q_social = np.clip(np.random.normal(1.8, 0.3), 0, 2)
            q_eye = np.clip(np.random.normal(1.7, 0.4), 0, 2)
            q_name = np.clip(np.random.normal(1.8, 0.3), 0, 2)
            q_point = np.clip(np.random.normal(1.7, 0.4), 0, 2)
            q_shared = np.clip(np.random.normal(1.8, 0.3), 0, 2)
            q_imit = np.clip(np.random.normal(1.7, 0.4), 0, 2)
            q_rep = np.clip(np.random.normal(0.3, 0.4), 0, 2)
            q_sensory = np.clip(np.random.normal(0.4, 0.4), 0, 2)
            q_play = np.clip(np.random.normal(1.8, 0.4), 0, 2)
            q_lang = np.clip(np.random.normal(1.7, 0.4), 0, 2)

        q_total = (q_social + q_eye + q_name + q_point + q_shared + q_imit + q_play + q_lang) + ((2 - q_rep) + (2 - q_sensory))

        # Camera Features (Stages 2-5, 7, 9)
        if is_concern == 1:
            face_ratio = np.clip(np.random.normal(0.70, 0.12), 0.3, 0.95)
            head_variability = np.clip(np.random.normal(0.25, 0.08), 0.08, 0.45)
            gaze_screen_ratio = np.clip(np.random.normal(0.58, 0.14), 0.25, 0.85)
            gaze_switches = np.clip(np.random.normal(6.5, 2.0), 2.0, 14.0)
            imitation_pose = np.clip(np.random.normal(0.58, 0.15), 0.2, 0.85)
            movement_smoothness = np.clip(np.random.normal(0.60, 0.12), 0.3, 0.88)
            repetitive_hand_freq = np.clip(np.random.normal(4.2, 1.8), 0.5, 12.0)
            rocking_detected = 1 if np.random.rand() < 0.28 else 0
            cam_latency = int(np.clip(np.random.normal(1650, 350), 900, 3200))
        else:
            face_ratio = np.clip(np.random.normal(0.88, 0.08), 0.65, 0.99)
            head_variability = np.clip(np.random.normal(0.14, 0.04), 0.05, 0.25)
            gaze_screen_ratio = np.clip(np.random.normal(0.82, 0.09), 0.60, 0.98)
            gaze_switches = np.clip(np.random.normal(3.8, 1.2), 1.0, 7.0)
            imitation_pose = np.clip(np.random.normal(0.82, 0.10), 0.60, 0.98)
            movement_smoothness = np.clip(np.random.normal(0.82, 0.08), 0.65, 0.98)
            repetitive_hand_freq = np.clip(np.random.normal(1.2, 0.8), 0.0, 3.5)
            rocking_detected = 1 if np.random.rand() < 0.03 else 0
            cam_latency = int(np.clip(np.random.normal(1100, 200), 700, 1800))

        # Voice Features (Stage 6)
        if is_concern == 1:
            vocalization_count = int(np.clip(np.random.normal(4, 2), 0, 12))
            speech_duration_sec = np.clip(np.random.normal(3.5, 1.8), 0.5, 9.0)
            voice_latency_ms = int(np.clip(np.random.normal(1750, 400), 950, 3500))
            pause_duration_avg = np.clip(np.random.normal(2.1, 0.6), 0.8, 4.0)
            word_count = int(np.clip(np.random.normal(3, 2), 0, 10))
            turn_taking_rate = np.clip(np.random.normal(0.52, 0.16), 0.15, 0.80)
            echolalia_index = np.clip(np.random.normal(0.32, 0.15), 0.0, 0.85)
        else:
            vocalization_count = int(np.clip(np.random.normal(9, 3), 4, 20))
            speech_duration_sec = np.clip(np.random.normal(7.2, 2.0), 3.0, 15.0)
            voice_latency_ms = int(np.clip(np.random.normal(1200, 220), 750, 2000))
            pause_duration_avg = np.clip(np.random.normal(1.1, 0.3), 0.4, 2.0)
            word_count = int(np.clip(np.random.normal(9, 3), 3, 22))
            turn_taking_rate = np.clip(np.random.normal(0.85, 0.09), 0.65, 0.98)
            echolalia_index = np.clip(np.random.normal(0.06, 0.04), 0.0, 0.20)

        # Categorized Numerical Behavioural Scores (Categories A through J, 0-100)
        social_score = np.clip((q_social * 20) + (face_ratio * 30) + (turn_taking_rate * 30), 0, 100)
        comm_score = np.clip((q_lang * 20) + (word_count * 3) + ((1.0 - echolalia_index) * 35), 0, 100)
        attention_score = np.clip((gaze_screen_ratio * 60) + (face_ratio * 40), 0, 100)
        motor_score = np.clip((movement_smoothness * 60) + (100 - repetitive_hand_freq * 8) * 0.4, 0, 100)
        imitation_score = np.clip((imitation_pose * 70) + (q_imit * 15), 0, 100)
        play_score = np.clip((q_play * 25) + (movement_smoothness * 35) + (gaze_screen_ratio * 20), 0, 100)
        repetitive_score = np.clip((repetitive_hand_freq * 8) + (echolalia_index * 35) + (rocking_detected * 20), 0, 100)
        latency_score = np.clip(115 - ((cam_latency + voice_latency_ms) / 50.0), 0, 100)
        voice_score = np.clip((turn_taking_rate * 50) + ((1.0 - echolalia_index) * 30) + (vocalization_count * 2), 0, 100)
        camera_score = np.clip((movement_smoothness * 50) + (face_ratio * 30) + (gaze_screen_ratio * 20), 0, 100)

        # ADOS-2 Clinical Reference Labels (Stage 13)
        if is_concern == 1:
            ados_sa = int(np.clip(np.random.normal(8.5, 1.8), 5, 14))
            ados_rrb = int(np.clip(np.random.normal(3.2, 1.2), 1, 6))
            ados_total = ados_sa + ados_rrb
            ados_class = 'AUTISM' if ados_total >= 11 else 'AUTISM_SPECTRUM'
        else:
            ados_sa = int(np.clip(np.random.normal(2.5, 1.2), 0, 5))
            ados_rrb = int(np.clip(np.random.normal(0.8, 0.7), 0, 2))
            ados_total = ados_sa + ados_rrb
            ados_class = 'NON_SPECTRUM'

        records.append({
            'child_id': child_id,
            'age_months': age_months,
            'gender': gender,
            'preferred_language': language,
            # Questionnaire
            'q1_social_smile': round(q_social, 1),
            'q2_eye_contact': round(q_eye, 1),
            'q3_response_to_name': round(q_name, 1),
            'q4_pointing_interest': round(q_point, 1),
            'q5_shared_enjoyment': round(q_shared, 1),
            'q6_imitation': round(q_imit, 1),
            'q7_repetitive_movements': round(q_rep, 1),
            'q8_sensory_interests': round(q_sensory, 1),
            'q9_play_flexibility': round(q_play, 1),
            'q10_language_milestones': round(q_lang, 1),
            'q_total_score': round(q_total, 1),
            # Camera Features
            'face_detected_ratio': round(face_ratio, 3),
            'head_movement_variability': round(head_variability, 3),
            'gaze_screen_ratio': round(gaze_screen_ratio, 3),
            'gaze_switch_frequency': round(gaze_switches, 2),
            'imitation_pose_similarity': round(imitation_pose, 3),
            'movement_smoothness': round(movement_smoothness, 3),
            'repetitive_hand_frequency': round(repetitive_hand_freq, 2),
            'rocking_motion_detected': rocking_detected,
            'camera_response_latency_ms': cam_latency,
            # Voice Features
            'vocalization_count': vocalization_count,
            'speech_duration_seconds': round(speech_duration_sec, 2),
            'voice_response_latency_ms': voice_latency_ms,
            'pause_duration_average': round(pause_duration_avg, 2),
            'word_count': word_count,
            'turn_taking_success_rate': round(turn_taking_rate, 3),
            'echolalia_repetition_index': round(echolalia_index, 3),
            # Categories A to J
            'cat_a_social_interaction': round(social_score, 1),
            'cat_b_communication': round(comm_score, 1),
            'cat_c_attention': round(attention_score, 1),
            'cat_d_motor_behaviour': round(motor_score, 1),
            'cat_e_imitation': round(imitation_score, 1),
            'cat_f_play_behaviour': round(play_score, 1),
            'cat_g_repetitive_behaviour': round(repetitive_score, 1),
            'cat_h_response_latency': round(latency_score, 1),
            'cat_i_voice_metrics': round(voice_score, 1),
            'cat_j_camera_movement': round(camera_score, 1),
            # Clinical ADOS-2 Reference
            'ados_social_affect': ados_sa,
            'ados_rrb': ados_rrb,
            'ados_total_cutoff': ados_total,
            'ados_classification': ados_class,
            # Ground truth screening target (1 = Elevated concern, 0 = Typical)
            'screening_target': is_concern
        })

    df = pd.DataFrame(records)
    output_path = 'synthetic_autism_multimodal_dataset.csv'
    df.to_csv(output_path, index=False)
    print(f"[SUCCESS] Synthetic dataset generated with {num_samples} samples.")
    print(f"[INFO] Class balance: {df['screening_target'].value_counts().to_dict()}")
    print(f"[FILE SAVED] {output_path}")
    return df

if __name__ == '__main__':
    generate_synthetic_dataset()

