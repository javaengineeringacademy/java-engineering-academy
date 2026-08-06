package academy.javaengineering.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * Exercises: JUnit Annotations and Assertions
 *
 * Complete the TODO sections below.
 * Note: These exercises demonstrate test structure. Run the main method to verify.
 */
public class JunitExercises {

    // TODO 1: Implement a Calculator class with methods to test
    public static class Calculator {
        public int add(int a, int b) {
            // TODO: implement
            return 0;
        }

        public int subtract(int a, int b) {
            // TODO: implement
            return 0;
        }

        public int multiply(int a, int b) {
            // TODO: implement
            return 0;
        }

        public double divide(double a, double b) {
            // TODO: implement - throw ArithmeticException if b == 0
            return 0;
        }

        public boolean isEven(int number) {
            // TODO: implement
            return false;
        }

        public List<Integer> fibonacci(int n) {
            // TODO: implement - return first n Fibonacci numbers
            return new ArrayList<>();
        }
    }

    // TODO 2: Implement a StringValidator with validation methods
    public static class StringValidator {
        public boolean isEmail(String input) {
            // TODO: implement basic email validation
            return false;
        }

        public boolean isPhoneNumber(String input) {
            // TODO: implement phone validation (format: XXX-XXX-XXXX)
            return false;
        }

        public boolean isStrongPassword(String input) {
            // TODO: implement: 8+ chars, upper, lower, digit
            return false;
        }
    }

    // TODO 3: Implement a TemperatureConverter for assertion testing
    public static class TemperatureConverter {
        public double celsiusToFahrenheit(double celsius) {
            // TODO: implement
            return 0;
        }

        public double fahrenheitToCelsius(double fahrenheit) {
            // TODO: implement
            return 0;
        }

        public double celsiusToKelvin(double celsius) {
            // TODO: implement
            return 0;
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        JunitExercises exercises = new JunitExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== JunitExercises Tests ===\n");

        Calculator calc = new Calculator();
        StringValidator validator = new StringValidator();
        TemperatureConverter temp = new TemperatureConverter();

        // Test Calculator
        total++;
        if (calc.add(2, 3) == 5 && calc.add(-1, 1) == 0) {
            System.out.println("Test 1a PASSED: Calculator.add");
            passed++;
        } else {
            System.out.println("Test 1a FAILED: Calculator.add");
        }

        total++;
        if (calc.subtract(10, 3) == 7) {
            System.out.println("Test 1b PASSED: Calculator.subtract");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: Calculator.subtract");
        }

        total++;
        if (calc.multiply(4, 5) == 20) {
            System.out.println("Test 1c PASSED: Calculator.multiply");
            passed++;
        } else {
            System.out.println("Test 1c FAILED: Calculator.multiply");
        }

        total++;
        try {
            double result = calc.divide(10, 2);
            if (Math.abs(result - 5.0) < 0.001) {
                System.out.println("Test 1d PASSED: Calculator.divide");
                passed++;
            } else {
                System.out.println("Test 1d FAILED: Calculator.divide - " + result);
            }
        } catch (Exception e) {
            System.out.println("Test 1d FAILED: " + e.getMessage());
        }

        total++;
        try {
            calc.divide(10, 0);
            System.out.println("Test 1e FAILED: should throw ArithmeticException");
        } catch (ArithmeticException e) {
            System.out.println("Test 1e PASSED: Calculator.divide by zero");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 1e FAILED: wrong exception type");
        }

        total++;
        if (calc.isEven(4) && !calc.isEven(3) && calc.isEven(0)) {
            System.out.println("Test 1f PASSED: Calculator.isEven");
            passed++;
        } else {
            System.out.println("Test 1f FAILED: Calculator.isEven");
        }

        total++;
        List<Integer> fib = calc.fibonacci(6);
        if (fib.equals(List.of(0, 1, 1, 2, 3, 5))) {
            System.out.println("Test 1g PASSED: Calculator.fibonacci");
            passed++;
        } else {
            System.out.println("Test 1g FAILED: Calculator.fibonacci - " + fib);
        }

        // Test StringValidator
        total++;
        if (validator.isEmail("user@example.com") && !validator.isEmail("invalid")) {
            System.out.println("Test 2a PASSED: StringValidator.isEmail");
            passed++;
        } else {
            System.out.println("Test 2a FAILED: StringValidator.isEmail");
        }

        total++;
        if (validator.isPhoneNumber("123-456-7890") && !validator.isPhoneNumber("1234567890")) {
            System.out.println("Test 2b PASSED: StringValidator.isPhoneNumber");
            passed++;
        } else {
            System.out.println("Test 2b FAILED: StringValidator.isPhoneNumber");
        }

        total++;
        if (validator.isStrongPassword("MyP@ss1") && !validator.isStrongPassword("weak")) {
            System.out.println("Test 2c PASSED: StringValidator.isStrongPassword");
            passed++;
        } else {
            System.out.println("Test 2c FAILED: StringValidator.isStrongPassword");
        }

        // Test TemperatureConverter
        total++;
        if (Math.abs(temp.celsiusToFahrenheit(0) - 32) < 0.01
            && Math.abs(temp.celsiusToFahrenheit(100) - 212) < 0.01) {
            System.out.println("Test 3a PASSED: celsiusToFahrenheit");
            passed++;
        } else {
            System.out.println("Test 3a FAILED: celsiusToFahrenheit");
        }

        total++;
        if (Math.abs(temp.fahrenheitToCelsius(32)) < 0.01
            && Math.abs(temp.fahrenheitToCelsius(212) - 100) < 0.01) {
            System.out.println("Test 3b PASSED: fahrenheitToCelsius");
            passed++;
        } else {
            System.out.println("Test 3b FAILED: fahrenheitToCelsius");
        }

        total++;
        if (Math.abs(temp.celsiusToKelvin(0) - 273.15) < 0.01) {
            System.out.println("Test 3c PASSED: celsiusToKelvin");
            passed++;
        } else {
            System.out.println("Test 3c FAILED: celsiusToKelvin");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
