package uhh_lt.GermEval2017.baseline;

import uhh_lt.GermEval2017.baseline.type.AspectExpression;
import uhh_lt.GermEval2017.baseline.type.Result;
import uhh_lt.GermEval2017.baseline.type.uima.AspectTarget;
import uhh_lt.GermEval2017.baseline.uimahelper.Preprocessor;
import org.apache.uima.jcas.JCas;
import uhh_lt.GermEval2017.baseline.classifier.CrfClassifier;
import uhh_lt.GermEval2017.baseline.classifier.LinearClassifier;
import uhh_lt.GermEval2017.baseline.classifier.aspectclass.LinearAspectClassifier;
import uhh_lt.GermEval2017.baseline.classifier.relevance.LinearRelevanceClassifier;
import uhh_lt.GermEval2017.baseline.classifier.sentiment.LinearSentimentClassifer;

import static org.apache.uima.fit.util.JCasUtil.select;

/**
 * Abstraction class for the Aspect-based Sentiment Analysis. Processes text input.
 */
public class AbSentiment {

    private LinearClassifier relevanceClassifier;
    private LinearClassifier aspectClassifier;
    private LinearClassifier coarseAspectClassifier;
    private LinearClassifier sentimentClassifier;
    private CrfClassifier aspectTargetClassifier;

    private Preprocessor nlpPipeline;

    /**
     * Constructor that utilizes the classiers and the NLP pipeline.
     */
    public AbSentiment() {
        relevanceClassifier = new LinearRelevanceClassifier("data/models/relevance_model.svm");
        aspectClassifier = new LinearAspectClassifier("data/models/aspect_model.svm");
        coarseAspectClassifier = new LinearAspectClassifier("data/models/aspect_coarse_model.svm", "data/models/aspect_coarse_label_mappings.tsv");
        sentimentClassifier = new LinearSentimentClassifer("data/models/sentiment_model.svm");
        aspectTargetClassifier = new CrfClassifier("data/models/");

        nlpPipeline = new Preprocessor();
    }

    /**
     * Analyzes an input string. Runs a NLP pipeline, then the classifiers.
     * @param text the input string
     * @return a structured {@link Result}
     */
    public Result analyzeText(String text) {
        nlpPipeline.processText(text);
        JCas cas = nlpPipeline.getCas();

        Result res = new Result(text);

        // extract aspect terms
        JCas aspectCas = aspectTargetClassifier.processCas(cas);
        extractAspectExpressions(aspectCas, res);

        res.setRelevance(relevanceClassifier.getLabel(cas));
        res.setRelevanceScore(relevanceClassifier.getScore());

        res.setAspect(aspectClassifier.getLabel(cas));
        res.setAspectScore(aspectClassifier.getScore());

        res.setAspectCoarse(coarseAspectClassifier.getLabel(cas));
        res.setAspectCoarseScore(coarseAspectClassifier.getScore());

        res.setSentiment(sentimentClassifier.getLabel(cas));
        res.setSentimentScore(sentimentClassifier.getScore());

        return res;
    }

    /**
     * Extracts aspect expressions from a CAS.
     * @param cas the CAS containing {@link AspectTarget} annotations
     * @param res the {@link Result}, where the expressions are added
     */
    private void extractAspectExpressions(JCas cas, Result res) {
        String text = cas.getDocumentText();
        int begin = 0;
        int end = text.length()-1;

        boolean aspectActive = false;
        for (AspectTarget t: select(cas, AspectTarget.class)) {
            end = t.getEnd();
            if (aspectActive && t.getAspectTargetType().compareTo("O") == 0) {
                aspectActive = false;
                res.addAspectExpression(new AspectExpression(text.substring(begin, end), begin, end));
            } else if (!aspectActive && t.getAspectTargetType().compareTo("B") == 0) {
                aspectActive = true;
                begin = t.getBegin();
            } else if (aspectActive && t.getAspectTargetType().compareTo("B") == 0) {
                aspectActive = true;
                res.addAspectExpression(new AspectExpression(text.substring(begin, end), begin, end));
                begin = t.getBegin();
            }
        }
        if (aspectActive) {
            res.addAspectExpression(new AspectExpression(text.substring(begin, end), begin, end));
        }
    }
}
