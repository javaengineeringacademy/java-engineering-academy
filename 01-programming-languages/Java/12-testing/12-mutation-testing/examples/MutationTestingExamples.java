package academy.javaengineering.testing.mutation.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutationTestingExamples {

    static class Calculator {
        int add(int a, int b) { return a + b; }
        int subtract(int a, int b) { return a - b; }
        int multiply(int a, int b) { return a * b; }
        double divide(double a, double b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }
    }

    static class PasswordValidator {
        boolean isValid(String password) {
            return password != null && password.length() >= 8;
        }
    }

    @Test
    void shouldTestCalculator() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
        assertEquals(1, calc.subtract(3, 2));
        assertEquals(6, calc.multiply(2, 3));
        assertEquals(2.5, calc.divide(5, 2), 0.01);
    }

    @Test
    void shouldTestPasswordValidator() {
        PasswordValidator validator = new PasswordValidator();
        assertTrue(validator.isValid("12345678"));
        assertFalse(validator.isValid("short"));
        assertFalse(validator.isValid(null));
    }
}
