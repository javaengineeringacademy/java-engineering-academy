package academy.javaengineering.patterns.interpreter;

/**
 * Terminal expression representing a numeric literal.
 * This is a leaf node in the expression tree — it returns its value directly.
 */
public class Number implements Expression {

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double evaluate() {
        return value;
    }

    @Override
    public String toString() {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
