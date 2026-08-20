package academy.javaengineering.testing.solutions;

import java.util.*;

/**
 * JUnit 5 Solutions
 * Complete solutions for JUnit 5 exercises
 */
class Junit5Solutions {

    // ============================================
    // Exercise 1: Basic Assertions
    // ============================================

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
     * JUnit 5 Test Solutions:
     * 
     * @Test
     * void testAbs() {
     *     assertEquals(5, MathUtils.abs(-5));
     *     assertEquals(5, MathUtils.abs(5));
     *     assertEquals(0, MathUtils.abs(0));
     * }
     * 
     * @Test
     * void testMax() {
     *     assertEquals(10, MathUtils.max(10, 5));
     *     assertEquals(10, MathUtils.max(5, 10));
     *     assertEquals(5, MathUtils.max(5, 5));
     * }
     * 
     * @Test
     * void testMin() {
     *     assertEquals(5, MathUtils.min(10, 5));
     *     assertEquals(5, MathUtils.min(5, 10));
     *     assertEquals(5, MathUtils.min(5, 5));
     * }
     * 
     * @Test
     * void testIsPowerOfTwo() {
     *     assertTrue(MathUtils.isPowerOfTwo(1));
     *     assertTrue(MathUtils.isPowerOfTwo(2));
     *     assertTrue(MathUtils.isPowerOfTwo(4));
     *     assertTrue(MathUtils.isPowerOfTwo(8));
     *     assertFalse(MathUtils.isPowerOfTwo(3));
     *     assertFalse(MathUtils.isPowerOfTwo(0));
     *     assertFalse(MathUtils.isPowerOfTwo(-1));
     * }
     */

    // ============================================
    // Exercise 2: Exception Testing
    // ============================================

    static class AgeValidator {
        static void validate(int age) {
            if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
            if (age > 150) throw new IllegalArgumentException("Age seems unrealistic");
            if (age < 18) throw new IllegalArgumentException("Must be at least 18");
        }
    }

    /*
     * JUnit 5 Exception Test Solutions:
     * 
     * @Test
     * void testNegativeAgeThrowsException() {
     *     assertThrows(IllegalArgumentException.class, () -> {
     *         AgeValidator.validate(-1);
     *     });
     * }
     * 
     * @Test
     * void testUnrealisticAgeThrowsException() {
     *     assertThrows(IllegalArgumentException.class, () -> {
     *         AgeValidator.validate(200);
     *     });
     * }
     * 
     * @Test
     * void testUnderageThrowsException() {
     *     IllegalArgumentException exception = assertThrows(
     *         IllegalArgumentException.class,
     *         () -> AgeValidator.validate(15)
     *     );
     *     assertEquals("Must be at least 18", exception.getMessage());
     * }
     * 
     * @Test
     * void testValidAgeDoesNotThrow() {
     *     assertDoesNotThrow(() -> AgeValidator.validate(25));
     * }
     */

    // ============================================
    // Exercise 3: Nested Tests
    // ============================================

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
     * JUnit 5 Nested Test Solutions:
     * 
     * @Nested
     * class WhenCartIsEmpty {
     *     ShoppingCart cart;
     * 
     *     @BeforeEach
     *     void setUp() {
     *         cart = new ShoppingCart();
     *     }
     * 
     *     @Test
     *     void shouldHaveZeroItems() {
     *         assertEquals(0, cart.getItemCount());
     *     }
     * 
     *     @Test
     *     void shouldReturnEmptyList() {
     *         assertTrue(cart.getItems().isEmpty());
     *     }
     * }
     * 
     * @Nested
     * class AfterAddingItem {
     *     ShoppingCart cart;
     * 
     *     @BeforeEach
     *     void setUp() {
     *         cart = new ShoppingCart();
     *         cart.addItem("Laptop");
     *     }
     * 
     *     @Test
     *     void shouldHaveOneItem() {
     *         assertEquals(1, cart.getItemCount());
     *     }
     * 
     *     @Test
     *     void shouldContainLaptop() {
     *         assertTrue(cart.getItems().contains("Laptop"));
     *     }
     * }
     * 
     * @Nested
     * class DiscountTests {
     *     ShoppingCart cart;
     * 
     *     @BeforeEach
     *     void setUp() {
     *         cart = new ShoppingCart();
     *     }
     * 
     *     @Test
     *     void testValidDiscount() {
     *         cart.setDiscount(10);
     *         assertEquals(10, cart.getDiscount());
     *     }
     * 
     *     @Test
     *     void testInvalidDiscountThrows() {
     *         assertThrows(IllegalArgumentException.class, () -> {
     *             cart.setDiscount(150);
     *         });
     *     }
     * }
     */

    // ============================================
    // Exercise 4: Parameterized Tests
    // ============================================

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
     * JUnit 5 Parameterized Test Solutions:
     * 
     * @ParameterizedTest
     * @ValueSource(ints = {2, 3, 5, 7, 11})
     * void testIsPrime(int number) {
     *     assertTrue(NumberUtils.isPrime(number));
     * }
     * 
     * @ParameterizedTest
     * @ValueSource(ints = {4, 6, 8, 9, 10})
     * void testIsNotPrime(int number) {
     *     assertFalse(NumberUtils.isPrime(number));
     * }
     * 
     * @ParameterizedTest
     * @CsvSource({
     *         "0, 1",
     *         "1, 1",
     *         "5, 120",
     *         "10, 3628800"
     * })
     * void testFactorial(int input, int expected) {
     *     assertEquals(expected, NumberUtils.factorial(input));
     * }
     * 
     * @ParameterizedTest
     * @MethodSource("primeTestCases")
     * void testIsPrimeWithMethodSource(int number, boolean expected) {
     *     assertEquals(expected, NumberUtils.isPrime(number));
     * }
     * 
     * static Stream<Arguments> primeTestCases() {
     *     return Stream.of(
     *         Arguments.of(1, false),
     *         Arguments.of(2, true),
     *         Arguments.of(3, true),
     *         Arguments.of(4, false),
     *         Arguments.of(5, true),
     *         Arguments.of(100, false)
     *     );
     * }
     */

    // ============================================
    // Exercise 5: Test Lifecycle
    // ============================================

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
     * JUnit 5 Lifecycle Test Solutions:
     * 
     * UserService service;
     * 
     * @BeforeEach
     * void setUp() {
     *     service = new UserService();
     *     service.addUser("1", "Alice");
     *     service.addUser("2", "Bob");
     * }
     * 
     * @AfterEach
     * void tearDown() {
     *     service = null;
     * }
     * 
     * @Test
     * void testAddUser() {
     *     service.addUser("3", "Charlie");
     *     assertEquals(3, service.getUserCount());
     *     assertEquals("Charlie", service.getUser("3"));
     * }
     * 
     * @Test
     * void testGetUser() {
     *     assertEquals("Alice", service.getUser("1"));
     *     assertEquals("Bob", service.getUser("2"));
     *     assertNull(service.getUser("999"));
     * }
     * 
     * @Test
     * void testRemoveUser() {
     *     service.removeUser("1");
     *     assertEquals(1, service.getUserCount());
     *     assertNull(service.getUser("1"));
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== JUnit 5 Solutions ===\n");
        System.out.println("These are reference solutions for JUnit 5 exercises.");
        System.out.println("Implement as JUnit 5 test classes with proper annotations.\n");

        System.out.println("--- MathUtils Solutions ---");
        System.out.println("testAbs: assertEquals(5, MathUtils.abs(-5))");
        System.out.println("testMax: assertEquals(10, MathUtils.max(10, 5))");
        System.out.println("testIsPowerOfTwo: assertTrue(MathUtils.isPowerOfTwo(8))");

        System.out.println("\n--- AgeValidator Solutions ---");
        System.out.println("testNegativeAge: assertThrows(IllegalArgumentException.class, ...)");
        System.out.println("testValidAge: assertDoesNotThrow(() -> AgeValidator.validate(25))");

        System.out.println("\n--- NumberUtils Solutions ---");
        System.out.println("testIsPrime: @ParameterizedTest @ValueSource(ints = {2,3,5,7,11})");
        System.out.println("testFactorial: @ParameterizedTest @CsvSource({\"0,1\",\"5,120\"})");

        System.out.println("\n=== All solutions completed ===");
    }
}
