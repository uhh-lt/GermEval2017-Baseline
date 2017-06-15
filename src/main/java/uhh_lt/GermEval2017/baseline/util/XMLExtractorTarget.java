package uhh_lt.GermEval2017.baseline.util;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import uhh_lt.GermEval2017.baseline.featureExtractor.util.Pair;
import uhh_lt.GermEval2017.baseline.reader.InputReader;
import uhh_lt.GermEval2017.baseline.reader.XMLReader;
import uhh_lt.GermEval2017.baseline.type.Document;
import uhh_lt.GermEval2017.baseline.type.uima.GoldAspectTarget;
import uhh_lt.GermEval2017.baseline.uimahelper.Preprocessor;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.io.*;

public class XMLExtractorTarget {

    public static void main(String[] args) {

        String inputFile = args[0];
        String outputFile = inputFile.replace(".xml", "") + ".conll";

        Writer out = null;

        try {
            OutputStream predStream = new FileOutputStream(outputFile);
            out = new OutputStreamWriter(predStream, "UTF-8");

        } catch (FileNotFoundException | UnsupportedEncodingException e1) {
            e1.printStackTrace();
            System.exit(1);
        }


        InputReader in = new XMLReader(inputFile);

        Preprocessor preprocessor = new Preprocessor();

        for (Document d: in) {
            preprocessor.processText(d.getDocumentText());
            JCas cas = preprocessor.getCas();
            for (Pair<Integer, Integer> o : d.getTargetOffsets()) {
                GoldAspectTarget t = new GoldAspectTarget(cas, o.getFirst(), o.getSecond());
                t.addToIndexes();
            }
            boolean inTarget = false;
            for (Token t : JCasUtil.selectCovered(cas, Token.class, 0, cas.getDocumentText().length())) {
                try {
                    out.append(t.getCoveredText()).append("\t");
                    if (JCasUtil.selectCovered(GoldAspectTarget.class, t).size() > 0) {
                        if (inTarget) {
                            out.append("I\n");
                        } else {
                            out.append("B\n");
                            inTarget = true;
                        }
                    } else {
                        out.append("O\n");
                        inTarget = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            try {
                out.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

