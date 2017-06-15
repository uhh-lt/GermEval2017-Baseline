package uhh_lt.GermEval2017.baseline.classifier.aspectclass;

import org.apache.uima.jcas.JCas;
import uhh_lt.GermEval2017.baseline.classifier.Classifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Baseline aspect classifier using a dictionary of aspect expressions.
 */
public class DictionaryAspectClassifier implements Classifier {

    private HashMap<String, String> wordList;
    private String label;

    /**
     * Constructor, loads a map of aspect terms and their aspect classes
     */
    public DictionaryAspectClassifier() {
        String filename = "/data/dictionaries/aspect_classes";

        wordList = loadWordList(filename);
    }

    @Override
    public String getLabel(JCas cas) {
        String text = cas.getDocumentText();
        wordList.keySet().stream().filter(text::contains).forEach(term -> {
            label = wordList.get(term);
        });
        return label;
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
     * Loads a word list.
     *
     * @param fileName the path and filename of the wordlist
     * @return HashSet containing the words
     */
    private HashMap<String, String> loadWordList(String fileName) {
        HashMap<String, String> set = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] split = line.split("\\t");
                    if (split.length >= 1) {
                        set.put(split[0], split[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

}
