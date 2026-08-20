package academy.javaengineering.testing.practices;

/**
 * Testing Fundamentals Exercises
 * Complete each exercise using AAA pattern and FIRST principles
 */
class TestingFundamentalsExercises {

    // ============================================
    // Exercise 1: Calculator
    // ============================================
    // TODO: Implement tests for Calculator using AAA pattern
    // Arrange: Set up test data
    // Act: Execute the method
    // Assert: Verify the result

    static class Calculator {
        int add(int a, int b) { return a + b; }
        int subtract(int a, int b) { return a - b; }
        int multiply(int a, int b) { return a * b; }
        int divide(int a, int b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }
        int power(int base, int exponent) {
            if (exponent < 0) throw new IllegalArgumentException("Negative exponent");
            int result = 1;
            for (int i = 0; i < exponent; i++) result *= base;
            return result;
        }
    }

    static class CalculatorTest {
        Calculator calc = new Calculator();

        // TODO: Write 5 test methods
        // 1. testAddition
        // 2. testSubtraction
        // 3. testMultiplication
        // 4. testDivision
        // 5. testDivisionByZero

        void testAddition() {
            // TODO: Implement
        }

        void testSubtraction() {
            // TODO: Implement
        }

        void testMultiplication() {
            // TODO: Implement
        }

        void testDivision() {
            // TODO: Implement
        }

        void testDivisionByZero() {
            // TODO: Implement
        }
    }

    // ============================================
    // Exercise 2: StringValidator
    // ============================================
    // TODO: Implement tests following FIRST principles
    // Fast: Tests should execute quickly
    // Independent: Tests should not depend on each other
    // Repeatable: Tests should produce same result every time
    // Self-validating: Tests should have clear pass/fail
    // Timely: Tests written alongside code

    static class StringValidator {
        boolean isValidEmail(String email) {
            if (email == null) return false;
            return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        }

        boolean isValidPassword(String password) {
            if (password == null || password.length() < 8) return false;
            boolean hasUpper = false, hasLower = false, hasDigit = false;
            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUpper = true;
                if (Character.isLowerCase(c)) hasLower = true;
                if (Character.isDigit(c)) hasDigit = true;
            }
            return hasUpper && hasLower && hasDigit;
        }

        boolean isValidUsername(String username) {
            if (username == null) return false;
            if (username.length() < 3 || username.length() > 20) return false;
            return username.matches("^[a-zA-Z0-9_]+$");
        }
    }

    static class StringValidatorTest {
        StringValidator validator = new StringValidator();

        // TODO: Write 6 test methods (2 for each method)
        void testValidEmail() {
            // TODO: Implement
        }

        void testInvalidEmail() {
            // TODO: Implement
        }

        void testValidPassword() {
            // TODO: Implement
        }

        void testInvalidPassword() {
            // TODO: Implement
        }

        void testValidUsername() {
            // TODO: Implement
        }

        void testInvalidUsername() {
            // TODO: Implement
        }
    }

    // ============================================
    // Exercise 3: TemperatureConverter
    // ============================================
    // TODO: Write tests that cover edge cases and boundaries

    static class TemperatureConverter {
        double celsiusToFahrenheit(double celsius) {
            return (celsius * 9/5) + 32;
        }

        double fahrenheitToCelsius(double fahrenheit) {
            return (fahrenheit - 32) * 5/9;
        }

        double celsiusToKelvin(double celsius) {
            if (celsius < -273.15) throw new IllegalArgumentException("Below absolute zero");
            return celsius + 273.15;
        }

        double kelvinToCelsius(double kelvin) {
            if (kelvin < 0) throw new IllegalArgumentException("Negative Kelvin");
            return kelvin - 273.15;
        }
    }

    static class TemperatureConverterTest {
        TemperatureConverter converter = new TemperatureConverter();

        // TODO: Write 6 test methods covering edge cases
        void testCelsiusToFahrenheit() {
            // TODO: Implement (test 0, 100, -40)
        }

        void testFahrenheitToCelsius() {
            // TODO: Implement (test 32, 212, -40)
        }

        void testCelsiusToKelvin() {
            // TODO: Implement (test 0, -273.15 boundary)
        }

        void testKelvinToCelsius() {
            // TODO: Implement (test 273.15, 0)
        }

        void testAbsoluteZero() {
            // TODO: Implement (test below -273.15 throws exception)
        }

        void testNegativeKelvin() {
            // TODO: Implement (test negative Kelvin throws exception)
        }
    }

    // ============================================
    // Exercise 4: FizzBuzz
    // ============================================
    // TODO: Implement tests for FizzBuzz logic

    static class FizzBuzz {
        String evaluate(int number) {
            if (number <= 0) throw new IllegalArgumentException("Number must be positive");
            if (number % 15 == 0) return "FizzBuzz";
            if (number % 3 == 0) return "Fizz";
            if (number % 5 == 0) return "Buzz";
            return String.valueOf(number);
        }
    }

    static class FizzBuzzTest {
        FizzBuzz fizzBuzz = new FizzBuzz();

        // TODO: Write 5 test methods
        void testFizz() {
            // TODO: Implement (test 3, 6, 9)
        }

        void testBuzz() {
            // TODO: Implement (test 5, 10, 20)
        }

        void testFizzBuzz() {
            // TODO: Implement (test 15, 30)
        }

        void testNumber() {
            // TODO: Implement (test 1, 2, 4, 7)
        }

        void testInvalidInput() {
            // TODO: Implement (test 0, -1 throws exception)
        }
    }

    // ============================================
    // Exercise 5: ArrayUtils
    // ============================================
    // TODO: Write tests for array operations

    static class ArrayUtils {
        int findMax(int[] arr) {
            if (arr == null || arr.length == 0)
                throw new IllegalArgumentException("Array cannot be empty");
            int max = arr[0];
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) max = arr[i];
            }
            return max;
        }

        int findMin(int[] arr) {
            if (arr == null || arr.length == 0)
                throw new IllegalArgumentException("Array cannot be empty");
            int min = arr[0];
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] < min) min = arr[i];
            }
            return min;
        }

        double average(int[] arr) {
            if (arr == null || arr.length == 0)
                throw new IllegalArgumentException("Array cannot be empty");
            int sum = 0;
            for (int num : arr) sum += num;
            return (double) sum / arr.length;
        }

        int[] reverse(int[] arr) {
            if (arr == null) return null;
            int[] reversed = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                reversed[i] = arr[arr.length - 1 - i];
            }
            return reversed;
        }
    }

    static class ArrayUtilsTest {
        ArrayUtils utils = new ArrayUtils();

        // TODO: Write 5 test methods
        void testFindMax() {
            // TODO: Implement
        }

        void testFindMin() {
            // TODO: Implement
        }

        void testAverage() {
            // TODO: Implement
        }

        void testReverse() {
            // TODO: Implement
        }

        void testEmptyArrayThrowsException() {
            // TODO: Implement
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Testing Fundamentals Exercises ===");
        System.out.println("Complete each exercise by implementing the TODO methods.");
        System.out.println("Check solutions folder for reference implementations.");
    }
}
