package uhh_lt.GermEval2017.baseline.classifier;

import org.apache.uima.jcas.JCas;

/**
 * Interface for classifier classes.
 */
public interface Classifier {

    /**
     * Classifies a CAS and returns a label.
     * @param cas the CAS that is analyzed
     * @return a String label assigned by the classifier
     */
    String getLabel(JCas cas);

    /**
     * Returns the last label.
     * @return a String label assigned by the classifier
     */
    String getLabel();

    /**
     * Returns the confidence score for the most probable label.
     * @return the confidence score for the label
     */
    double getScore();

    //double getScore(int i);
    //String[] getLabels();
    //double[] getScores();

}
