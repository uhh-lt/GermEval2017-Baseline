package uhh_lt.GermEval2017.baseline.training.aspecttarget;

import de.tudarmstadt.ukp.dkpro.core.clearnlp.ClearNlpLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import uhh_lt.GermEval2017.baseline.classifier.aspecttarget.AspectAnnotator;
import uhh_lt.GermEval2017.baseline.reader.ConllReader;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.cleartk.ml.CleartkSequenceAnnotator;
import org.cleartk.ml.crfsuite.CrfSuiteStringOutcomeDataWriter;
import org.cleartk.ml.jar.DefaultSequenceDataWriterFactory;
import org.cleartk.ml.jar.DirectoryDataWriterFactory;
import org.cleartk.util.cr.FilesCollectionReader;
import uhh_lt.GermEval2017.baseline.util.XMLExtractorTarget;

import java.io.File;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

/**
 * Aspect Target Identification Model Trainer
 */
public class Train {

    /**
     * Trains the model from an input file
     *
     * @param args optional: input file and directory for model
     */
    public static void main(String[] args) {

        File modelDirectory = new File("data/models/");
        String inputFile = "train.xml";

        if (args.length == 2) {
            inputFile = args[0];
            modelDirectory = new File(args[1]);
        } else if (args.length == 1) {
            inputFile = args[0];
        }


        if (inputFile.endsWith("xml")) {
            String[] xArgs = new String[2];
            xArgs[0] = inputFile;
            inputFile = inputFile.replace(".xml", "") + ".conll";
            xArgs[1] = inputFile;
            XMLExtractorTarget.main(xArgs);
        }

        File trainingFile = new File(inputFile);
        trainingFile.deleteOnExit();
        try {
            runPipeline(
                    FilesCollectionReader.getCollectionReaderWithSuffixes(trainingFile.getAbsolutePath(),
                            ConllReader.CONLL_VIEW, inputFile),
                    createEngine(ConllReader.class),
                    createEngine(OpenNlpPosTagger.class,
                            OpenNlpPosTagger.PARAM_MODEL_LOCATION, "data/models/opennlp-de-pos-maxent.bin"),

                    createEngine(ClearNlpLemmatizer.class),
                    AnalysisEngineFactory.createEngine(AspectAnnotator.class,
                            CleartkSequenceAnnotator.PARAM_IS_TRAINING, true,
                            DirectoryDataWriterFactory.PARAM_OUTPUT_DIRECTORY,
                            modelDirectory.getAbsolutePath(),
                            DefaultSequenceDataWriterFactory.PARAM_DATA_WRITER_CLASS_NAME,
                            CrfSuiteStringOutcomeDataWriter.class));
            org.cleartk.ml.jar.Train.main(modelDirectory.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
