package academy.javaengineering.testing.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing Fundamentals Demo - AAA Pattern & FIRST Principles
 * 
 * AAA Pattern: Arrange -> Act -> Assert
 * FIRST Principles: Fast, Independent, Repeatable, Self-validating, Timely
 */
public class TestingFundamentalsDemo {

    // System under test
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

        boolean isEven(int number) {
            return number % 2 == 0;
        }
    }

    // AAA Pattern Demo
    static class CalculatorTest {

        private final Calculator calculator = new Calculator();

        // Arrange - Act - Assert
        void testAdd() {
            // Arrange
            int a = 2;
            int b = 3;

            // Act
            int result = calculator.add(a, b);

            // Assert
            assert result == 5 : "Expected 5 but got " + result;
            System.out.println("testAdd PASSED");
        }

        void testSubtract() {
            // Arrange
            int a = 10;
            int b = 4;

            // Act
            int result = calculator.subtract(a, b);

            // Assert
            assert result == 6 : "Expected 6 but got " + result;
            System.out.println("testSubtract PASSED");
        }

        void testDivideByZero() {
            // Arrange
            int a = 10;
            int b = 0;

            // Act & Assert
            try {
                calculator.divide(a, b);
                assert false : "Expected ArithmeticException";
            } catch (ArithmeticException e) {
                assert "Division by zero".equals(e.getMessage());
            }
            System.out.println("testDivideByZero PASSED");
        }

        void runAll() {
            testAdd();
            testSubtract();
            testDivideByZero();
        }
    }

    // FIRST Principles in action
    static class StringProcessor {

        String reverse(String input) {
            if (input == null) return null;
            return new StringBuilder(input).reverse().toString();
        }

        boolean isPalindrome(String input) {
            if (input == null) return false;
            String cleaned = input.toLowerCase().replaceAll("[^a-z0-9]", "");
            return cleaned.equals(reverse(cleaned));
        }

        int countVowels(String input) {
            if (input == null) return 0;
            int count = 0;
            for (char c : input.toLowerCase().toCharArray()) {
                if ("aeiou".indexOf(c) >= 0) count++;
            }
            return count;
        }

        String capitalize(String input) {
            if (input == null || input.isEmpty()) return input;
            return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        }
    }

    static class StringProcessorTest {

        private final StringProcessor processor = new StringProcessor();

        // FAST - executes in microseconds
        void testReverse() {
            String input = "hello";
            String result = processor.reverse(input);
            assert "olleh".equals(result) : "Expected 'olleh' but got '" + result + "'";
            System.out.println("testReverse PASSED");
        }

        // INDEPENDENT - no shared state between tests
        void testIsPalindrome() {
            assert processor.isPalindrome("racecar") : "racecar should be palindrome";
            assert !processor.isPalindrome("hello") : "hello should not be palindrome";
            System.out.println("testIsPalindrome PASSED");
        }

        // REPEATABLE - produces same result every time
        void testCountVowels() {
            assert processor.countVowels("hello") == 2 : "Expected 2 vowels";
            assert processor.countVowels("xyz") == 0 : "Expected 0 vowels";
            assert processor.countVowels("aeiou") == 5 : "Expected 5 vowels";
            System.out.println("testCountVowels PASSED");
        }

        // SELF-VALIDATING - no manual inspection needed
        void testCapitalize() {
            assert "Hello".equals(processor.capitalize("hello"));
            assert "Hello".equals(processor.capitalize("HELLO"));
            assert null == processor.capitalize(null);
            System.out.println("testCapitalize PASSED");
        }

        // TIMELY - tests written before or with production code
        void testEdgeCases() {
            assert "".equals(processor.reverse(""));
            assert null == processor.reverse(null);
            assert !processor.isPalindrome(null);
            assert processor.countVowels(null) == 0;
            System.out.println("testEdgeCases PASSED");
        }

        void runAll() {
            testReverse();
            testIsPalindrome();
            testCountVowels();
            testCapitalize();
            testEdgeCases();
        }
    }

    // Test isolation demonstration
    static class UserRepository {
        private final List<String> users = new ArrayList<>();

        void addUser(String user) {
            users.add(user);
        }

        List<String> getUsers() {
            return new ArrayList<>(users); // Defensive copy for isolation
        }

        boolean containsUser(String user) {
            return users.contains(user);
        }

        void clear() {
            users.clear();
        }
    }

    static class UserRepositoryTest {

        // Each test method gets fresh state - INDEPENDENT
        void testAddUser() {
            UserRepository repo = new UserRepository();
            repo.addUser("Alice");
            assert repo.containsUser("Alice") : "Should contain Alice";
            System.out.println("testAddUser PASSED");
        }

        void testGetUsersReturnsDefensiveCopy() {
            UserRepository repo = new UserRepository();
            repo.addUser("Bob");
            List<String> users = repo.getUsers();
            users.add("Charlie"); // Modify the copy
            assert !repo.containsUser("Charlie") : "Original should not be affected";
            System.out.println("testGetUsersReturnsDefensiveCopy PASSED");
        }

        void testClear() {
            UserRepository repo = new UserRepository();
            repo.addUser("Dave");
            repo.clear();
            assert repo.getUsers().isEmpty() : "Should be empty after clear";
            System.out.println("testClear PASSED");
        }

        void runAll() {
            testAddUser();
            testGetUsersReturnsDefensiveCopy();
            testClear();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Testing Fundamentals Demo ===\n");

        System.out.println("--- Calculator Tests (AAA Pattern) ---");
        new CalculatorTest().runAll();

        System.out.println("\n--- String Processor Tests (FIRST Principles) ---");
        new StringProcessorTest().runAll();

        System.out.println("\n--- User Repository Tests (Test Isolation) ---");
        new UserRepositoryTest().runAll();

        System.out.println("\n=== All tests completed ===");
    }
}
