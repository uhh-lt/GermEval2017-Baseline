package uhh_lt.GermEval2017.baseline;

import uhh_lt.GermEval2017.baseline.featureExtractor.precomputation.ComputeCorpusIdfScores;

/**
 * Precomputation of IDF map and other data used in features.
 */
public class PreComputeFeatures {

    /**
     * Extracts different data for feature extractors.
     * @param args array for CLI arguments; unused
     */
    public static void main(String[] args) {

        String idfFile = "data/features/idfmap.tsv.gz";
        String corpusFile = "train.tsv";

        ComputeCorpusIdfScores.computeIdfScores(corpusFile, idfFile, 100);

    }

}