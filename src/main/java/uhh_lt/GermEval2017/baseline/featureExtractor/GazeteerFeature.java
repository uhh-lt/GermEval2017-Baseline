package uhh_lt.GermEval2017.baseline.featureExtractor;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import uhh_lt.GermEval2017.baseline.uimahelper.Preprocessor;
import org.apache.uima.jcas.JCas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Gazeteer {@link FeatureExtractor}, extracts binary features if words from a word list are found in a document.
 */
public class GazeteerFeature implements FeatureExtractor {

    private int offset = 0;

    private ArrayList<String> terms = new ArrayList<>();

    private Preprocessor preprocessor = new Preprocessor();

    /**
     * Constructor; specifies the gazetteer file. Feature offset is set to '0' by default.
     * @param gazetteer path to a wordlist
     */
    public GazeteerFeature(String gazetteer) {
        loadWordList(gazetteer);
    }

    /**
     * Constructor; specifies the gazetteer file. Feature offset is specified.
     * @param gazetteer path to a wordlist
     * @param offset the feature offset, all features start from this offset
     */
    public GazeteerFeature(String gazetteer, int offset) {
        this(gazetteer);
        this.offset = offset;
    }

    @Override
    public Feature[] extractFeature(JCas cas) {
        Collection<String> documentText = preprocessor.getTokenStrings(cas);
        int matchCount = 0;

        // find matches
        Vector<Integer> matches = new Vector<>();
        String term;
        for (int i = 0; i<terms.size(); i++) {
            term = terms.get(i);
            if (documentText.contains(term)) {
                matches.add(i+1);
                matchCount++;
            }
        }
        // construct feature array
        Feature[] features = new Feature[matches.size()];
        int i = 0;
        for (Integer match : matches) {
            features[i++] = new FeatureNode(match+offset, 1);
        }
        return features;
    }

    @Override
    public int getFeatureCount() {
        return terms.size();
    }

    @Override
    public int getOffset() {
        return offset;
    }


    /**
     * Loads a word list with words, other columns optional.
     * @param fileName the path to the wordlist
     */
    private void loadWordList(String fileName) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokenLine = line.split("\\t");
                terms.add(tokenLine[0]);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
