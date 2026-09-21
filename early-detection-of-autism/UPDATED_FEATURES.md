# Updated Project Features

## Child experience
- Eight simple, colourful games with one clear action at a time.
- Large emoji/SVG illustrations designed for young children.
- Pip the penguin repeats instructions.
- Progress bar and friendly feedback.
- Camera sensor remains available for real-time behavioural feature extraction.
- Voice sensor is used in the animal/hello activities.
- Raw camera/audio streams are not stored by the front-end sensor modules.

## ML / screening
- Added `RandomForestScreeningModel.java`: a genuine decision-tree ensemble for prototype screening inference.
- Random Forest uses six fused behavioural features: social interaction, communication, attention, motor behaviour, imitation and repetitive behaviour.
- The included forest is calibrated on synthetic/demo data. It must not be presented as clinically validated.
- Added browser `cnn-vision.js` using TensorFlow.js + MobileNet as a CNN visual feature extractor. This is a visual embedding layer, not an autism classifier.
- Final category is explicitly a screening/observed-concern category, not a diagnosis.

## Parent output
- Parent dashboard now explains the CNN → Random Forest → screening flow.
- PDF report already supported through OpenPDF and the Download PDF Report button.
- PDF report now includes model architecture, synthetic-data limitation and a clear ADOS-2 relationship statement.
- Removed the incorrect wording that ADOS-2 is a “gold standard” and removed the hard-coded claim of AI/ADOS concordance.
