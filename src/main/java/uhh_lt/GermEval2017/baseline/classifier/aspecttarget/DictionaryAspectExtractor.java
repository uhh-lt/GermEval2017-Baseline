package uhh_lt.GermEval2017.baseline.classifier.aspecttarget;

import uhh_lt.GermEval2017.baseline.classifier.Classifier;
import org.apache.uima.jcas.JCas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Baseline aspect target extractor, uses dictionaries of aspect terms.
 */
public class DictionaryAspectExtractor implements Classifier {

    private HashSet<String> wordList;
    private String label;

    /**
     * Constructor, loads a list of aspect terms
     */
    public DictionaryAspectExtractor() {
        String filename = "/data/dictionaries/aspects";

        wordList = loadWordList(filename);
    }

    @Override
    public String getLabel(JCas cas) {
        String text = cas.getDocumentText();
        for (String t : wordList) {
            if (text.contains(t)) {
               label = t;
            }
        }
        return label;
    }

    public List<String> getLabels(JCas cas) {
        String text = cas.getDocumentText();
        List<String> ret = new ArrayList<>();

        for (String t : wordList) {
            if (text.contains(t)) {
                ret.add(t);
            }
        }
        return ret;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public double getScore() {
        return 1.0;
    }

    /**
     * Loads a word list
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