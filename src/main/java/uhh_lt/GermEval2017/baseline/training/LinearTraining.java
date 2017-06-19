package uhh_lt.GermEval2017.baseline.training;


import de.bwaldvogel.liblinear.*;
import uhh_lt.GermEval2017.baseline.featureExtractor.FeatureExtractor;
import uhh_lt.GermEval2017.baseline.featureExtractor.GazeteerFeature;
import uhh_lt.GermEval2017.baseline.featureExtractor.TfFeatures;
import uhh_lt.GermEval2017.baseline.type.Document;
import uhh_lt.GermEval2017.baseline.uimahelper.Preprocessor;
import org.apache.uima.jcas.JCas;
import uhh_lt.GermEval2017.baseline.reader.InputReader;
import uhh_lt.GermEval2017.baseline.reader.TsvReader;
import uhh_lt.GermEval2017.baseline.reader.XMLReader;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

/**
 * Training Class that is extended by all linear learners.
 * Offers methods to train {@link Model}s using different {@link FeatureExtractor}s.
 */
public class LinearTraining {

    private static InputReader fr;
    protected static Preprocessor preprocessor = new Preprocessor();

    private static Integer maxLabelId = -1;
    private static int featureCount = 0;
    protected static boolean useCoarseLabels = false;

    private static HashMap<String, Integer> labelMappings;
    private static HashMap<Integer, String> labelLookup;

    protected static String trainingFile;
    protected static String modelFile;
    protected static String labelMappingsFile;
    protected static String featureOutputFile;
    protected static String featureStatisticsFile;
    protected static String tfFile = "data/features/tfmap.tsv.gz";

    protected static String positiveGazeteerFile;
    protected static String negativeGazeteerFile;

    /**
     * Loads and initializes {@link FeatureExtractor}s for training and testing. Ensures that there is no feature ID overlap between different {@link FeatureExtractor}s.
     * @return a Vector of {@link FeatureExtractor} entries
     */
    protected static Vector<FeatureExtractor> loadFeatureExtractors() {
        int offset = 0;
        Vector<FeatureExtractor> features = new Vector<>();
        FeatureExtractor tf = new TfFeatures(tfFile, offset);
        offset += tf.getFeatureCount();
        features.add(tf);

        // FeatureExtractors are added to the features Vector;
        // the offset should be updated for each new FeatureExtractor to prevent overlapping Feature ids

        if (positiveGazeteerFile!= null) {
            FeatureExtractor posDict = new GazeteerFeature(positiveGazeteerFile, offset);
            offset += posDict.getFeatureCount();
            features.add(posDict);
        }
        if (negativeGazeteerFile!= null) {
            FeatureExtractor negDict = new GazeteerFeature(negativeGazeteerFile, offset);
            offset += negDict.getFeatureCount();
            features.add(negDict);
        }

        return features;
    }

    /**
     * Builds the {@link Problem} from a training file, using provided {@link FeatureExtractor}s. Stores feature vectors in a file.
     * @param trainingFile path to the training file
     * @param features Vector of {@link FeatureExtractor}s
     * @param type The type of the problem: relevance, sentiment, aspect
     * @return {@link Problem}, containing the extracted features per instance
     */
    protected static Problem buildProblem(String trainingFile, Vector<FeatureExtractor> features, String type) {
        resetLabelMappings();
        printFeatureStatistics(features);

        if (trainingFile.endsWith("xml")) {
            fr = new XMLReader(trainingFile);
        } else {
            fr = new TsvReader(trainingFile);
        }

        int documentCount = 0;
        Vector<Double> labels = new Vector<>();
        Vector<Feature[]> featureVector = new Vector<>();
        Vector<Feature[]> instanceFeatures;
        for (Document d: fr) {
            preprocessor.processText(d.getDocumentText());
            instanceFeatures = applyFeatures(preprocessor.getCas(), features);

            // creates a training instance for each document label (multi-label training)
            String[] documentLabels = new String[0];

            if (type == null) {
                documentLabels = d.getLabels();
            } else if (type.compareTo("relevance") == 0) {
                documentLabels = d.getRelevance();
            } else if (type.compareTo("sentiment") == 0) {
                documentLabels = d.getDocumentSentiment();
            } else if (type.compareTo("aspect") == 0) {
                if (useCoarseLabels) {
                    documentLabels = d.getLabelsCoarse();
                } else {
                    documentLabels = d.getLabels();
                }
            }

            for (String l : documentLabels) {
                if (l.isEmpty()) {continue;}
                Double label = getLabelId(l);
                labels.add(label);
                // combine feature vectors for one instance
                featureVector.add(combineInstanceFeatures(instanceFeatures));
                documentCount++;
            }
        }

        if (featureOutputFile != null) {
            saveFeatureVectors(featureOutputFile, featureVector, labels);
        }

        Problem problem = new Problem();
        problem.l = documentCount;
        problem.n = featureCount;
        problem.x = new Feature[documentCount][];
        problem.y = new double[documentCount];

        for (int i = 0; i<labels.size(); i++) {
            problem.y[i] = labels.get(i);
            problem.x[i] = featureVector.get(i);

        }
        return problem;
    }

    /**
     * Builds the {@link Problem} from a training file, using provided {@link FeatureExtractor}s.
     * @param trainingFile path to the training file
     * @param features Vector of {@link FeatureExtractor}s
     * @return {@link Problem}, containing the extracted features per instance
     */
    protected static Problem buildProblem(String trainingFile, Vector<FeatureExtractor> features) {
        resetLabelMappings();
        printFeatureStatistics(features);
        return buildProblem(trainingFile, features, null);
    }




    protected static void resetLabelMappings() {
        labelMappings = new HashMap<>();
        labelLookup = new HashMap<>();
        maxLabelId = -1;
    }


    /**
     * Applies {@link FeatureExtractor}s to a given CAS. Each {@link FeatureExtractor} creates an individual array of {@link Feature} entries.
     * @param cas the CAS that the {@link FeatureExtractor}s operate on
     * @param features a vector of {@link FeatureExtractor}s
     * @return a vector with an {@link Feature} array from each {@link FeatureExtractor}
     */
    protected static Vector<Feature[]> applyFeatures(JCas cas, Vector<FeatureExtractor> features) {
        Vector<Feature[]> instanceFeatures = new Vector<>();
        for (FeatureExtractor feature : features) {
            instanceFeatures.add(feature.extractFeature(cas));
            // update the featureCount, the maximal Feature id
            featureCount = feature.getFeatureCount() + feature.getOffset();
        }
        return instanceFeatures;
    }

    /**
     * Helper method to combine {@link Feature} arrays that are produced by several {@link FeatureExtractor}s, when applied to a single document.
     * @param instanceFeatures a Vector of {@link Feature} arrays that should be combined
     * @return one {@link Feature} array to be used as a training instance
     */
    protected static Feature[] combineInstanceFeatures(Vector<Feature[]> instanceFeatures) {
        int length = 0;
        for (Feature[] f : instanceFeatures) {
            length += f.length;
        }
        Feature[] instance = new Feature[length];
        int i=0;
        for (Feature[] fa : instanceFeatures) {
            for (Feature value : fa) {
                instance[i++] = value;
            }
        }
        return instance;
    }

    /**
     * Saves the label--identifier mappings in a TAB separated file using the following format:<br>
     * LABEL_ID  &emsp; LABEL<br>
     * Allows for retrieving the original String labels in classification.
     * @param mappingFile the path to the file
     */
    protected static void saveLabelMappings(String mappingFile) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(mappingFile), "UTF-8"));
            for (String label : labelMappings.keySet()) {
                out.write(labelMappings.get(label) + "\t" + label + "\n");
            }
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Gets the LabelId for a given String label. If the label has not been encountered yet, returns a new Double value.
     * @param label the String label
     * @return Double value to be used as an instance label
     */
    protected static Double getLabelId(String label) {
        if (labelMappings.containsKey(label)) {
            return labelMappings.get(label).doubleValue();
        } else {
            labelMappings.put(label, ++maxLabelId);
            labelLookup.put(maxLabelId, label);
            return maxLabelId.doubleValue();
        }
    }

    /**
     * Gets the String label for a given label ID.
     * @param labelId the label ID
     * @return the String representation of the label
     */
    protected static String getLabelString(Double labelId) {
        return labelLookup.get(labelId.intValue());
    }

    /**
     * Trains the linear classifier {@link Model} from a given {@link Problem}. Returns the trained {@link Model}.
     * @param problem the Problem containing all the training instances
     * @return trained model
     */
    protected static Model trainModel(Problem problem) {
        SolverType solver = SolverType.L2R_LR;
        double C = 1.0;
        double eps = 0.1;
        Parameter parameter = new Parameter(solver, C, eps);

        return Linear.train(problem, parameter);
    }

    /**
     * Saves the {@link Model} in a model file. The file can be compressed.
     * @param model the model to be saved
     * @param modelFile path to the output file
     * @param saveGzipped boolean flag to enable model compression (recommended)
     */
    protected static void saveModel(Model model, String modelFile, boolean saveGzipped) {
        try {
            if (saveGzipped) {
                model.save(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(modelFile + ".gz")), "UTF-8"));
            } else {
                model.save(new File(modelFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the {@link Model} in a model file. By default, compresses model and appends .gz to the filename.
     * @param model the model to be saved
     * @param modelFile path to the output file
     */
    protected static void saveModel(Model model, String modelFile) {
       saveModel(model, modelFile, true);
    }

    protected static void saveFeatureVectors(String featureVectorFile, Vector<Feature[]> featureVector, Vector<Double> labels) {
        if (featureVectorFile == null) {return;}
        try {
            Writer featureOut = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(featureVectorFile), "UTF-8"));
            for (int i = 0; i< labels.size(); i++) {
                featureOut.write(labels.get(i).toString());
                Feature[] features = featureVector.get(i);
                for (Feature f : features) {
                    featureOut.write(" " + f.getIndex() + ":" + f.getValue());
                }
                featureOut.write("\n");
            }
            featureOut.close();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void printFeatureStatistics(Vector<FeatureExtractor> features) {
        if (featureStatisticsFile != null) {
            try {
                Writer statisticsOut = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(featureStatisticsFile), "UTF-8"));
                statisticsOut.write("training set: "+ trainingFile + "\n");
                if (featureStatisticsFile != null) {
                    int start;
                    int end;
                    for (FeatureExtractor feature : features) {
                        start = feature.getOffset() + 1;
                        end = feature.getOffset() + feature.getFeatureCount();
                        statisticsOut.append(feature.getClass().getCanonicalName())
                                .append("\t").append(String.valueOf(start)).append("\t").append(String.valueOf(end)).append("\n");
                    }
                }
                statisticsOut.close();
            } catch (UnsupportedEncodingException | FileNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
