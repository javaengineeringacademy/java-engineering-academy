package academy.javaengineering.patterns.interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    @Test
    void numberShouldEvaluateToItsValue() {
        Expression five = new Number(5);
        assertEquals(5.0, five.evaluate(), 0.001);
    }

    @Test
    void numberShouldFormatIntegerAsIntegerString() {
        Expression five = new Number(5);
        assertEquals("5", five.toString());
    }

    @Test
    void numberShouldFormatDecimalAsDecimalString() {
        Expression pi = new Number(3.14);
        assertEquals("3.14", pi.toString());
    }

    @Test
    void addShouldSumTwoExpressions() {
        Expression expr = new Add(new Number(3), new Number(4));
        assertEquals(7.0, expr.evaluate(), 0.001);
    }

    @Test
    void addShouldRepresentAsParenthesizedString() {
        Expression expr = new Add(new Number(3), new Number(4));
        assertEquals("(3 + 4)", expr.toString());
    }

    @Test
    void subtractShouldSubtractRightFromLeft() {
        Expression expr = new Subtract(new Number(10), new Number(3));
        assertEquals(7.0, expr.evaluate(), 0.001);
    }

    @Test
    void subtractShouldRepresentAsParenthesizedString() {
        Expression expr = new Subtract(new Number(10), new Number(3));
        assertEquals("(10 - 3)", expr.toString());
    }

    @Test
    void multiplyShouldMultiplyTwoExpressions() {
        Expression expr = new Multiply(new Number(4), new Number(5));
        assertEquals(20.0, expr.evaluate(), 0.001);
    }

    @Test
    void multiplyShouldRepresentAsParenthesizedString() {
        Expression expr = new Multiply(new Number(4), new Number(5));
        assertEquals("(4 * 5)", expr.toString());
    }

    @Test
    void complexExpressionShouldEvaluateCorrectly() {
        // (3 + 4) * 5 = 35
        Expression expr = new Multiply(
                new Add(new Number(3), new Number(4)),
                new Number(5)
        );
        assertEquals(35.0, expr.evaluate(), 0.001);
    }

    @Test
    void complexExpressionShouldRepresentAsNestedString() {
        Expression expr = new Multiply(
                new Add(new Number(3), new Number(4)),
                new Number(5)
        );
        assertEquals("((3 + 4) * 5)", expr.toString());
    }

    @Test
    void nestedExpressionShouldEvaluateCorrectly() {
        // (10 - 3) * (4 + 2) = 42
        Expression expr = new Multiply(
                new Subtract(new Number(10), new Number(3)),
                new Add(new Number(4), new Number(2))
        );
        assertEquals(42.0, expr.evaluate(), 0.001);
    }

    @Test
    void deeplyNestedExpressionShouldEvaluateCorrectly() {
        // ((3 + 4) * 5) - (2 * 3) = 29
        Expression expr = new Subtract(
                new Multiply(
                        new Add(new Number(3), new Number(4)),
                        new Number(5)
                ),
                new Multiply(new Number(2), new Number(3))
        );
        assertEquals(29.0, expr.evaluate(), 0.001);
    }

    @Test
    void shouldHandleDecimalValues() {
        Expression expr = new Add(new Number(1.5), new Number(2.5));
        assertEquals(4.0, expr.evaluate(), 0.001);
    }

    @Test
    void shouldHandleNegativeNumbers() {
        Expression expr = new Add(new Number(-3), new Number(7));
        assertEquals(4.0, expr.evaluate(), 0.001);
    }

    @Test
    void chainedAdditionShouldEvaluateCorrectly() {
        // (1 + 2) + 3 = 6
        Expression expr = new Add(
                new Add(new Number(1), new Number(2)),
                new Number(3)
        );
        assertEquals(6.0, expr.evaluate(), 0.001);
    }

    @Test
    void chainedSubtractionShouldEvaluateCorrectly() {
        // (10 - 3) - 2 = 5
        Expression expr = new Subtract(
                new Subtract(new Number(10), new Number(3)),
                new Number(2)
        );
        assertEquals(5.0, expr.evaluate(), 0.001);
    }
}
