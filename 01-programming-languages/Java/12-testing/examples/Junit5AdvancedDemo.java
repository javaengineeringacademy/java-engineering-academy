package academy.javaengineering.testing.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.lang.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Advanced - Extensions, Parameterized Tests, Nested Tests
 */
class Junit5AdvancedDemo {

    // ============================================
    // Custom Extension - Temporary Folder
    // ============================================

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ExtendWith(TemporaryFolderExtension.class)
    @interface TempFolder {}

    static class TemporaryFolderExtension implements BeforeEachCallback, AfterEachCallback {
        private String tempPath;

        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            tempPath = System.getProperty("java.io.tmpdir") + "/junit-test-" + System.currentTimeMillis();
            new java.io.File(tempPath).mkdirs();
            System.out.println("Created temp folder: " + tempPath);
        }

        @Override
        public void afterEach(ExtensionContext context) throws Exception {
            deleteDirectory(new java.io.File(tempPath));
            System.out.println("Deleted temp folder: " + tempPath);
        }

        private void deleteDirectory(java.io.File dir) {
            if (dir.isDirectory()) {
                java.io.File[] files = dir.listFiles();
                if (files != null) {
                    for (java.io.File file : files) {
                        deleteDirectory(file);
                    }
                }
            }
            dir.delete();
        }

        public String getTempPath() {
            return tempPath;
        }
    }

    // ============================================
    // Custom Extension - Retry Failed Tests
    // ============================================

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ExtendWith(RetryExtension.class)
    @interface Retry {
        int maxAttempts() default 3;
    }

    static class RetryExtension implements TestExecutionExceptionHandler {
        @Override
        public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
            Retry retry = context.getRequiredTestMethod().getAnnotation(Retry.class);
            int maxAttempts = retry != null ? retry.maxAttempts() : 3;

            for (int i = 1; i < maxAttempts; i++) {
                try {
                    System.out.println("Retry attempt " + i + " for: " + context.getTestMethod().getName());
                    return; // Success
                } catch (Throwable t) {
                    // Continue retrying
                }
            }
            throw throwable; // All retries exhausted
        }
    }

    // ============================================
    // Custom Extension - Timing
    // ============================================

    static class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
        private long startTime;

        @Override
        public void beforeTestExecution(ExtensionContext context) {
            startTime = System.nanoTime();
        }

        @Override
        public void afterTestExecution(ExtensionContext context) {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            System.out.println(context.getTestMethod().getName() + " took " + duration + "ms");
        }
    }

    @ExtendWith(TimingExtension.class)
    static class TimingTests {

        @Test
        void fastTest() throws InterruptedException {
            Thread.sleep(10);
            assertTrue(true);
        }

        @Test
        void slowTest() throws InterruptedException {
            Thread.sleep(50);
            assertTrue(true);
        }
    }

    // ============================================
    // Parameterized Tests - Values
    // ============================================

    @ParameterizedTest(name = "Square of {0} = {1}")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testSquares(int number) {
        int expected = number * number;
        assertEquals(expected, number * number);
    }

    // ============================================
    // Parameterized Tests - CSV
    // ============================================

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
        "1, 2, 3",
        "10, 20, 30",
        "-1, 1, 0",
        "0, 0, 0"
    })
    void testAddition(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }

    @ParameterizedTest(name = "isPalindrome(\"{0}\") = {1}")
    @CsvSource({
        "racecar, true",
        "hello, false",
        "'', true",
        "a, true",
        "A man a plan a canal Panama, false"
    })
    void testIsPalindrome(String input, boolean expected) {
        String cleaned = input.toLowerCase().replaceAll("[^a-z0-9]", "");
        String reversed = new StringBuilder(cleaned).reverse().toString();
        assertEquals(expected, cleaned.equals(reversed));
    }

    // ============================================
    // Parameterized Tests - Method Source
    // ============================================

    @ParameterizedTest
    @MethodSource("stringProvider")
    void testWithMethodSource(String input) {
        assertNotNull(input);
        assertFalse(input.isEmpty());
    }

    static Stream<String> stringProvider() {
        return Stream.of("hello", "world", "junit5", "parameterized");
    }

    @ParameterizedTest
    @MethodSource("additionProvider")
    void testAdditionWithMethodSource(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> additionProvider() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(1, 2, 3),
            org.junit.jupiter.params.provider.Arguments.of(10, 20, 30),
            org.junit.jupiter.params.provider.Arguments.of(-1, 1, 0)
        );
    }

    // ============================================
    // Parameterized Tests - Enums
    // ============================================

    enum Planet {
        MERCURY(3.303e+23, 2.4397e6),
        VENUS(4.869e+24, 6.0518e6),
        EARTH(5.976e+24, 6.37814e6);

        private final double mass;
        private final double radius;

        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
        }

        double surfaceGravity() {
            final double G = 6.67300E-11;
            return G * mass / (radius * radius);
        }

        double surfaceWeight(double otherMass) {
            return otherMass * surfaceGravity();
        }
    }

    @ParameterizedTest
    @EnumSource(Planet.class)
    void testPlanetSurfaceGravity(Planet planet) {
        assertTrue(planet.surfaceGravity() > 0, planet.name() + " should have positive gravity");
    }

    // ============================================
    // Nested Tests - Organized Structure
    // ============================================

    @Nested
    @DisplayName("Stack Tests")
    class StackTests {

        private List<String> stack;

        @BeforeEach
        void setUp() {
            stack = new java.util.Stack<>();
        }

        @Test
        @DisplayName("New stack is empty")
        void testNewStackIsEmpty() {
            assertTrue(stack.isEmpty());
            assertEquals(0, stack.size());
        }

        @Nested
        @DisplayName("After pushing an element")
        class AfterPush {

            @BeforeEach
            void pushElement() {
                stack.push("element");
            }

            @Test
            @DisplayName("Stack is no longer empty")
            void testStackNotEmpty() {
                assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("Stack size is 1")
            void testStackSizeOne() {
                assertEquals(1, stack.size());
            }

            @Test
            @DisplayName("Popped element matches pushed element")
            void testPopReturnsPushedElement() {
                assertEquals("element", stack.pop());
            }

            @Nested
            @DisplayName("After popping the element")
            class AfterPop {

                @BeforeEach
                void popElement() {
                    stack.pop();
                }

                @Test
                @DisplayName("Stack is empty again")
                void testStackEmptyAgain() {
                    assertTrue(stack.isEmpty());
                }
            }
        }
    }

    // ============================================
    // Repeated Tests
    // ============================================

    @RepeatedTest(value = 3, name = "Repeated test {currentRepetition} of {totalRepetitions}")
    @DisplayName("Repeated test for flaky operation")
    void repeatedTest() {
        assertTrue(Math.random() > 0.0);
    }
}
