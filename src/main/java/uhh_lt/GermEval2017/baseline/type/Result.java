package uhh_lt.GermEval2017.baseline.type;


import java.util.Vector;

public class Result {

    private final String text;
    private String sentiment;
    private double sentimentScore;
    private String aspect;
    private double aspectScore;
    private String aspectCoarse;
    private double aspectCoarseScore;
    private String relevance;
    private double relevanceScore;

    private Vector<AspectExpression> aspectExpressions = new Vector<>();

    public Result(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public double getAspectScore() {
        return aspectScore;
    }

    public void setAspectScore(double aspectScore) {
        this.aspectScore = aspectScore;
    }

    public String getAspectCoarse() {
        return aspectCoarse;
    }

    public void setAspectCoarse(String aspectCoarse) {
        this.aspectCoarse = aspectCoarse;
    }

    public double getAspectCoarseScore() {
        return aspectCoarseScore;
    }

    public void setAspectCoarseScore(double aspectCoarseScore) {
        this.aspectCoarseScore = aspectCoarseScore;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public void addAspectExpression(AspectExpression ae) {
        aspectExpressions.add(ae);
    }

    public Vector<AspectExpression> getAspectExpressions() {
        return aspectExpressions;
    }
}
