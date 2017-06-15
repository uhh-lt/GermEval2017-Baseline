package uhh_lt.GermEval2017.baseline.reader;

import uhh_lt.GermEval2017.baseline.type.Document;

import java.util.Iterator;

/**
 * Interface for input readers.
 */
public interface InputReader extends Iterable<Document>, Iterator<Document> {

    @Override
    boolean hasNext();

    @Override
    Document next();

}
