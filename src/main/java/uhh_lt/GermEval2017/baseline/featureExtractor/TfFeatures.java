package uhh_lt.GermEval2017.baseline.featureExtractor;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import uhh_lt.GermEval2017.baseline.uimahelper.Preprocessor;
import org.apache.uima.jcas.JCas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

/**
 * TF {@link FeatureExtractor}, extracts normalized TF scores using a pre-computed file with term IDs.
 */
public class TfFeatures implements FeatureExtractor {

    private int maxTokenId = 0;
    private int offset = 0;

    private HashMap<String, Integer> tokenIds = new HashMap<>();
    private HashMap<Integer, String> tokenStrings = new HashMap<>();

    private Preprocessor preprocessor = new Preprocessor();

    /**
     * Constructor; specifies the TF file. Feature offset is set to '0' by default.
     * @param tfFile path to the file containing scores and term IDs
     */
    public TfFeatures(String tfFile) {
        loadList(tfFile);
    }

    /**
     * Constructor; specifies the TF file. Feature offset is specified.
     * @param tfFile path to the file containing scores
     * @param offset the feature offset, all features start from this offset
     */
    public TfFeatures(String tfFile, int offset) {
        this(tfFile);
        this.offset = offset;
    }

    @Override
    public Feature[] extractFeature(JCas cas) {

        Collection<String> documentText = preprocessor.getTokenStrings(cas);
        HashMap<Integer, Integer> tokenCounts = getTokenCounts(documentText);
        return getScores(tokenCounts);
    }

    /**
     * Calculates the token counts for each token in the document.
     * @param documentText a collection of String tokens that constitute the document
     * @return a HashMap that stores the token count for each token (tokenId-&gt;count)
     */
    public HashMap<Integer, Integer> getTokenCounts(Collection<String> documentText) {
        HashMap<Integer, Integer> tokenCounts = new HashMap<>();
        for (String token : documentText) {
            if (token == null) {
                continue;
            }
            Integer tokenId = tokenIds.get(token);
            if (tokenId == null) {
                continue;
            }
            if (tokenCounts.get(tokenId) != null) {
                tokenCounts.put(tokenId, tokenCounts.get(tokenId) + 1);
            } else {
                tokenCounts.put(tokenId, 1);
            }
        }
        return tokenCounts;
    }

    @Override
    public int getFeatureCount() {
        return maxTokenId;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    /**
     * Calculates the instance array containing TF scores for each token
     * @param tokenCounts the token count for each token ID
     * @return an array of {@link Feature} elements
     */
    private Feature[] getScores(HashMap<Integer, Integer> tokenCounts) {
        double weight;
        double normalizedWeight;
        double norm = 0;

        HashMap<Integer, Double> termWeights = new HashMap<>();
        for (int tokenID : tokenCounts.keySet()) {
            weight = tokenCounts.get(tokenID);

            if (weight > 0.0) {
                norm += Math.pow(weight, 2);
                termWeights.put(tokenID, weight);
            }
        }
        // calculate normalization
        norm = Math.sqrt(norm);

        Feature[] instance = new Feature[termWeights.size()];
        ArrayList<Integer> list = new ArrayList<>(termWeights.keySet());
        Collections.sort(list);
        Double w;
        int i =0;
        for (int tokenId: list) {
            w = termWeights.get(tokenId);
            if (w == null) {
                w = 0.0;
            }
            normalizedWeight = w / norm;
            instance[i++] = new FeatureNode(tokenId+offset, normalizedWeight);
        }
        return instance;
    }

    public int getWordId(String term) {
        return tokenIds.get(term);
    }

    public String getWordString(Integer termId) {
        return tokenStrings.get(termId);
    }

    /**
     * Loads a word list with words, wordIds and  frequencies.
     * @param fileName the path to the input file
     */
    private void loadList(String fileName) {
        try {
            BufferedReader br;
            if (fileName.endsWith(".gz")) {
                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fileName)), "UTF-8"));
            } else {
                br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            }
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokenLine = line.split("\\t");

                int tokenId = ++maxTokenId;
                tokenIds.put(tokenLine[0], tokenId);
                tokenStrings.put(tokenId, tokenLine[0]);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
