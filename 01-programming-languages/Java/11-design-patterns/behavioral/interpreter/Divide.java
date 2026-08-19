package academy.javaengineering.patterns.behavioral.interpreter;

/**
 * Non-terminal expression for division.
 * Contains two sub-expressions and divides the left by the right.
 * Throws ArithmeticException for division by zero.
 */
public class Divide implements Expression {

    private final Expression left;
    private final Expression right;

    public Divide(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate() {
        double rightValue = right.evaluate();
        if (rightValue == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left.evaluate() / rightValue;
    }

    @Override
    public String toString() {
        return "(" + left + " / " + right + ")";
    }
}
