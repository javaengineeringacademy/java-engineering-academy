package academy.javaengineering.patterns.interpreter;

/**
 * Non-terminal expression for multiplication.
 * Contains two sub-expressions and multiplies their evaluated results.
 */
public class Multiply implements Expression {

    private final Expression left;
    private final Expression right;

    public Multiply(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate() {
        return left.evaluate() * right.evaluate();
    }

    @Override
    public String toString() {
        return "(" + left + " * " + right + ")";
    }
}
