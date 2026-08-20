package academy.javaengineering.testing.fundamentals.practices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Write unit tests for the Calculator class.
 *
 * Tasks:
 * 1. Test add() with positive numbers
 * 2. Test add() with negative numbers
 * 3. Test add() with zero
 * 4. Test subtract() with various inputs
 * 5. Test multiply() with various inputs
 * 6. Test divide() with normal division
 * 7. Test divide() throws exception when dividing by zero
 */
class Calculator {

    int add(int a, int b) { return a + b; }
    int subtract(int a, int b) { return a - b; }
    int multiply(int a, int b) { return a * b; }

    double divide(double a, double b) {
        if (b == 0) throw new ArithmeticException("Cannot divide by zero");
        return a / b;
    }
}

class Exercise1CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // TODO: Write test for add with positive numbers
    @Test
    @DisplayName("add should return sum of two positive numbers")
    void shouldAddPositiveNumbers() {
        // Arrange, Act, Assert
    }

    // TODO: Write test for add with negative numbers
    @Test
    @DisplayName("add should handle negative numbers")
    void shouldAddNegativeNumbers() {
        // Arrange, Act, Assert
    }

    // TODO: Write test for add with zero
    @Test
    @DisplayName("add should handle zero")
    void shouldHandleZero() {
        // Arrange, Act, Assert
    }

    // TODO: Write test for subtract
    @Test
    @DisplayName("subtract should return difference")
    void shouldSubtractNumbers() {
        // Arrange, Act, Assert
    }

    // TODO: Write test for multiply
    @Test
    @DisplayName("multiply should return product")
    void shouldMultiplyNumbers() {
        // Arrange, Act, Assert
    }

    // TODO: Write test for divide
    @Test
    @DisplayName("divide should return quotient")
    void shouldDivideNumbers() {
        // Arrange, Act, Assert
    }

    // TODO: Write test for divide by zero
    @Test
    @DisplayName("divide should throw exception when dividing by zero")
    void shouldThrowOnDivisionByZero() {
        // Arrange, Act, Assert
    }
}
