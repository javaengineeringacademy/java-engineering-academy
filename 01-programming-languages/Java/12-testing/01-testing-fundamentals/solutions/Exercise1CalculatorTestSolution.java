package academy.javaengineering.testing.fundamentals.solutions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorSolution {
    int add(int a, int b) { return a + b; }
    int subtract(int a, int b) { return a - b; }
    int multiply(int a, int b) { return a * b; }
    double divide(double a, double b) {
        if (b == 0) throw new ArithmeticException("Cannot divide by zero");
        return a / b;
    }
}

class Exercise1CalculatorTestSolution {

    private CalculatorSolution calculator;

    @BeforeEach
    void setUp() {
        calculator = new CalculatorSolution();
    }

    @Test
    @DisplayName("add should return sum of two positive numbers")
    void shouldAddPositiveNumbers() {
        assertEquals(5, calculator.add(2, 3));
        assertEquals(100, calculator.add(50, 50));
    }

    @Test
    @DisplayName("add should handle negative numbers")
    void shouldAddNegativeNumbers() {
        assertEquals(-5, calculator.add(-2, -3));
        assertEquals(-1, calculator.add(2, -3));
    }

    @Test
    @DisplayName("add should handle zero")
    void shouldHandleZero() {
        assertEquals(5, calculator.add(5, 0));
        assertEquals(0, calculator.add(0, 0));
    }

    @Test
    @DisplayName("subtract should return difference")
    void shouldSubtractNumbers() {
        assertEquals(2, calculator.subtract(5, 3));
        assertEquals(-2, calculator.subtract(3, 5));
        assertEquals(0, calculator.subtract(5, 5));
    }

    @Test
    @DisplayName("multiply should return product")
    void shouldMultiplyNumbers() {
        assertEquals(15, calculator.multiply(3, 5));
        assertEquals(0, calculator.multiply(0, 100));
        assertEquals(-6, calculator.multiply(2, -3));
    }

    @Test
    @DisplayName("divide should return quotient")
    void shouldDivideNumbers() {
        assertEquals(2.5, calculator.divide(5, 2), 0.001);
        assertEquals(0.0, calculator.divide(0, 5), 0.001);
    }

    @Test
    @DisplayName("divide should throw exception when dividing by zero")
    void shouldThrowOnDivisionByZero() {
        assertThrows(ArithmeticException.class,
            () -> calculator.divide(10, 0));
    }
}
