package uhh_lt.GermEval2017.baseline.type;

import uhh_lt.GermEval2017.baseline.featureExtractor.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Document {

    private List<Sentence> sentences;
    private List<Opinion> opinions;
    private String documentId;
    private String[] labels = null;
    private String relevance;
    private String sentiment;

    public Document() {
        this.sentences = new LinkedList<>();
        this.opinions = new LinkedList<>();
    }

    public Document(String id) {
        this();
        this.documentId = id;
    }

    public void setDocumentId(String id) {
        this.documentId = id;
    }

    public void setLabels(String label) {
        label = label.replaceAll("  ", " ").trim();
        labels = label.split(" ");
    }

    public void setDocumentAspects(String label) {
        label = label.replaceAll("  ", " ").trim();
        String[] labelsU= label.split(" ");
        labels = new String[labelsU.length];
        int i = 0;
        for (String l: labelsU) {
            labels[i++] = l.substring(0, l.lastIndexOf(":"));
        }
    }


    public void addSentence(Sentence s) {
        sentences.add(s);
    }

    public String getDocumentText() {
        StringBuilder sb = new StringBuilder();
        for (Sentence s : sentences) {
            sb.append(s.getText());
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public String getDocumentId() {
        return documentId;
    }

    public String[] getLabels() {
        if (labels != null) {
            return labels;
        } else return sentences.get(0).getAspectCategories();
    }

    public String getLabelsString() {
        StringBuilder sb = new StringBuilder();
        if (labels == null && sentences.get(0).getAspectCategories().length == 0) {
            return "0";
        }
        if (labels != null) {
            for (String l : labels) {
                if (l != null) {
                    sb.append(l).append(" ");
                }
            }
        } else {
            for (Sentence sen : sentences) {
                for (String s : sen.getAspectCategories()) {

                    sb.append(s).append(" ");
                }
            }
        }

        return sb.toString().trim();
    }

    public String[] getLabelsCoarse() {
        if (sentences.get(0).getAspectCategoriesCoarse().length == 0) {
            if (labels == null) {
                return new String[0];
            }
            String[] ret = new String[labels.length];
            int i = 0;
            for (String s : labels) {
                ret[i++] = extractCoarseCategory(s);
            }
            return ret;
        }
        return sentences.get(0).getAspectCategoriesCoarse();
    }

    public String getLabelsCoarseString() {
        StringBuilder sb = new StringBuilder();
        if (labels == null && sentences.get(0).getAspectCategories().length == 0) {return "0";}
        if (labels != null) {

            for (String l: labels) {
                if (l != null) {
                    sb.append(extractCoarseCategory(l)).append(" ");
                }
            }
        } else {
            for (Sentence sen : sentences) {
                for (String s : sen.getAspectCategoriesCoarse()) {

                    sb.append(extractCoarseCategory(s)).append(" ");
                }
            }
        }
        return sb.toString().trim();
    }

    private String extractCoarseCategory(String categoryFine) {
        if (categoryFine.indexOf('#') == -1) {
            return categoryFine;
        }
        return categoryFine.substring(0, categoryFine.indexOf('#'));
    }

    public List<Sentence> getSentences() {
        return this.sentences;
    }


    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public String[] getRelevance() {
        String[] ret = new String[1];
        ret[0] = relevance;
        return ret;
    }

    public void setSentiments(String docSent) {
        Opinion op;
        if (opinions.isEmpty()) {
            op = new Opinion(null, docSent);
        } else {
            op = opinions.get(0);
            op.setPolarity(docSent);
        }
        opinions.add(0, op);
    }

    public void setDocumentSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String[] getDocumentSentiment() {
        String[] sentiments = new String[1];
        if (sentiment != null && !sentiment.isEmpty()) {
            sentiments[0] = sentiment;
        } else {
            sentiments = new String[opinions.size()];
            int i = 0;
            for (Opinion op: opinions) {
                try {
                    sentiments[i++] = op.getPolarity();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return sentiments;
    }


    public List<Pair<Integer, Integer>> getTargetOffsets() {
        List<Pair<Integer, Integer>> ret = new ArrayList<>();
        for (Sentence s : sentences) {
                ret.addAll(s.getTargetOffsets());
        }
        return ret;
    }

}
