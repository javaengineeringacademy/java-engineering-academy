package academy.javaengineering.patterns.interpreter;

/**
 * Non-terminal expression for addition.
 * Contains two sub-expressions and adds their evaluated results.
 */
public class Add implements Expression {

    private final Expression left;
    private final Expression right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate() {
        return left.evaluate() + right.evaluate();
    }

    @Override
    public String toString() {
        return "(" + left + " + " + right + ")";
    }
}
