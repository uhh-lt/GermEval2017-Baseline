package uhh_lt.GermEval2017.baseline.featureExtractor.util;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * A java Class for Confusion Matrix and Error Analysis
 */
public class ConfusionMatrix {

    private ArrayList<String> labels = new ArrayList<>();
    private HashMap<Pair<String, String>, Integer> matrix = new HashMap<>();

    public void addLabel(String label){
        labels.add(label);
    }

    public void createMatrix(){
        for(String predictedLabel:labels){
            for(String goldLabel:labels){
                matrix.put(new Pair<>(predictedLabel, goldLabel), 0);
            }
        }
    }

    /**
     * Adds an entry to the confusion matrix
     * @param predictedLabel the predicted label for the instance
     * @param goldLabel the actual gold label for the instance
     */
    public void updateMatrix(String predictedLabel, String goldLabel){
        Pair<String,String> entry = new Pair<>(predictedLabel, goldLabel);
        if (matrix.get(entry) == null) {
            for (String label : labels) {
                matrix.put(new Pair<>(label, goldLabel), 0);
            }
        }
            matrix.put(entry, matrix.get(entry)+1);

    }

    /**
     * Prints the confusion matrix
     */
    public void printConfusionMatrix(){
        System.out.println("Gold labels:\t Left to Right");
        System.out.println("Predicted labels:\t Top to bottom");
        for(String label:labels){
            System.out.print("\t"+label);
        }
        for(String predictedLabel:labels){
            System.out.print("\n"+predictedLabel);
            for(String goldLabel:labels){
                System.out.print("\t"+matrix.get(new Pair<>(predictedLabel, goldLabel)));
            }
        }
    }

    /**
     * Adds and returns the total number of gold instances of a particular label type
     * @param target the label for which the sum is to be returned
     * @return the total number of a particular gold label
     */
    private int getGoldSumForLabel(String target){
        int result = 0;
        for(String label:labels){
            result+=matrix.get(new Pair<>(label, target));
        }
        return result;
    }

    /**
     * Adds and returns the total number of predicted instances of a particular label type
     * @param target the label for which the sum is to be returned
     * @return the total number of a particular predicted label
     */
    private int getPredictedSumForLabel(String target){
        int result = 0;
        for(String label:labels){
            result+=matrix.get(new Pair<>(target, label));
        }
        return result;
    }

    /**
     * Returns the recall value of a particular label type
     * @param label the label for which the recall is to be returned
     * @return the recall value
     */
    public float getRecallForLabel(String label){
        float recall = 0;
        if(matrix.containsKey(new Pair<>(label, label)) && getGoldSumForLabel(label)>0){
            recall = (float)matrix.get(new Pair<>(label, label))/ (float) (getGoldSumForLabel(label));
        }
        return recall;
    }

    /**
     * Returns the precision value of a particular label type
     * @param label the label for which the precision is to be returned
     * @return the precision value
     */
    public float getPrecisionForLabel(String label){
        float precision = 0;
        if(matrix.containsKey(new Pair<>(label, label)) && getPredictedSumForLabel(label)>0){
            precision = (float) matrix.get(new Pair<>(label, label))/ (float) (getPredictedSumForLabel(label));
        }
        return precision;
    }

    /**
     * Returns the recall value for all labels
     * @return a hashmap of label name and their recall values
     */
    public HashMap<String, Float> getRecallForAllLabels(){
        HashMap<String, Float> recall = new HashMap<>();
        for(String label:labels){
            recall.put(label, getRecallForLabel(label));
        }
        return recall;
    }

    /**
     * Returns the precision value for all labels
     * @return a hashmap of label name and their precision values
     */
    public HashMap<String, Float> getPrecisionForAllLabels(){
        HashMap<String, Float> precision = new HashMap<>();
        for(String label:labels){
            precision.put(label, getPrecisionForLabel(label));
        }
        return precision;
    }

    /**
     * Returns the total number of true positives
     * @return an int indicating the total number of true positives
     */
    public int getTruePositive(){
        int result = 0;
        for(String label:labels){
            result+=matrix.get(new Pair<>(label, label));
        }
        return result;
    }


    /**
     * Returns the f measure value for all labels
     * @return a hashmap of label name and their f measure values
     */
    public HashMap<String, Float> getFMeasureForAllLabels(){
        HashMap<String, Float> fMeasure = new HashMap<>();
        for(String label:labels){
            if((getPrecisionForLabel(label)+getRecallForLabel(label))>0){
                fMeasure.put(label, (2*getPrecisionForLabel(label)*getRecallForLabel(label))/ (getPrecisionForLabel(label)+getRecallForLabel(label)));
            }
        }
        return fMeasure;
    }

    /**
     * Returns the overall recall value
     * @return a float indicating the overall recall value
     */
    public float getOverallRecall(){
        int num = 0;
        float sum = 0;
        for(String label:labels){
            sum+=getRecallForLabel(label);
            num++;
        }
        return sum/num;
    }

    /**
     * Returns the overall precision value
     * @return a float indicating the overall precision value
     */
    public float getOverallPrecision(){
        int num = 0;
        float sum = 0;
        for(String label:labels){
            sum+=getPrecisionForLabel(label);
            num++;
        }
        return sum/num;
    }


    /**
     * Returns the overall f measure value
     * @return a float indicating the overall f measure value
     */
    public float getOverallFMeasure(){
        return (2*getOverallPrecision()*getOverallRecall())/(getOverallPrecision()+getOverallRecall());
    }

    /**
     * Returns the overall accuracy value
     * @return a float indicating the overall accuracy value
     */
    public float getOverallAccuracy(){
        int truePositive = getTruePositive(), allPrediction = 0;
        for(String label1:labels){
            for(String label2:labels){
                allPrediction+=matrix.get(new Pair<>(label1, label2));
            }
        }
        return ((float) truePositive / (float) allPrediction);
    }
}