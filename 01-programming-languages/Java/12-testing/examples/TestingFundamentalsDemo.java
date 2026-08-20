package academy.javaengineering.testing;

/**
 * Testing Fundamentals Demo
 *
 * Covers:
 * - Arrange-Act-Assert (AAA) pattern
 * - FIRST principles (Fast, Independent, Repeatable, Self-validating, Timely)
 * - Test types: unit, integration, functional, regression
 * - Test naming conventions
 * - Boundary testing, equivalence partitioning
 */
public class TestingFundamentalsDemo {

    // ---- Sample System Under Test (SUT) ----

    static class Calculator {
        int add(int a, int b) {
            return a + b;
        }

        int subtract(int a, int b) {
            return a - b;
        }

        int multiply(int a, int b) {
            return a * b;
        }

        int divide(int a, int b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }

        boolean isEven(int n) {
            return n % 2 == 0;
        }
    }

    static class StringValidator {
        boolean isValidEmail(String email) {
            if (email == null || email.isBlank()) return false;
            return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        }

        boolean isStrongPassword(String password) {
            if (password == null || password.length() < 8) return false;
            boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
            boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
            boolean hasDigit = password.chars().anyMatch(Character::isDigit);
            boolean hasSpecial = password.chars().anyMatch(c -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(c) >= 0);
            return hasUpper && hasLower && hasDigit && hasSpecial;
        }
    }

    static class TemperatureConverter {
        double celsiusToFahrenheit(double celsius) {
            return celsius * 9.0 / 5.0 + 32;
        }

        double fahrenheitToCelsius(double fahrenheit) {
            return (fahrenheit - 32) * 5.0 / 9.0;
        }

        double celsiusToKelvin(double celsius) {
            return celsius + 273.15;
        }
    }

    static class ArrayUtils {
        int findMax(int[] arr) {
            if (arr == null || arr.length == 0) throw new IllegalArgumentException("Array must not be empty");
            int max = arr[0];
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) max = arr[i];
            }
            return max;
        }

        int[] reverse(int[] arr) {
            if (arr == null || arr.length == 0) return arr;
            int[] result = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                result[i] = arr[arr.length - 1 - i];
            }
            return result;
        }

        boolean contains(int[] arr, int value) {
            if (arr == null) return false;
            for (int v : arr) {
                if (v == value) return true;
            }
            return false;
        }
    }

    // ---- Demo: AAA Pattern ----

    /**
     * AAA Pattern Explanation:
     *
     * ARRANGE: Set up test data, mock objects, expected values
     * ACT:     Invoke the method under test
     * ASSERT:  Verify the result matches expectations
     *
     * This pattern makes tests readable and maintainable.
     */
    static class CalculatorTest {

        void testAddPositiveNumbers() {
            // Arrange
            Calculator calc = new Calculator();
            int a = 5, b = 3;

            // Act
            int result = calc.add(a, b);

            // Assert
            assert result == 8 : "Expected 8 but got " + result;
        }

        void testAddNegativeNumbers() {
            Calculator calc = new Calculator();
            int result = calc.add(-5, -3);
            assert result == -8;
        }

        void testDivideByZero() {
            Calculator calc = new Calculator();
            try {
                calc.divide(10, 0);
                assert false : "Expected ArithmeticException";
            } catch (ArithmeticException e) {
                assert "Division by zero".equals(e.getMessage());
            }
        }
    }

    // ---- FIRST Principles ----

    /**
     * F - Fast: Tests run in milliseconds
     * I - Independent: Each test stands alone, no dependencies
     * R - Repeatable: Same result every time, no flakiness
     * S - Self-validating: Pass/fail without manual inspection
     * T - Timely: Written alongside or before production code (TDD)
     */

    // ---- Boundary Testing ----

    static class BoundaryTestDemo {

        void testEdgeCases() {
            StringValidator validator = new StringValidator();

            // Null boundary
            assert !validator.isValidEmail(null);

            // Empty string boundary
            assert !validator.isValidEmail("");

            // Whitespace boundary
            assert !validator.isValidEmail("   ");

            // Valid email
            assert validator.isValidEmail("user@example.com");

            // Missing @
            assert !validator.isValidEmail("userexample.com");

            // Password length boundary
            assert !validator.isStrongPassword("Ab1!abcd"); // exactly 8 chars with all required
            assert !validator.isStrongPassword("Short1!");  // 8 chars but no lowercase? No, it has all
            assert !validator.isStrongPassword("Ab1!");     // too short (4 chars)
        }
    }

    // ---- Equivalence Partitioning ----

    /**
     * Divide input into partitions:
     * - Valid partitions: inputs that should be handled the same way
     * - Invalid partitions: inputs that should be rejected similarly
     *
     * Test one representative from each partition.
     */

    // ---- Test Naming Conventions ----

    /**
     * Convention 1: testMethodName ExpectedBehavior WhenCondition
     *   testAdd_ThrowsException_WhenDivisorIsZero
     *
     * Convention 2: methodName_StateUnderTest_ExpectedBehavior
     *   add_PositiveNumbers_ReturnsSum
     *
     * Convention 3: shouldExpectedBehaviorWhenCondition (BDD style)
     *   shouldReturnSumWhenPositiveNumbersAreAdded
     */

    // ---- Running Demo ----

    public static void main(String[] args) {
        System.out.println("=== Testing Fundamentals Demo ===\n");

        CalculatorTest calculatorTest = new CalculatorTest();
        BoundaryTestDemo boundaryTest = new BoundaryTestDemo();

        System.out.println("--- AAA Pattern Tests ---");
        calculatorTest.testAddPositiveNumbers();
        System.out.println("PASS: testAddPositiveNumbers");
        calculatorTest.testAddNegativeNumbers();
        System.out.println("PASS: testAddNegativeNumbers");
        calculatorTest.testDivideByZero();
        System.out.println("PASS: testDivideByZero");

        System.out.println("\n--- Boundary Tests ---");
        boundaryTest.testEdgeCases();
        System.out.println("PASS: testEdgeCases");

        System.out.println("\n--- FIRST Principles Reminder ---");
        System.out.println("Fast | Independent | Repeatable | Self-validating | Timely");

        System.out.println("\n--- Temperature Conversion ---");
        TemperatureConverter converter = new TemperatureConverter();
        System.out.println("0°C = " + converter.celsiusToFahrenheit(0) + "°F");
        System.out.println("100°C = " + converter.celsiusToFahrenheit(100) + "°F");
        System.out.println("32°F = " + converter.fahrenheitToCelsius(32) + "°C");
        System.out.println("0°C = " + converter.celsiusToKelvin(0) + "K");

        System.out.println("\nAll tests passed!");
    }
}
