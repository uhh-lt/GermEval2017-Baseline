package uhh_lt.GermEval2017.baseline.type;

import uhh_lt.GermEval2017.baseline.featureExtractor.util.Pair;

import java.util.*;


public class Sentence {

    private String text;
    private String id;
    private Vector<Opinion> opinions;

    public Sentence() {
        opinions = new Vector<>();
    }

    public Sentence (String sentence) {
        this.text = sentence.trim();
        opinions = new Vector<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String sentence) {
        this.text = sentence.trim();
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public void addOpinions(Vector<Opinion> opinions) {
        this.opinions.addAll(opinions);
    }

    public String getSentiment() throws NoSuchFieldException {
        Set<String> polarities = new HashSet<>();
        for (Opinion o: opinions) {
            try {
                polarities.add(o.getPolarity());
            } catch (NoSuchFieldException e) {
            }
        }
        String r = new String();
        for (String p : polarities) {
            r += p + " ";
        }
        if (r.compareTo("") == 0) {
            throw new NoSuchFieldException("No Sentiment present");
        }
        return r.trim();
    }

    public String[] getAspectCategories() {
        String[] aspects = new String[opinions.size()];
        int i = 0;
        for (Opinion o: opinions) {
            try {
                aspects[i++] = o.getFineCategory();
            } catch (NoSuchFieldException e) {
            }
        }
        return aspects;
    }


    public String[] getAspectCategoriesCoarse() {
        String[] aspects = new String[opinions.size()];
        int i = 0;
        for (Opinion o: opinions) {
            try {
                aspects[i++] = o.getCoarseCategory();
            } catch (NoSuchFieldException e) {
            }
        }
        return aspects;
    }

    public Set<String> getTargets() {
        Set<String> targets = new HashSet<>();
        for (Opinion o: opinions) {
           targets.add(o.getTarget());
        }
        return targets;
    }

    public Collection<? extends Pair<Integer,Integer>> getTargetOffsets() {

        List<Pair<Integer, Integer>> offsets = new ArrayList<>();
        for (Opinion opinion : opinions) {
            offsets.addAll(opinion.getTargets());
        }
        return offsets;
    }
}
