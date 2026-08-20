package academy.javaengineering.testing.junit5.advanced.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class Junit5AdvancedExamples {

    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedExamples {

        @ParameterizedTest(name = "{0} squared = {1}")
        @CsvSource({"1, 1", "2, 4", "3, 9", "4, 16", "5, 25"})
        void shouldCalculateSquare(String input, String expected) {
            int number = Integer.parseInt(input);
            int result = Integer.parseInt(expected);
            assertEquals(result, number * number);
        }

        @ParameterizedTest
        @ValueSource(strings = {"racecar", "madam", "level"})
        void shouldDetectPalindromes(String word) {
            String reversed = new StringBuilder(word).reverse().toString();
            assertEquals(word, reversed);
        }

        @ParameterizedTest
        @MethodSource("stringProvider")
        void shouldProcessStrings(String input) {
            assertNotNull(input);
            assertFalse(input.isBlank());
        }

        static Stream<String> stringProvider() {
            return Stream.of("hello", "world", "junit5");
        }
    }

    @Nested
    @DisplayName("Dynamic Tests")
    class DynamicTestExamples {

        @TestFactory
        Stream<DynamicTest> mathOperations() {
            return Stream.of(
                dynamicTest("Addition", () -> assertEquals(4, 2 + 2)),
                dynamicTest("Subtraction", () -> assertEquals(2, 5 - 3)),
                dynamicTest("Multiplication", () -> assertEquals(6, 2 * 3))
            );
        }
    }

    @Nested
    @DisplayName("Custom Extension")
    class ExtensionExamples {

        static class TimingExtension implements BeforeEachCallback, AfterEachCallback {
            private long startTime;

            @Override
            public void beforeEach(ExtensionContext context) {
                startTime = System.nanoTime();
            }

            @Override
            public void afterEach(ExtensionContext context) {
                long duration = System.nanoTime() - startTime;
                System.out.printf("[%s] took %d ms%n",
                    context.getDisplayName(), duration / 1_000_000);
            }
        }

        @ExtendWith(TimingExtension.class)
        @Test
        void shouldRunWithTiming() {
            assertTrue(true);
        }
    }
}
