package uhh_lt.GermEval2017.baseline.featureExtractor.precomputation;

import uhh_lt.GermEval2017.baseline.type.Document;
import uhh_lt.GermEval2017.baseline.reader.InputReader;
import uhh_lt.GermEval2017.baseline.reader.TsvReader;

/**
 * Computes global TF scores for a corpus in TSV format.
 */
public class ComputeCorpusTfScores {

    /**
     * Computes global TF scores from an input file and saves them in a file; specifies a minimum frequency for terms.
     * @param inputFile file containing the input corpus
     * @param outputFile path to the output file with term ids and their scores
     * @param minFrequency the minimum frequency for a term
     */
    public static void computeScores(String inputFile, String outputFile, int minFrequency) {
        ComputeTf tf = new ComputeTf();
        tf.setMinFrequency(minFrequency);
        InputReader fr = new TsvReader(inputFile);

        for (Document d: fr) {
            tf.addDocument(d);
        }
        tf.saveScores(outputFile);
    }

    /**
     * Computes global TF scores and term IDs from an input file and saves them in a file.
     * @param inputFile file containing the input corpus
     * @param outputFile path to the output file
     */
    public static void computeScores(String inputFile, String outputFile) {
        computeScores(inputFile, outputFile, 1);
    }
}
