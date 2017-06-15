package uhh_lt.GermEval2017.baseline.uimahelper;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import uhh_lt.GermEval2017.baseline.featureExtractor.FeatureExtractor;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.uima.fit.util.JCasUtil.select;

/**
 * Preprocessor class that performs NLP operations using UIMA AnalysisEngines.
 */
public class Preprocessor {

    private JCas cas;
    private AnalysisEngine tokenizer;
    private AnalysisEngine postagger;
    private boolean lightAnalysis = false;
    private final String language;

    /**
     * Constructor; initializes the UIMA pipeline and the CAS.
     */
    public Preprocessor() {
        // build annotation engine
        try {
            tokenizer = AnalysisEngineFactory.createEngine(BreakIteratorSegmenter.class);
            postagger = AnalysisEngineFactory.createEngine(OpenNlpPosTagger.class,
                            OpenNlpPosTagger.PARAM_MODEL_LOCATION, "data/models/opennlp-de-pos-maxent.bin");
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        }
        // build cas
        try {
            cas = JCasFactory.createJCas();
        } catch (UIMAException e) {
            e.printStackTrace();
        }
        language = "de";
    }

    /**
     * Constructor; initializes the UIMA pipeline and the CAS, then processes an input text
     * @param lightAnalysis flag to indicate light analysis, only tokenization is applied
     */
    public Preprocessor(boolean lightAnalysis) {
        this();
        this.lightAnalysis = lightAnalysis;
    }

    /**
     * Constructor; initializes the UIMA pipeline and the CAS, then processes an input text
     * @param input input text that is analyzed in the CAS
     */
    public Preprocessor(String input) {
        this();
        processText(input);
    }

    /**
     * Processes a new text by the NLP pipeline. Resets the CAS for fast processing.
     * @param input input text
     */
    public void processText(String input) {
        createCas(input);
        try {
            tokenizer.process(cas);
            if (!lightAnalysis) {
                postagger.process(cas);
            }
        } catch (AnalysisEngineProcessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the CAS from an input text
     * @param input input text
     */
    private void createCas(String input) {
        cas.reset();
        cas.setDocumentText(input);
        cas.setDocumentLanguage(language);
    }

    /**
     * Retrieves the CAS, e.g. for {@link FeatureExtractor}s
     * @return the CAS object
     */
    public JCas getCas() {
        return cas;
    }

    /**
     * Retrieves a list of tokens as Strings from a provied CAS.
     * @param cas the CAS, from which the tokens are extracted
     * @return a list of Strings
     */
    public List<String> getTokenStrings(JCas cas) {
        List<String> tokenStrings = new ArrayList<>();
        Collection<Token> tokens = select(cas, Token.class);
        for (Annotation token : tokens) {
            tokenStrings.add(token.getCoveredText());
        }
        return tokenStrings;
    }

    /**
     * Retrieves a list of tokens as Strings from the current CAS.
     * @return a list of Strings
     */
    public List<String> getTokenStrings() {
        return getTokenStrings(this.cas);
    }
}
