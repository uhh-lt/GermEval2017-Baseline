package uhh_lt.GermEval2017.baseline;


import uhh_lt.GermEval2017.baseline.training.aspecttarget.Train;
import uhh_lt.GermEval2017.baseline.training.aspectclass.TrainCoarse;

import java.io.File;

public class TrainAllClassifiers {

    public static void main(String [] args) {

        uhh_lt.GermEval2017.baseline.training.relevance.Train.main(args);
        uhh_lt.GermEval2017.baseline.training.aspectclass.Train.main(args);
        TrainCoarse.main(args);
        uhh_lt.GermEval2017.baseline.training.sentiment.Train.main(args);

        if (args.length > 0 && args[0].endsWith(".xml")) {
            Train.main(args);
        }

        // remove temporary CRF data
        File model = new File("data/models/crfsuite.model");
        model.deleteOnExit();
        File trainData = new File("data/models/crfsuite.training");
        trainData.deleteOnExit();
        File encoders = new File("data/models/encoders.ser");
        encoders.deleteOnExit();
        File manifest = new File("data/models/MANIFEST.MF");
        manifest.deleteOnExit();
    }
}
