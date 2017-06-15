package uhh_lt.GermEval2017.baseline.reader;

import uhh_lt.GermEval2017.baseline.type.Document;
import uhh_lt.GermEval2017.baseline.type.Sentence;

import java.io.*;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

/**
 * TSV input reader for tab separated input files.<br>
 * The input format is: ID &emsp; text &emsp; optional label
 */
public class TsvReader implements InputReader {

    private BufferedReader reader = null;
    private boolean checkedNext = false;
    private boolean hasNext = false;

    private String line;


    /**
     * Creates a Reader using a file name
     * @param filename the path and filename of the input file
     */
    public TsvReader(String filename) {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream(filename), "UTF-8"));
        } catch (Exception e) {
            System.err.println("Stream could not be opened: " + filename + "\nTrying filename...");
            try {
                reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            } catch (FileNotFoundException e1) {
                System.err.println("File could not be opened: " + filename);
                try {
                    System.err.println("Trying gzipped file: " + filename + ".gz ...");
                    reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(filename + ".gz")), "UTF-8"));
                } catch (IOException e2) {
                    e2.printStackTrace();
                    System.exit(1);
                }
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            System.err.println("... success.");
        }
    }

    @Override
    public Document next() {
        if (!checkedNext) {
            try {
                line = reader.readLine();
                while (line.isEmpty() || line.startsWith("SOURCE")) {
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkedNext = false;
        Document currentDoc = buildDocument(line);
        return currentDoc;
    }

    @Override
    public boolean hasNext() {
        if (!checkedNext) {
            checkedNext = true;
            line = "";
            try {
                // skip empty lines for robustness
                while (line.isEmpty() || line.startsWith("SOURCE")) {
                    line = reader.readLine();
                    if (line == null) {
                        hasNext = false;
                        return false;
                    }
                    hasNext = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                hasNext = false;
            }
        }
        return hasNext;
    }

    /**
     * Creats a {@link Document} from an input line.
     * @param line the input line
     * @return a {@link Document} with text, identifier and label
     */
    private Document buildDocument(String line) {
        Document doc = new Document();

        String[] documentFields = line.split("\\t");
        if (documentFields.length < 2 ) {
            throw new IllegalArgumentException("The document should at least have 2 fields, with optional labels in the following fields!");
        }
        doc.setDocumentId(documentFields[0]);
        doc.addSentence(new Sentence(documentFields[1]));

        if (documentFields.length >= 3) {
            doc.setRelevance(documentFields[2]);
        }
        if (documentFields.length >= 4) {
            doc.setDocumentSentiment(documentFields[3]);
        }
        if (documentFields.length >= 5) {
            doc.setDocumentAspects(documentFields[4]);
        }
        return doc;
    }

    @Override
    public Iterator<Document> iterator() {
        return this;
    }
}
