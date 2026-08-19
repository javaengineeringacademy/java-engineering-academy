package academy.javaengineering.patterns.interpreter;

/**
 * Expression interface — the abstract element in the Interpreter pattern.
 * Every grammar rule implements this interface to evaluate itself.
 */
public interface Expression {

    /**
     * Evaluate this expression and return the numeric result.
     *
     * @return the computed value
     */
    double evaluate();

    /**
     * Return a human-readable string representation of this expression.
     *
     * @return the expression as a string, e.g. "(3 + 4)"
     */
    String toString();
}
