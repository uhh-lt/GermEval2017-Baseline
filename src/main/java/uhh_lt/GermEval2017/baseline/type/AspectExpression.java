package uhh_lt.GermEval2017.baseline.type;

public class AspectExpression {

    private final int begin;
    private final int end;
    private final String aspectExpression;

    public AspectExpression(String expression, int begin, int end) {
        this.aspectExpression = expression;
        this.begin = begin;
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public String getAspectExpression() {
        return aspectExpression;
    }
}
