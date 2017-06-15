package uhh_lt.GermEval2017.baseline.reader;

import uhh_lt.GermEval2017.baseline.type.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uhh_lt.GermEval2017.baseline.featureExtractor.util.Pair;
import uhh_lt.GermEval2017.baseline.type.Opinion;
import uhh_lt.GermEval2017.baseline.type.Sentence;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;

/**
 * XML input reader to read GermEval sentiment XML format.
 */
public class XMLReader implements InputReader {

    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private org.w3c.dom.Document doc;

    private NodeList reviewList;
    private int reviewPosition;

    private static final String docTag = "Document";
    private static final String docAttrId = "id";
    private static final String sentenceTag = "text";
    private static final String opinionTag = "Opinion";
    private static final String relevanceTag = "relevance";
    private static final String sentimentTag = "sentiment";
    private static final String opinionAttrCategory = "category";
    private static final String opinionAttrPolarity = "polarity";
    private static final String opinionAttrTarget = "target";
    private static final String opinionAttrTargetBegin = "from";
    private static final String opinionAttrTargetEnd = "to";

    /**
     * Constructor, creates a {@link org.w3c.dom.Document} from an input file.
     * @param filename path and file name of the .xml file
     */
    public XMLReader(String filename) {
        dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            doc = dBuilder.parse(new FileInputStream(filename), "UTF-8");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("File could not be opened: " +filename );
            e.printStackTrace();
            System.exit(1);
        }
        doc.getDocumentElement().normalize();

        reviewList = doc.getElementsByTagName(docTag);
        reviewPosition = 0;
    }


    @Override
    public Iterator<Document> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return reviewPosition < reviewList.getLength();
    }

    @Override
    public Document next() {
        Node reviewNode = reviewList.item(reviewPosition);
        if (reviewNode.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) reviewNode;
            return buildDocument(elem);

        } else {
            return null;
        }
    }

    /**
     * Creates a {@link Document} from an element node in the XML graph
     * @param element the element node containing the document
     * @return a {@link Document} object with id, label and sentences (text)
     */
    private Document buildDocument(Element element) {
        Document doc = new Document();
        doc.setDocumentId(element.getAttribute(docAttrId));
        doc.setRelevance(element.getElementsByTagName(relevanceTag).item(0).getTextContent());
        doc.setDocumentSentiment(element.getElementsByTagName(sentimentTag).item(0).getTextContent());
        NodeList sList = element.getElementsByTagName(sentenceTag);
        Sentence s;

        for (int sI = 0; sI < sList.getLength(); sI++) {
            Node sNode = sList.item(sI);

            s = new Sentence( sNode.getTextContent());
            s.addOpinions(getOpinions(element));

            doc.addSentence(s);
        }
        reviewPosition++;
        return doc;
    }

    /**
     * Extracts opinions from a sentence element.
     * @param sNode the sentence Node
     * @return a Vector of opinions for the sentence
     */
    private Vector<Opinion> getOpinions(Node sNode) {
        String category,polarity,target;
        Vector<Opinion> opinions = new Vector<>();
        NodeList oList = ((Element) sNode).getElementsByTagName(opinionTag);
        for (int oI = 0; oI < oList.getLength(); oI++) {
            Element oNode = (Element) oList.item(oI);

            category =  oNode.getAttribute(opinionAttrCategory);
            polarity = oNode.getAttribute(opinionAttrPolarity);
            target = oNode.getAttribute(opinionAttrTarget);


            Opinion opinion = new Opinion(category, polarity, target);
            if (target.compareTo("NULL") != 0) {
                opinion.addTarget(new Pair<>(Integer.parseInt(oNode.getAttribute(opinionAttrTargetBegin)), Integer.parseInt(oNode.getAttribute(opinionAttrTargetEnd))));
                int i = 1;
                while (oNode.getAttribute(opinionAttrTargetBegin + i).compareTo("") != 0) {
                    opinion.addTarget(new Pair<>(Integer.parseInt(oNode.getAttribute(opinionAttrTargetBegin+i)), Integer.parseInt(oNode.getAttribute(opinionAttrTargetEnd+i))));
                    i++;
                }
            }

            opinions.add(opinion);
        }
        return  opinions;
    }

}
