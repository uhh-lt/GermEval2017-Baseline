package uhh_lt.GermEval2017.baseline.classifier.relevance;

import uhh_lt.GermEval2017.baseline.classifier.Classifier;
import org.apache.uima.jcas.JCas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Baseline relevance classifier using a dictionary terms that indicate irrelevance.
 */
public class DictionaryRelevanceClassifier implements Classifier {

    private HashSet<String> wordList;
    private String label;

    /**
     * Constructor, loads a list of words that indicate non-relevance.
     */
    public DictionaryRelevanceClassifier() {
        String filename = "/data/dictionaries/non-relevant";

        wordList = loadWordList(filename);
    }


    @Override
    public String getLabel(JCas cas) {
        String text = cas.getDocumentText();
        label = "1.0";

        for (String w : wordList) {
            if (text.contains(w)) {
                label = "-1.0";
            }
        }
        return label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public double getScore() {
        return 0;
    }


    /**
     * Loads a word list.
     *
     * @param fileName the path and filename of the wordlist
     * @return HashSet containing the words
     */
    private HashSet<String> loadWordList(String fileName) {
        HashSet<String> set = new HashSet<>();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream(fileName), "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                set.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return set;
    }

}
