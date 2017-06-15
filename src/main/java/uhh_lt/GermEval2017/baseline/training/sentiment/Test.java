package uhh_lt.GermEval2017.baseline.training.sentiment;

import de.bwaldvogel.liblinear.Model;
import uhh_lt.GermEval2017.baseline.featureExtractor.FeatureExtractor;
import uhh_lt.GermEval2017.baseline.training.LinearTesting;

import java.util.Vector;

/**
 * Sentiment Model Tester
 */
public class Test extends LinearTesting {

    /**
     * Classifies an input file, given a model
     * @param args optional: input file, model file and the output file
     */
    public static void main(String[] args) {

        loadLabelMappings("data/models/sentiment_label_mappings.tsv");

        modelFile = "data/models/sentiment_model.svm";
        testFile = "dev.tsv";
        predictionFile = "sentiment_test_predictions.tsv";
        positiveGazeteerFile = "data/dictionaries/positive";
        negativeGazeteerFile = "data/dictionaries/negative";

        if (args.length == 3) {
            testFile = args[0];
            modelFile = args[1];
            predictionFile = args[2];
        }

        Vector<FeatureExtractor> features = loadFeatureExtractors();

        Model model = loadModel(modelFile);

        classifyTestSet(testFile, model, features, predictionFile, "sentiment");

    }

}

