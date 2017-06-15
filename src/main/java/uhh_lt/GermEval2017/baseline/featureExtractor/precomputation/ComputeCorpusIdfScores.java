package uhh_lt.GermEval2017.baseline.featureExtractor.precomputation;

import uhh_lt.GermEval2017.baseline.type.Document;
import uhh_lt.GermEval2017.baseline.reader.InputReader;
import uhh_lt.GermEval2017.baseline.reader.TsvReader;

/**
 * Computes global IDF scores for a corpus in TSV format.
 */
public class ComputeCorpusIdfScores {

    /**
     * Computes global IDF scores from an input file and saves them in a file; specifies a minimum frequency for terms.
     * @param inputFile file containing the input corpus
     * @param outputFile path to the output file with term ids and idf scores
     * @param minFrequency the minimum frequency for a term
     */
    public static void computeIdfScores(String inputFile, String outputFile, int minFrequency) {
        ComputeIdf idf = new ComputeIdf();
        idf.setMinFrequency(minFrequency);
        InputReader fr = new TsvReader(inputFile);

        for (Document d: fr) {
            idf.addDocument(d);
        }
        idf.saveIdfScores(outputFile);
    }

    /**
     * Computes global IDF scores from an input file and saves them in a file.
     * @param inputFile file containing the input corpus
     * @param outputFile path to the output file
     */
    public static void computeIdfScores(String inputFile, String outputFile) {
        computeIdfScores(inputFile, outputFile, 1);
    }
}
