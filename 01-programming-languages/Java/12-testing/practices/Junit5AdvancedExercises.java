package academy.javaengineering.testing.practices;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 5 Advanced Exercises
 * Practice extensions, parameterized tests, nested tests
 */
class Junit5AdvancedExercises {

    // ============================================
    // Exercise 1: Custom Extension
    // ============================================

    /*
     * TODO: Create a RetryExtension that:
     * 1. Retries failed tests up to 3 times
     * 2. Logs retry attempts
     * 3. Only retries on specific exceptions
     */

    // ============================================
    // Exercise 2: Parameterized with Method Source
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

        static int countWords(String input) {
            if (input == null || input.trim().isEmpty()) return 0;
            return input.trim().split("\\s+").length;
        }

        static String truncate(String input, int maxLength) {
            if (input == null) return null;
            if (input.length() <= maxLength) return input;
            return input.substring(0, maxLength) + "...";
        }
    }

    /*
     * TODO: Implement method sources and parameterized tests
     * static Stream<Arguments> reverseTestCases() {
     *     return Stream.of(
     *         Arguments.of("hello", "olleh"),
     *         Arguments.of("", ""),
     *         Arguments.of(null, null)
     *     );
     * }
     */

    // ============================================
    // Exercise 3: Enum Source
    // ============================================

    enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    static class DayService {
        boolean isWeekend(DayOfWeek day) {
            return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
        }

        boolean isWorkday(DayOfWeek day) {
            return !isWeekend(day);
        }
    }

    /*
     * TODO: Implement tests using @EnumSource(DayOfWeek.class)
     */

    // ============================================
    // Exercise 4: Nested Test Organization
    // ============================================

    static class BankAccount {
        private double balance;
        private final String owner;
        private final List<String> transactions = new ArrayList<>();

        BankAccount(String owner, double initialBalance) {
            this.owner = owner;
            this.balance = initialBalance;
            transactions.add("Opened with " + initialBalance);
        }

        void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
            balance += amount;
            transactions.add("Deposited " + amount);
        }

        void withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
            if (amount > balance) throw new IllegalStateException("Insufficient funds");
            balance -= amount;
            transactions.add("Withdrew " + amount);
        }

        double getBalance() { return balance; }
        String getOwner() { return owner; }
        List<String> getTransactions() { return new ArrayList<>(transactions); }
    }

    /*
     * TODO: Write nested tests for BankAccount
     * @Nested class DepositTests
     * @Nested class WithdrawalTests
     */

    // ============================================
    // Exercise 5: Repeated Tests
    // ============================================

    static class RandomNumberGenerator {
        private final java.util.Random random;

        RandomNumberGenerator(long seed) {
            this.random = new java.util.Random(seed);
        }

        int nextInt(int bound) {
            return random.nextInt(bound);
        }

        boolean nextBoolean() {
            return random.nextBoolean();
        }
    }

    /*
     * TODO: Implement repeated tests
     * @RepeatedTest(value = 10, name = "Random in range {currentRepetition}")
     */

    public static void main(String[] args) {
        System.out.println("=== JUnit 5 Advanced Exercises ===");
        System.out.println("Implement parameterized, nested, and repeated tests.");
    }
}
