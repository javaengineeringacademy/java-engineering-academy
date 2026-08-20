package academy.javaengineering.testing.solutions;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * JUnit 5 Advanced Solutions
 * Complete solutions for extensions, parameterized, nested tests
 */
class Junit5AdvancedSolutions {

    // ============================================
    // Exercise 1: Custom Extension
    // ============================================

    /*
     * RetryExtension Solution:
     * 
     * public class RetryExtension implements TestExecutionExceptionHandler {
     *     @Override
     *     public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
     *         Retry retry = context.getRequiredTestMethod().getAnnotation(Retry.class);
     *         if (retry == null) throw throwable;
     *         
     *         for (int i = 1; i < retry.maxAttempts(); i++) {
     *             try {
     *                 System.out.println("Retry attempt " + i);
     *                 return;
     *             } catch (Throwable t) {
     *                 // Continue
     *             }
     *         }
     *         throw throwable;
     *     }
     * }
     * 
     * @Retention(RetentionPolicy.RUNTIME)
     * @Target(ElementType.METHOD)
     * @ExtendWith(RetryExtension.class)
     * @interface Retry {
     *     int maxAttempts() default 3;
     * }
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
     * Method Source Solution:
     * 
     * static Stream<Arguments> reverseTestCases() {
     *     return Stream.of(
     *         Arguments.of("hello", "olleh"),
     *         Arguments.of("world", "dlrow"),
     *         Arguments.of("", ""),
     *         Arguments.of("a", "a"),
     *         Arguments.of(null, null)
     *     );
     * }
     * 
     * @ParameterizedTest
     * @MethodSource("reverseTestCases")
     * void testReverse(String input, String expected) {
     *     assertEquals(expected, StringUtils.reverse(input));
     * }
     * 
     * static Stream<Arguments> palindromeTestCases() {
     *     return Stream.of(
     *         Arguments.of("racecar", true),
     *         Arguments.of("hello", false),
     *         Arguments.of("A man a plan a canal Panama", false),
     *         Arguments.of("", true),
     *         Arguments.of(null, false)
     *     );
     * }
     * 
     * @ParameterizedTest
     * @MethodSource("palindromeTestCases")
     * void testIsPalindrome(String input, boolean expected) {
     *     assertEquals(expected, StringUtils.isPalindrome(input));
     * }
     * 
     * static Stream<Arguments> truncateTestCases() {
     *     return Stream.of(
     *         Arguments.of("Hello World", 5, "Hello..."),
     *         Arguments.of("Hi", 10, "Hi"),
     *         Arguments.of(null, 5, null),
     *         Arguments.of("", 5, "")
     *     );
     * }
     * 
     * @ParameterizedTest
     * @MethodSource("truncateTestCases")
     * void testTruncate(String input, int maxLength, String expected) {
     *     assertEquals(expected, StringUtils.truncate(input, maxLength));
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
     * Enum Source Solution:
     * 
     * DayService dayService = new DayService();
     * 
     * @ParameterizedTest
     * @EnumSource(DayOfWeek.class)
     * void testIsWeekendOrWorkday(DayOfWeek day) {
     *     if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
     *         assertTrue(dayService.isWeekend(day));
     *         assertFalse(dayService.isWorkday(day));
     *     } else {
     *         assertFalse(dayService.isWeekend(day));
     *         assertTrue(dayService.isWorkday(day));
     *     }
     * }
     * 
     * @ParameterizedTest
     * @EnumSource(value = DayOfWeek.class, names = {"SATURDAY", "SUNDAY"})
     * void testWeekendDays(DayOfWeek day) {
     *     assertTrue(dayService.isWeekend(day));
     * }
     * 
     * @ParameterizedTest
     * @EnumSource(value = DayOfWeek.class, mode = EnumSource.Mode.EXCLUDE, names = {"SATURDAY", "SUNDAY"})
     * void testWeekdays(DayOfWeek day) {
     *     assertTrue(dayService.isWorkday(day));
     * }
     */

    // ============================================
    // Exercise 4: Nested Test Organization
    // ============================================

    static class BankAccount {
        private double balance;
        private final String owner;
        private final java.util.List<String> transactions = new java.util.ArrayList<>();

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
        java.util.List<String> getTransactions() { return new java.util.ArrayList<>(transactions); }
    }

    /*
     * Nested Test Solution:
     * 
     * @Nested
     * class DepositTests {
     *     BankAccount account;
     * 
     *     @BeforeEach
     *     void setUp() {
     *         account = new BankAccount("John", 1000);
     *     }
     * 
     *     @Test
     *     void testDepositIncreasesBalance() {
     *         account.deposit(500);
     *         assertEquals(1500, account.getBalance());
     *     }
     * 
     *     @Test
     *     void testMultipleDeposits() {
     *         account.deposit(100);
     *         account.deposit(200);
     *         assertEquals(1300, account.getBalance());
     *     }
     * 
     *     @Test
     *     void testInvalidDepositThrows() {
     *         assertThrows(IllegalArgumentException.class, () -> {
     *             account.deposit(-100);
     *         });
     *     }
     * }
     * 
     * @Nested
     * class WithdrawalTests {
     *     BankAccount account;
     * 
     *     @BeforeEach
     *     void setUp() {
     *         account = new BankAccount("John", 1000);
     *     }
     * 
     *     @Test
     *     void testWithdrawDecreasesBalance() {
     *         account.withdraw(500);
     *         assertEquals(500, account.getBalance());
     *     }
     * 
     *     @Test
     *     void testInsufficientFundsThrows() {
     *         assertThrows(IllegalStateException.class, () -> {
     *             account.withdraw(2000);
     *         });
     *     }
     * 
     *     @Test
     *     void testInvalidWithdrawalThrows() {
     *         assertThrows(IllegalArgumentException.class, () -> {
     *             account.withdraw(-100);
     *         });
     *     }
     * }
     * 
     * @Nested
     * class TransactionTests {
     *     @Test
     *     void testTransactionsAreRecorded() {
     *         BankAccount account = new BankAccount("John", 1000);
     *         account.deposit(500);
     *         account.withdraw(200);
     *         assertEquals(3, account.getTransactions().size());
     *     }
     * }
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
     * Repeated Test Solution:
     * 
     * @RepeatedTest(value = 10, name = "Random in range {currentRepetition} of {totalRepetitions}")
     * void testRandomInBounds() {
     *     RandomNumberGenerator gen = new RandomNumberGenerator(System.nanoTime());
     *     int value = gen.nextInt(100);
     *     assertTrue(value >= 0 && value < 100, "Value " + value + " out of range");
     * }
     * 
     * @RepeatedTest(5)
     * void testRandomBoolean() {
     *     RandomNumberGenerator gen = new RandomNumberGenerator(System.nanoTime());
     *     boolean result = gen.nextBoolean();
     *     assertTrue(result || !result); // Always passes, just verifies no exception
     * }
     * 
     * @RepeatedTest(value = 3, name = "Retry test {currentRepetition}")
     * @Retry(maxAttempts = 3)
     * void testFlakyOperation() {
     *     // Simulates a flaky test that might fail occasionally
     *     RandomNumberGenerator gen = new RandomNumberGenerator(System.nanoTime());
     *     int value = gen.nextInt(10);
     *     assertTrue(value >= 0);
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== JUnit 5 Advanced Solutions ===\n");
        System.out.println("These are reference solutions for advanced JUnit 5 exercises.");
        System.out.println("Implement as JUnit 5 test classes with proper annotations.\n");

        System.out.println("--- StringUtils Solutions ---");
        System.out.println("MethodSource with Arguments.of() for parameterized tests");
        System.out.println("Example: Arguments.of(\"hello\", \"olleh\")\n");

        System.out.println("--- DayService Solutions ---");
        System.out.println("@EnumSource(DayOfWeek.class) for enum parameterization");
        System.out.println("@EnumSource with names/exclude for filtering\n");

        System.out.println("--- BankAccount Solutions ---");
        System.out.println("@Nested classes for organized test structure");
        System.out.println("@BeforeEach for common setup in each nested class\n");

        System.out.println("--- RandomNumberGenerator Solutions ---");
        System.out.println("@RepeatedTest for testing non-deterministic behavior");

        System.out.println("\n=== All solutions completed ===");
    }
}
