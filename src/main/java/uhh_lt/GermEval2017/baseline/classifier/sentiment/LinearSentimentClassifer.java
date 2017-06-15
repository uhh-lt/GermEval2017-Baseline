package uhh_lt.GermEval2017.baseline.classifier.sentiment;

import uhh_lt.GermEval2017.baseline.classifier.LinearClassifier;
import uhh_lt.GermEval2017.baseline.training.LinearTesting;
import uhh_lt.GermEval2017.baseline.training.LinearTraining;

/**
 * The LinearSentimentClassifer analyzes the sentiment of a document.
 */
public class LinearSentimentClassifer extends LinearClassifier {
    /**
     * Constructor for the sentiment classifier; expects the path to the model file.
     * @param modelFile path to the SVM model file
     */
    public LinearSentimentClassifer(String modelFile) {
        this(modelFile, "data/models/sentiment_label_mappings.tsv");
    }

    /**
     * Constructor for the sentiment classifier; expects the path to the model file, as well as the label mapping file.
     * @param modelFile path to the SVM model file
     * @param labelMappingsFile path to the file containing label ids and their String representation
     */
    public LinearSentimentClassifer(String modelFile, String labelMappingsFile) {
        model = LinearTesting.loadModel(modelFile);
        features = LinearTraining.loadFeatureExtractors();

        labelMappings = loadLabelMapping(labelMappingsFile);
    }
}
