package uhh_lt.GermEval2017.baseline.featureExtractor;

import de.bwaldvogel.liblinear.Feature;
import org.apache.uima.jcas.JCas;

/**
 * Interface for feature extractors. Specifies common methods.
 */
public interface FeatureExtractor {

    /**
     * Extracts the feature using a {@link JCas}
     * @param cas the provided {@link JCas}
     * @return an array of {@link Feature}s, a training instance
     */
    Feature[] extractFeature(JCas cas);

    /**
     * Returns the maximal number of features that the extractor can produce.
     * @return the number of features in the {@link FeatureExtractor}
     */
    int getFeatureCount();

    /**
     * Returns the feature offset of the {@link FeatureExtractor}. All feature ids start from this offset to prevent overlaps.
     * @return the feature offset of the {@link FeatureExtractor}
     */
    int getOffset();
}
