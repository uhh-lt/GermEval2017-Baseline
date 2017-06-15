package uhh_lt.GermEval2017.baseline.training.aspectclass;

import de.bwaldvogel.liblinear.Model;
import uhh_lt.GermEval2017.baseline.featureExtractor.FeatureExtractor;
import uhh_lt.GermEval2017.baseline.training.LinearTesting;

import java.util.Vector;

/**
 * Aspect Model Tester (course-grained)
 */
public class TestCoarse extends LinearTesting {

    /**
     * Classifies an input file, given a model
     * @param args optional: input file, model file and the output file
     */
    public static void main(String[] args) {

        loadLabelMappings("data/models/aspect_coarse_label_mappings.tsv");

        testFile = "dev.tsv";
        modelFile = "data/models/aspect_coarse_model.svm";
        predictionFile = "aspect_coarse_test_predictions.tsv";

        if (args.length == 3) {
            testFile = args[0];
            modelFile = args[1];
            predictionFile = args[2];
        }

        Vector<FeatureExtractor> features = loadFeatureExtractors();

        Model model = loadModel(modelFile);

        useCoarseLabels = true;
        classifyTestSet(testFile, model, features, predictionFile, "aspect");
    }

}

