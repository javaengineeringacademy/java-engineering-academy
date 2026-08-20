package academy.javaengineering.testing.bdd.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BddTestingExamples {

    static class Calculator {
        private double result;
        void add(double a, double b) { result = a + b; }
        void subtract(double a, double b) { result = a - b; }
        double getResult() { return result; }
    }

    @Test
    void shouldAddNumbers() {
        // Given
        Calculator calc = new Calculator();
        // When
        calc.add(2, 3);
        // Then
        assertEquals(5, calc.getResult());
    }

    @Test
    void shouldSubtractNumbers() {
        Calculator calc = new Calculator();
        calc.subtract(10, 4);
        assertEquals(6, calc.getResult());
    }
}
