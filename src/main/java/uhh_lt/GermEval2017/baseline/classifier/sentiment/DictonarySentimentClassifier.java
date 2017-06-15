package uhh_lt.GermEval2017.baseline.classifier.sentiment;


import org.apache.uima.jcas.JCas;
import uhh_lt.GermEval2017.baseline.classifier.Classifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Baseline relevance classifier, uses dictionaries of polarity terms.
 */
public class DictonarySentimentClassifier implements Classifier {

    private HashSet<String> wordList_p;
    private HashSet<String> wordList_n;

    private int pos;
    private int neg;

    /**
     * Constructor, initializes the polarity wordlists.
     */
    public DictonarySentimentClassifier() {
        String filename_p = "/data/dictionaries/positive";
        String filename_n = "/data/dictionaries/negative";

        wordList_p = loadWordList(filename_p);
        wordList_n = loadWordList(filename_n);
    }

    @Override
    public String getLabel(JCas cas) {
        pos = 0;
        neg = 0;
        String text = cas.getDocumentText();

        for (String w : wordList_p) {
            if (text.contains(w)) {
                pos++;
            }
        }
        for (String w : wordList_n) {
            if (text.contains(w)) {
                neg++;
            }
        }
        return getLabel();
    }


    @Override
    public String getLabel() {
        if (pos > neg) {
            return "pos";
        }
        if (neg > pos) {
            return "neg";
        }
        return "neut";
    }

    @Override
    public double getScore() {
        return pos - neg;
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