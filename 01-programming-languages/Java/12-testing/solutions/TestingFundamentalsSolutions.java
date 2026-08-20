package academy.javaengineering.testing.solutions;

/**
 * Testing Fundamentals Solutions
 * Complete solutions for AAA pattern and FIRST principles exercises
 */
class TestingFundamentalsSolutions {

    // ============================================
    // Exercise 1: Calculator - AAA Pattern
    // ============================================

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

        // AAA Pattern Solution
        void testAddition() {
            // Arrange
            int a = 5;
            int b = 3;
            // Act
            int result = calc.add(a, b);
            // Assert
            assert result == 8 : "Expected 8 but got " + result;
            System.out.println("testAddition PASSED");
        }

        void testSubtraction() {
            // Arrange
            int a = 10;
            int b = 4;
            // Act
            int result = calc.subtract(a, b);
            // Assert
            assert result == 6 : "Expected 6 but got " + result;
            System.out.println("testSubtraction PASSED");
        }

        void testMultiplication() {
            // Arrange
            int a = 3;
            int b = 7;
            // Act
            int result = calc.multiply(a, b);
            // Assert
            assert result == 21 : "Expected 21 but got " + result;
            System.out.println("testMultiplication PASSED");
        }

        void testDivision() {
            // Arrange
            int a = 20;
            int b = 4;
            // Act
            int result = calc.divide(a, b);
            // Assert
            assert result == 5 : "Expected 5 but got " + result;
            System.out.println("testDivision PASSED");
        }

        void testDivisionByZero() {
            // Arrange
            int a = 10;
            int b = 0;
            // Act & Assert
            try {
                calc.divide(a, b);
                assert false : "Expected ArithmeticException";
            } catch (ArithmeticException e) {
                assert "Division by zero".equals(e.getMessage());
            }
            System.out.println("testDivisionByZero PASSED");
        }
    }

    // ============================================
    // Exercise 2: StringValidator - FIRST Principles
    // ============================================

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

        // FIRST Principles: Fast, Independent, Repeatable, Self-validating, Timely
        void testValidEmail() {
            // Arrange & Act & Assert
            assert validator.isValidEmail("test@example.com") : "Valid email should pass";
            System.out.println("testValidEmail PASSED");
        }

        void testInvalidEmail() {
            assert !validator.isValidEmail("invalid") : "Invalid email should fail";
            assert !validator.isValidEmail(null) : "Null email should fail";
            assert !validator.isValidEmail("") : "Empty email should fail";
            System.out.println("testInvalidEmail PASSED");
        }

        void testValidPassword() {
            assert validator.isValidPassword("Password123") : "Valid password should pass";
            assert validator.isValidPassword("MyP@ssw0rd") : "Complex password should pass";
            System.out.println("testValidPassword PASSED");
        }

        void testInvalidPassword() {
            assert !validator.isValidPassword("short") : "Short password should fail";
            assert !validator.isValidPassword("alllowercase1") : "No uppercase should fail";
            assert !validator.isValidPassword("ALLUPPERCASE1") : "No lowercase should fail";
            assert !validator.isValidPassword("NoNumbers") : "No digits should fail";
            assert !validator.isValidPassword(null) : "Null password should fail";
            System.out.println("testInvalidPassword PASSED");
        }

        void testValidUsername() {
            assert validator.isValidUsername("john_doe") : "Valid username should pass";
            assert validator.isValidUsername("user123") : "Alphanumeric should pass";
            System.out.println("testValidUsername PASSED");
        }

        void testInvalidUsername() {
            assert !validator.isValidUsername("ab") : "Too short should fail";
            assert !validator.isValidUsername("a".repeat(21)) : "Too long should fail";
            assert !validator.isValidUsername("user name") : "Space should fail";
            assert !validator.isValidUsername("user@name") : "Special char should fail";
            assert !validator.isValidUsername(null) : "Null should fail";
            System.out.println("testInvalidUsername PASSED");
        }
    }

    // ============================================
    // Exercise 3: TemperatureConverter
    // ============================================

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

        void testCelsiusToFahrenheit() {
            assert converter.celsiusToFahrenheit(0) == 32 : "0C = 32F";
            assert converter.celsiusToFahrenheit(100) == 212 : "100C = 212F";
            assert converter.celsiusToFahrenheit(-40) == -40 : "-40C = -40F";
            System.out.println("testCelsiusToFahrenheit PASSED");
        }

        void testFahrenheitToCelsius() {
            assert converter.fahrenheitToCelsius(32) == 0 : "32F = 0C";
            assert converter.fahrenheitToCelsius(212) == 100 : "212F = 100C";
            assert converter.fahrenheitToCelsius(-40) == -40 : "-40F = -40C";
            System.out.println("testFahrenheitToCelsius PASSED");
        }

        void testCelsiusToKelvin() {
            assert converter.celsiusToKelvin(0) == 273.15 : "0C = 273.15K";
            assert converter.celsiusToKelvin(-273.15) == 0 : "-273.15C = 0K";
            System.out.println("testCelsiusToKelvin PASSED");
        }

        void testKelvinToCelsius() {
            assert converter.kelvinToCelsius(273.15) == 0 : "273.15K = 0C";
            assert converter.kelvinToCelsius(0) == -273.15 : "0K = -273.15C";
            System.out.println("testKelvinToCelsius PASSED");
        }

        void testAbsoluteZero() {
            try {
                converter.celsiusToKelvin(-300);
                assert false : "Should throw for below absolute zero";
            } catch (IllegalArgumentException e) {
                assert true;
            }
            System.out.println("testAbsoluteZero PASSED");
        }

        void testNegativeKelvin() {
            try {
                converter.kelvinToCelsius(-1);
                assert false : "Should throw for negative Kelvin";
            } catch (IllegalArgumentException e) {
                assert true;
            }
            System.out.println("testNegativeKelvin PASSED");
        }
    }

    // ============================================
    // Exercise 4: FizzBuzz
    // ============================================

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

        void testFizz() {
            assert "Fizz".equals(fizzBuzz.evaluate(3));
            assert "Fizz".equals(fizzBuzz.evaluate(6));
            assert "Fizz".equals(fizzBuzz.evaluate(9));
            System.out.println("testFizz PASSED");
        }

        void testBuzz() {
            assert "Buzz".equals(fizzBuzz.evaluate(5));
            assert "Buzz".equals(fizzBuzz.evaluate(10));
            assert "Buzz".equals(fizzBuzz.evaluate(20));
            System.out.println("testBuzz PASSED");
        }

        void testFizzBuzz() {
            assert "FizzBuzz".equals(fizzBuzz.evaluate(15));
            assert "FizzBuzz".equals(fizzBuzz.evaluate(30));
            System.out.println("testFizzBuzz PASSED");
        }

        void testNumber() {
            assert "1".equals(fizzBuzz.evaluate(1));
            assert "2".equals(fizzBuzz.evaluate(2));
            assert "4".equals(fizzBuzz.evaluate(4));
            assert "7".equals(fizzBuzz.evaluate(7));
            System.out.println("testNumber PASSED");
        }

        void testInvalidInput() {
            try {
                fizzBuzz.evaluate(0);
                assert false : "Should throw for 0";
            } catch (IllegalArgumentException e) {
                assert true;
            }
            try {
                fizzBuzz.evaluate(-1);
                assert false : "Should throw for negative";
            } catch (IllegalArgumentException e) {
                assert true;
            }
            System.out.println("testInvalidInput PASSED");
        }
    }

    // ============================================
    // Exercise 5: ArrayUtils
    // ============================================

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

        void testFindMax() {
            assert utils.findMax(new int[]{1, 5, 3, 9, 2}) == 9;
            assert utils.findMax(new int[]{-1, -5, -3}) == -1;
            assert utils.findMax(new int[]{42}) == 42;
            System.out.println("testFindMax PASSED");
        }

        void testFindMin() {
            assert utils.findMin(new int[]{1, 5, 3, 9, 2}) == 1;
            assert utils.findMin(new int[]{-1, -5, -3}) == -5;
            assert utils.findMin(new int[]{42}) == 42;
            System.out.println("testFindMin PASSED");
        }

        void testAverage() {
            assert utils.average(new int[]{1, 2, 3, 4, 5}) == 3.0;
            assert utils.average(new int[]{10, 20}) == 15.0;
            System.out.println("testAverage PASSED");
        }

        void testReverse() {
            int[] result = utils.reverse(new int[]{1, 2, 3});
            assert result[0] == 3 && result[1] == 2 && result[2] == 1;
            assert utils.reverse(null) == null;
            System.out.println("testReverse PASSED");
        }

        void testEmptyArrayThrowsException() {
            try {
                utils.findMax(new int[]{});
                assert false : "Should throw for empty array";
            } catch (IllegalArgumentException e) {
                assert true;
            }
            System.out.println("testEmptyArrayThrowsException PASSED");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Testing Fundamentals Solutions ===\n");

        System.out.println("--- Calculator Tests (AAA Pattern) ---");
        CalculatorTest test1 = new CalculatorTest();
        test1.testAddition();
        test1.testSubtraction();
        test1.testMultiplication();
        test1.testDivision();
        test1.testDivisionByZero();

        System.out.println("\n--- StringValidator Tests (FIRST Principles) ---");
        StringValidatorTest test2 = new StringValidatorTest();
        test2.testValidEmail();
        test2.testInvalidEmail();
        test2.testValidPassword();
        test2.testInvalidPassword();
        test2.testValidUsername();
        test2.testInvalidUsername();

        System.out.println("\n--- TemperatureConverter Tests ---");
        TemperatureConverterTest test3 = new TemperatureConverterTest();
        test3.testCelsiusToFahrenheit();
        test3.testFahrenheitToCelsius();
        test3.testCelsiusToKelvin();
        test3.testKelvinToCelsius();
        test3.testAbsoluteZero();
        test3.testNegativeKelvin();

        System.out.println("\n--- FizzBuzz Tests ---");
        FizzBuzzTest test4 = new FizzBuzzTest();
        test4.testFizz();
        test4.testBuzz();
        test4.testFizzBuzz();
        test4.testNumber();
        test4.testInvalidInput();

        System.out.println("\n--- ArrayUtils Tests ---");
        ArrayUtilsTest test5 = new ArrayUtilsTest();
        test5.testFindMax();
        test5.testFindMin();
        test5.testAverage();
        test5.testReverse();
        test5.testEmptyArrayThrowsException();

        System.out.println("\n=== All solutions completed ===");
    }
}
