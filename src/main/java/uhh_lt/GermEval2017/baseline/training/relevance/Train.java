package uhh_lt.GermEval2017.baseline.training.relevance;

import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Problem;
import uhh_lt.GermEval2017.baseline.featureExtractor.FeatureExtractor;
import uhh_lt.GermEval2017.baseline.training.LinearTraining;

import java.util.Vector;

/**
 * Relevance Model Trainer
 */
public class Train extends LinearTraining {

    /**
     * Trains the model from an input file
     * @param args optional: input file and optional model file
     */
    public static void main(String[] args) {

        trainingFile = "train.tsv";
        modelFile = "data/models/relevance_model.svm";
        labelMappingsFile  = "data/models/relevance_label_mappings.tsv";

        if (args.length == 2) {
            trainingFile = args[0];
            modelFile = args[1];
        } else if (args.length == 1) {
            trainingFile = args[0];
        }

        Vector<FeatureExtractor> features = loadFeatureExtractors();

        Problem problem = buildProblem(trainingFile, features, "relevance");
        Model model = trainModel(problem);
        saveModel(model, modelFile);

        saveLabelMappings(labelMappingsFile);
    }

}