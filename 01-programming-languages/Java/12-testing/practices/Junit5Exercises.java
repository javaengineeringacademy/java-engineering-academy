package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * JUnit 5 Exercises
 * Practice using JUnit 5 annotations, assertions, and lifecycle
 */
class Junit5Exercises {

    // ============================================
    // Exercise 1: Basic Assertions
    // ============================================
    // TODO: Complete tests using JUnit 5 assertions

    static class MathUtils {
        static int abs(int number) {
            return number < 0 ? -number : number;
        }

        static int max(int a, int b) {
            return a > b ? a : b;
        }

        static int min(int a, int b) {
            return a < b ? a : b;
        }

        static boolean isPowerOfTwo(int number) {
            if (number <= 0) return false;
            return (number & (number - 1)) == 0;
        }
    }

    /*
     * TODO: Write JUnit 5 tests for MathUtils
     * 
     * @Test
     * void testAbs() {
     *     assertEquals(5, MathUtils.abs(-5));
     *     assertEquals(5, MathUtils.abs(5));
     *     assertEquals(0, MathUtils.abs(0));
     * }
     */

    // ============================================
    // Exercise 2: Exception Testing
    // ============================================
    // TODO: Test that exceptions are thrown correctly

    static class AgeValidator {
        static void validate(int age) {
            if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
            if (age > 150) throw new IllegalArgumentException("Age seems unrealistic");
            if (age < 18) throw new IllegalArgumentException("Must be at least 18");
        }
    }

    /*
     * TODO: Write tests that verify exceptions
     * 
     * @Test
     * void testNegativeAgeThrowsException() {
     *     assertThrows(IllegalArgumentException.class, () -> {
     *         AgeValidator.validate(-1);
     *     });
     * }
     */

    // ============================================
    // Exercise 3: Nested Tests
    // ============================================
    // TODO: Organize tests using @Nested

    static class ShoppingCart {
        private final List<String> items = new ArrayList<>();
        private double discount = 0;

        void addItem(String item) { items.add(item); }
        void removeItem(String item) { items.remove(item); }
        void setDiscount(double discount) {
            if (discount < 0 || discount > 100) throw new IllegalArgumentException("Invalid discount");
            this.discount = discount;
        }
        List<String> getItems() { return new ArrayList<>(items); }
        int getItemCount() { return items.size(); }
        double getDiscount() { return discount; }
    }

    /*
     * TODO: Write nested tests
     * 
     * @Nested
     * class WhenCartIsEmpty {
     *     @Test
     *     void shouldHaveZeroItems() {
     *         // ...
     *     }
     * }
     * 
     * @Nested
     * class AfterAddingItem {
     *     @BeforeEach
     *     void setUp() { /* add item */ }
     *     
     *     @Test
     *     void shouldHaveOneItem() {
     *         // ...
     *     }
     * }
     */

    // ============================================
    // Exercise 4: Parameterized Tests
    // ============================================
    // TODO: Write parameterized tests

    static class NumberUtils {
        static boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;
            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return false;
            }
            return true;
        }

        static int factorial(int n) {
            if (n < 0) throw new IllegalArgumentException("Negative input");
            if (n == 0 || n == 1) return 1;
            int result = 1;
            for (int i = 2; i <= n; i++) result *= i;
            return result;
        }
    }

    /*
     * TODO: Write parameterized tests
     * 
     * @ParameterizedTest
     * @ValueSource(ints = {2, 3, 5, 7, 11})
     * void testIsPrime(int number) {
     *     assertTrue(NumberUtils.isPrime(number));
     * }
     * 
     * @ParameterizedTest
     * @CsvSource({"0,1", "1,1", "5,120"})
     * void testFactorial(int input, int expected) {
     *     assertEquals(expected, NumberUtils.factorial(input));
     * }
     */

    // ============================================
    // Exercise 5: Test Lifecycle
    // ============================================
    // TODO: Use lifecycle annotations properly

    static class UserService {
        private final Map<String, String> users = new HashMap<>();

        void addUser(String id, String name) {
            users.put(id, name);
        }

        String getUser(String id) {
            return users.get(id);
        }

        void removeUser(String id) {
            users.remove(id);
        }

        int getUserCount() {
            return users.size();
        }
    }

    /*
     * TODO: Write tests using lifecycle
     * 
     * @BeforeEach
     * void setUp() {
     *     // Initialize fresh UserService
     * }
     * 
     * @AfterEach
     * void tearDown() {
     *     // Clean up if needed
     * }
     * 
     * @Test
     * void testAddUser() {
     *     // ...
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== JUnit 5 Exercises ===");
        System.out.println("Implement tests using JUnit 5 features.");
        System.out.println("Refer to Junit5Demo.java for examples.");
    }
}
