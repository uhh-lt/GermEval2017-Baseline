package uhh_lt.GermEval2017.baseline;


import org.w3c.dom.Element;
import uhh_lt.GermEval2017.baseline.reader.InputReader;
import uhh_lt.GermEval2017.baseline.reader.TsvReader;
import uhh_lt.GermEval2017.baseline.reader.XMLReader;
import uhh_lt.GermEval2017.baseline.type.AspectExpression;
import uhh_lt.GermEval2017.baseline.type.Document;
import uhh_lt.GermEval2017.baseline.type.Result;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class Classify {

    private static boolean xmlData = false;

    private static String outputFile;

    private static Writer out = null;
    private static org.w3c.dom.Document results;
    private static Element documents;


    public static void main(String[] args) {



        String inputFile = "dev.tsv";

        if (args.length > 0) {
            inputFile = args[0];
        }
        if (inputFile.endsWith("xml")) {
            xmlData = true;
        }

        outputFile = inputFile.substring(0, inputFile.lastIndexOf(".")) + "_classified" +
                inputFile.substring(inputFile.lastIndexOf("."));

        AbSentiment classifier = new AbSentiment();


        InputReader fr;

        if (xmlData){
            fr = new XMLReader(inputFile);
        } else {
            fr = new TsvReader(inputFile);
        }

        // initialize Writers
        initializeOutput();

        uhh_lt.GermEval2017.baseline.type.Result res;
        for (Document d : fr) {
            res = classifier.analyzeText(d.getDocumentText());
            addResult(d, res);

        }

        // terminate writers
        writeDocuments();

    }

    private static void initializeOutput() {
        if (xmlData) {

            DocumentBuilderFactory docFactory;
            docFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                results = docBuilder.newDocument();

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            documents = results.createElement("Documents");
            results.appendChild(documents);
        } else {
            try {
                OutputStream predStream = new FileOutputStream(outputFile);
                out = new OutputStreamWriter(predStream, "UTF-8");
            } catch (FileNotFoundException | UnsupportedEncodingException e1) {
                e1.printStackTrace();
                System.exit(1);
            }
        }
    }

    private static void writeDocuments() {
        if (xmlData) {
            Transformer transformer;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(results);
                StreamResult result = new StreamResult(new File(outputFile));

                transformer.transform(source, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        } else {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addResult(Document d, Result res) {
        if (xmlData) {
            Element doc = results.createElement("Document");
            doc.setAttribute("id", d.getDocumentId());
            documents.appendChild(doc);

            Element text = results.createElement("text");
            text.setTextContent(d.getDocumentText());
            doc.appendChild(text);

            Element relevance = results.createElement("relevance");
            relevance.setTextContent(res.getRelevance());
            doc.appendChild(relevance);

            Element sentiment = results.createElement("sentiment");
            sentiment.setTextContent(res.getSentiment());
            doc.appendChild(sentiment);

            Element opinions = results.createElement("Opinions");
            doc.appendChild(opinions);
            if (res.getAspectExpressions().size() == 0) {
                Element opinion = results.createElement("Opinion");
                opinion.setAttribute("from", "0");
                opinion.setAttribute("to", "0");
                opinion.setAttribute("target", "NULL");
                opinion.setAttribute("polarity", res.getSentiment());
                opinion.setAttribute("category", res.getAspect());
                opinions.appendChild(opinion);
            } else {
                for (AspectExpression aspectExpression : res.getAspectExpressions()) {
                    Element opinion = results.createElement("Opinion");
                    opinion.setAttribute("from", aspectExpression.getBegin()+"");
                    opinion.setAttribute("to", aspectExpression.getEnd() +"");
                    opinion.setAttribute("target", aspectExpression.getAspectExpression());
                    opinion.setAttribute("polarity", res.getSentiment());
                    opinion.setAttribute("category", res.getAspect());
                    opinions.appendChild(opinion);
                }
            }

        } else {
            try {
                out.write(d.getDocumentId() + "\t" + d.getDocumentText() + "\t");
                out.write(res.getRelevance() + "\t" + res.getSentiment() + "\t" + res.getAspect() + ":" + res.getSentiment()+ "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}