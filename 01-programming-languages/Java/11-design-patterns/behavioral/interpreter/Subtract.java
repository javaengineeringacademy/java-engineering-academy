package academy.javaengineering.patterns.interpreter;

/**
 * Non-terminal expression for subtraction.
 * Contains two sub-expressions and subtracts the right from the left.
 */
public class Subtract implements Expression {

    private final Expression left;
    private final Expression right;

    public Subtract(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate() {
        return left.evaluate() - right.evaluate();
    }

    @Override
    public String toString() {
        return "(" + left + " - " + right + ")";
    }
}
