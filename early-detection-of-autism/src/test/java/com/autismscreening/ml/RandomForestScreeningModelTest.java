package com.autismscreening.ml;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomForestScreeningModelTest {

    @Test
    void testPredictionProbabilityBounds() {
        RandomForestScreeningModel model = new RandomForestScreeningModel();
        
        // Typical typical/low-concern feature vector
        double[] lowConcernFeatures = {85.0, 80.0, 80.0, 85.0, 90.0, 10.0};
        RandomForestScreeningModel.Prediction predLow = model.predict(lowConcernFeatures);
        
        assertNotNull(predLow);
        assertTrue(predLow.elevatedConcernProbability >= 0.0 && predLow.elevatedConcernProbability <= 1.0,
                "Probability should be bounded within [0.0, 1.0]");
        assertTrue(predLow.elevatedConcernProbability < 0.5,
                "High engagement should result in lower concern probability");

        // Typical elevated concern feature vector
        double[] highConcernFeatures = {30.0, 25.0, 30.0, 35.0, 20.0, 80.0};
        RandomForestScreeningModel.Prediction predHigh = model.predict(highConcernFeatures);
        
        assertNotNull(predHigh);
        assertTrue(predHigh.elevatedConcernProbability >= 0.0 && predHigh.elevatedConcernProbability <= 1.0);
        assertTrue(predHigh.elevatedConcernProbability > predLow.elevatedConcernProbability,
                "High concern features should yield higher elevated concern probability than low concern features");
    }

    @Test
    void testNullOrIncompleteFeatureVectorHandledGracefully() {
        RandomForestScreeningModel model = new RandomForestScreeningModel();
        
        double[] shortFeatures = {50.0, 50.0};
        RandomForestScreeningModel.Prediction pred = model.predict(shortFeatures);
        assertNotNull(pred);
        assertTrue(pred.elevatedConcernProbability >= 0.0 && pred.elevatedConcernProbability <= 1.0);
    }
}

