package academy.javaengineering.testing.examples;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Unit Testing Best Practices and Patterns
 */
class UnitTestingDemo {

    // ============================================
    // Pattern 1: Given-When-Then
    // ============================================

    static class Calculator {
        private final List<String> history = new ArrayList<>();

        int add(int a, int b) {
            int result = a + b;
            history.add(a + " + " + b + " = " + result);
            return result;
        }

        int subtract(int a, int b) {
            int result = a - b;
            history.add(a + " - " + b + " = " + result);
            return result;
        }

        List<String> getHistory() {
            return Collections.unmodifiableList(history);
        }

        void clearHistory() {
            history.clear();
        }
    }

    static class CalculatorTestGivenWhenThen {
        private Calculator calculator;

        @org.junit.jupiter.api.BeforeEach
        void setUp() {
            calculator = new Calculator();
        }

        // Given-When-Then pattern
        @org.junit.jupiter.api.Test
        void testAddition() {
            // Given
            int a = 5;
            int b = 3;

            // When
            int result = calculator.add(a, b);

            // Then
            assert result == 8;
        }

        @org.junit.jupiter.api.Test
        void testHistory() {
            // Given
            calculator.add(1, 2);
            calculator.subtract(5, 3);

            // When
            List<String> history = calculator.getHistory();

            // Then
            assert history.size() == 2;
            assert history.get(0).contains("+");
            assert history.get(1).contains("-");
        }
    }

    // ============================================
    // Pattern 2: Arrange-Act-Assert with Setup
    // ============================================

    static class StringUtils {
        static String reverse(String input) {
            if (input == null) return null;
            return new StringBuilder(input).reverse().toString();
        }

        static boolean isPalindrome(String input) {
            if (input == null) return false;
            String cleaned = input.toLowerCase().replaceAll("[^a-z0-9]", "");
            return cleaned.equals(reverse(cleaned));
        }

        static String truncate(String input, int maxLength) {
            if (input == null) return null;
            if (input.length() <= maxLength) return input;
            return input.substring(0, maxLength) + "...";
        }

        static String repeat(String input, int times) {
            if (input == null || times <= 0) return "";
            return input.repeat(times);
        }

        static String capitalize(String input) {
            if (input == null || input.isEmpty()) return input;
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
    }

    static class StringUtilsTest {
        // Arrange-Act-Assert with clear separation

        @org.junit.jupiter.api.Test
        void testReverse() {
            // Arrange
            String input = "hello";

            // Act
            String result = StringUtils.reverse(input);

            // Assert
            assert "olleh".equals(result);
        }

        @org.junit.jupiter.api.Test
        void testTruncate() {
            // Arrange
            String longString = "This is a very long string";

            // Act
            String result = StringUtils.truncate(longString, 10);

            // Assert
            assert "This is a ...".equals(result);
            assert result.length() <= 13; // 10 + "..."
        }

        @org.junit.jupiter.api.Test
        void testCapitalize() {
            // Arrange & Act & Assert - Simple cases
            assert "Hello".equals(StringUtils.capitalize("hello"));
            assert "HELLO".equals(StringUtils.capitalize("HELLO"));
            assert null == StringUtils.capitalize(null);
            assert "".equals(StringUtils.capitalize(""));
        }
    }

    // ============================================
    // Pattern 3: Test Data Builder
    // ============================================

    static class User {
        private final String name;
        private final String email;
        private final int age;
        private final boolean active;

        User(String name, String email, int age, boolean active) {
            this.name = name;
            this.email = email;
            this.age = age;
            this.active = active;
        }

        String getName() { return name; }
        String getEmail() { return email; }
        int getAge() { return age; }
        boolean isActive() { return active; }

        // Builder pattern for tests
        static UserBuilder builder() {
            return new UserBuilder();
        }

        static class UserBuilder {
            private String name = "Test User";
            private String email = "test@example.com";
            private int age = 25;
            private boolean active = true;

            UserBuilder name(String name) { this.name = name; return this; }
            UserBuilder email(String email) { this.email = email; return this; }
            UserBuilder age(int age) { this.age = age; return this; }
            UserBuilder active(boolean active) { this.active = active; return this; }

            User build() {
                return new User(name, email, age, active);
            }
        }
    }

    static class UserService {
        private final Map<String, User> users = new HashMap<>();

        void registerUser(User user) {
            if (users.containsKey(user.getEmail())) {
                throw new IllegalArgumentException("Email already registered");
            }
            users.put(user.getEmail(), user);
        }

        Optional<User> findByEmail(String email) {
            return Optional.ofNullable(users.get(email));
        }

        List<User> findActiveUsers() {
            return users.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
        }
    }

    static class UserServiceTestWithBuilder {
        private UserService service;

        @org.junit.jupiter.api.BeforeEach
        void setUp() {
            service = new UserService();
        }

        @org.junit.jupiter.api.Test
        void testRegisterUser() {
            // Arrange - Using builder
            User user = User.builder()
                .name("John")
                .email("john@example.com")
                .age(30)
                .active(true)
                .build();

            // Act
            service.registerUser(user);

            // Assert
            assert service.findByEmail("john@example.com").isPresent();
            assert "John".equals(service.findByEmail("john@example.com").get().getName());
        }

        @org.junit.jupiter.api.Test
        void testDuplicateEmailThrowsException() {
            // Arrange
            User user1 = User.builder().email("test@example.com").build();
            User user2 = User.builder().email("test@example.com").build();
            service.registerUser(user1);

            // Act & Assert
            try {
                service.registerUser(user2);
                assert false : "Should have thrown exception";
            } catch (IllegalArgumentException e) {
                assert "Email already registered".equals(e.getMessage());
            }
        }

        @org.junit.jupiter.api.Test
        void testFindActiveUsers() {
            // Arrange
            service.registerUser(User.builder().name("Active").active(true).build());
            service.registerUser(User.builder().name("Inactive").active(false).build());

            // Act
            List<User> activeUsers = service.findActiveUsers();

            // Assert
            assert activeUsers.size() == 1;
            assert "Active".equals(activeUsers.get(0).getName());
        }
    }

    // ============================================
    // Pattern 4: Exception Testing
    // ============================================

    static class BankAccount {
        private double balance;

        BankAccount(double initialBalance) {
            if (initialBalance < 0) throw new IllegalArgumentException("Initial balance cannot be negative");
            this.balance = initialBalance;
        }

        void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
            balance += amount;
        }

        void withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
            if (amount > balance) throw new IllegalStateException("Insufficient funds");
            balance -= amount;
        }

        double getBalance() { return balance; }
    }

    static class BankAccountTest {
        @org.junit.jupiter.api.Test
        void testNegativeInitialBalance() {
            try {
                new BankAccount(-100);
                assert false : "Should throw IllegalArgumentException";
            } catch (IllegalArgumentException e) {
                assert e.getMessage().contains("cannot be negative");
            }
        }

        @org.junit.jupiter.api.Test
        void testInsufficientFunds() {
            BankAccount account = new BankAccount(100);
            try {
                account.withdraw(200);
                assert false : "Should throw IllegalStateException";
            } catch (IllegalStateException e) {
                assert e.getMessage().contains("Insufficient funds");
            }
        }

        @org.junit.jupiter.api.Test
        void testZeroDeposit() {
            BankAccount account = new BankAccount(100);
            try {
                account.deposit(0);
                assert false : "Should throw IllegalArgumentException";
            } catch (IllegalArgumentException e) {
                assert e.getMessage().contains("must be positive");
            }
        }
    }

    // ============================================
    // Pattern 5: Test Coverage - Edge Cases
    // ============================================

    static class NumberUtils {
        static int factorial(int n) {
            if (n < 0) throw new IllegalArgumentException("Negative input not allowed");
            if (n == 0 || n == 1) return 1;
            int result = 1;
            for (int i = 2; i <= n; i++) {
                result *= i;
            }
            return result;
        }

        static boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;
            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return false;
            }
            return true;
        }

        static int fibonacci(int n) {
            if (n <= 0) throw new IllegalArgumentException("Input must be positive");
            if (n == 1) return 0;
            if (n == 2) return 1;
            int a = 0, b = 1;
            for (int i = 3; i <= n; i++) {
                int temp = a + b;
                a = b;
                b = temp;
            }
            return b;
        }
    }

    static class NumberUtilsTest {
        // Test normal cases
        @org.junit.jupiter.api.Test
        void testFactorialNormal() {
            assert NumberUtils.factorial(5) == 120;
            assert NumberUtils.factorial(10) == 3628800;
        }

        // Test edge cases
        @org.junit.jupiter.api.Test
        void testFactorialEdgeCases() {
            assert NumberUtils.factorial(0) == 1;
            assert NumberUtils.factorial(1) == 1;
        }

        // Test boundary
        @org.junit.jupiter.api.Test
        void testFactorialBoundary() {
            assert NumberUtils.factorial(2) == 2;
        }

        // Test exception
        @org.junit.jupiter.api.Test
        void testFactorialNegative() {
            try {
                NumberUtils.factorial(-1);
                assert false : "Should throw";
            } catch (IllegalArgumentException e) {
                assert true;
            }
        }

        // Test primes
        @org.junit.jupiter.api.Test
        void testIsPrime() {
            assert !NumberUtils.isPrime(1);
            assert NumberUtils.isPrime(2);
            assert NumberUtils.isPrime(3);
            assert !NumberUtils.isPrime(4);
            assert NumberUtils.isPrime(5);
            assert NumberUtils.isPrime(7);
            assert !NumberUtils.isPrime(9);
            assert NumberUtils.isPrime(11);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Unit Testing Patterns Demo ===\n");

        System.out.println("--- Given-When-Then Pattern ---");
        CalculatorTestGivenWhenThen test1 = new CalculatorTestGivenWhenThen();
        test1.setUp();
        test1.testAddition();
        test1.testHistory();
        System.out.println("All Given-When-Then tests PASSED\n");

        System.out.println("--- Arrange-Act-Assert Pattern ---");
        StringUtilsTest test2 = new StringUtilsTest();
        test2.testReverse();
        test2.testTruncate();
        test2.testCapitalize();
        System.out.println("All Arrange-Act-Assert tests PASSED\n");

        System.out.println("--- Test Data Builder Pattern ---");
        UserServiceTestWithBuilder test3 = new UserServiceTestWithBuilder();
        test3.setUp();
        test3.testRegisterUser();
        test3.testDuplicateEmailThrowsException();
        test3.testFindActiveUsers();
        System.out.println("All Builder tests PASSED\n");

        System.out.println("--- Exception Testing Pattern ---");
        BankAccountTest test4 = new BankAccountTest();
        test4.testNegativeInitialBalance();
        test4.testInsufficientFunds();
        test4.testZeroDeposit();
        System.out.println("All Exception tests PASSED\n");

        System.out.println("--- Edge Case Testing ---");
        NumberUtilsTest test5 = new NumberUtilsTest();
        test5.testFactorialNormal();
        test5.testFactorialEdgeCases();
        test5.testFactorialBoundary();
        test5.testFactorialNegative();
        test5.testIsPrime();
        System.out.println("All Edge Case tests PASSED\n");

        System.out.println("=== All unit testing pattern tests passed ===");
    }
}
