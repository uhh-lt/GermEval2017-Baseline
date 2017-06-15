package uhh_lt.GermEval2017.baseline.featureExtractor.util;

/**
 * A Java Class for storing a pair of values
 * @param <A> The first element of the pair
 * @param <B> The second element of the pair
 */
public class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     * Computes and returns the hash code of the pair object
     * @return the hash code of the pair object
     */
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    /**
     * Checks if the first and the second element are equal in the object
     * @param other The object whose elements are to be compared
     * @return a boolean indicating if the elements are equal or not
     */
    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return
                    ((  this.first == otherPair.first ||
                            ( this.first != null && otherPair.first != null &&
                                    this.first.equals(otherPair.first))) &&
                            (  this.second == otherPair.second ||
                                    ( this.second != null && otherPair.second != null &&
                                            this.second.equals(otherPair.second))) );
        }

        return false;
    }

    /**
     * Returns the string representation
     * @return a String with the first and the second element
     */
    public String toString()
    {
        return "(" + first + ", " + second + ")";
    }

    /**
     * Returns the first element
     * @return the first element of the Pair object
     */
    public A getFirst() {
        return first;
    }

    /**
     * Sets the first element
     * @param first the entity to be stored in the first element
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * Returns the second element
     * @return the second element of the Pair object
     */
    public B getSecond() {
        return second;
    }

    /**
     * Sets the second element
     * @param second the entity to be stored in the second element
     */
    public void setSecond(B second) {
        this.second = second;
    }
}