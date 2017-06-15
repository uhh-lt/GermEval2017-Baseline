package uhh_lt.GermEval2017.baseline.training.relevance;

import de.bwaldvogel.liblinear.Model;
import uhh_lt.GermEval2017.baseline.featureExtractor.FeatureExtractor;
import uhh_lt.GermEval2017.baseline.training.LinearTesting;

import java.util.Vector;

/**
 * Relevance Model Tester
 */
public class Test extends LinearTesting {

    /**
     * Classifies an input file, given a model
     * @param args optional: input file, model file and the output file
     */
    public static void main(String[] args) {

        loadLabelMappings("data/models/relevance_label_mappings.tsv");

        modelFile = "data/models/relevance_model.svm";
        testFile = "dev.tsv";
        predictionFile = "relevance_test_predictions.tsv";

        if (args.length == 3) {
            testFile = args[0];
            modelFile = args[1];
            predictionFile = args[2];
        }

        Vector<FeatureExtractor> features = loadFeatureExtractors();

        Model model = loadModel(modelFile);

        classifyTestSet(testFile, model, features, predictionFile, "relevance");
    }

}

