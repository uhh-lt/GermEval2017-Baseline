package uhh_lt.GermEval2017.baseline.training.aspecttarget;

import de.tudarmstadt.ukp.dkpro.core.clearnlp.ClearNlpLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import uhh_lt.GermEval2017.baseline.classifier.aspecttarget.AspectAnnotator;
import uhh_lt.GermEval2017.baseline.reader.ConllReader;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.cleartk.ml.jar.GenericJarClassifierFactory;
import org.cleartk.util.cr.FilesCollectionReader;

import java.io.File;
import java.io.IOException;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

/**
 * Aspect Target Identification Model Trainer
 */
public class Test {

    /**
     * Classifies an input file from a model
     * @param args optional: input file and model directory
     */
    public static void main(String[] args) {
        File testFile = new File("data/targets_test.connl");
        File modelDirectory = new File("data/models/");

        if (args.length == 2) {
            testFile = new File(args[0]);
            modelDirectory = new File(args[1]);
        }

        try {
            runPipeline(
                    FilesCollectionReader.getCollectionReaderWithSuffixes(testFile.getAbsolutePath(),
                            ConllReader.CONLL_VIEW, testFile.getName()),
                    createEngine(ConllReader.class),
                    AnalysisEngineFactory.createEngine(OpenNlpPosTagger.class,
                            OpenNlpPosTagger.PARAM_MODEL_LOCATION, "data/models/opennlp-de-pos-maxent.bin"),
                    AnalysisEngineFactory.createEngine(ClearNlpLemmatizer.class),
                    AnalysisEngineFactory.createEngine(AspectAnnotator.class,
                            GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH,
                            modelDirectory.getAbsolutePath() + "/model.jar"),
                    createEngine(AspectTermWriter.class, AspectTermWriter.OUTPUT_FILE, testFile+"_out",
                            AspectTermWriter.IS_GOLD, true));
        } catch (UIMAException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
