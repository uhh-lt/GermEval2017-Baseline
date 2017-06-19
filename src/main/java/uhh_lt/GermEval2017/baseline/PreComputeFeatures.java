package uhh_lt.GermEval2017.baseline;

import uhh_lt.GermEval2017.baseline.featureExtractor.precomputation.ComputeCorpusTfScores;

/**
 * Precomputation of TF map and other data used in features.
 */
public class PreComputeFeatures {

    /**
     * Extracts different data for feature extractors.
     * @param args array for CLI arguments; unused
     */
    public static void main(String[] args) {

        String tfFile = "data/features/tfmap.tsv.gz";
        String corpusFile = "train.tsv";

        ComputeCorpusTfScores.computeScores(corpusFile, tfFile, 100);

    }

}