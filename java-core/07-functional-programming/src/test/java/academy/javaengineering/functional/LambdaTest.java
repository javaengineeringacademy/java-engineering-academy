package academy.javaengineering.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Lambda Expression Tests")
class LambdaTest {

    @Nested
    @DisplayName("Basic Syntax Tests")
    class BasicSyntaxTests {

        @Test
        @DisplayName("Should create lambda with no parameters")
        void shouldCreateLambdaWithNoParams() {
            Runnable runnable = () -> {};
            runnable.run();
            assertTrue(true);
        }

        @Test
        @DisplayName("Should create lambda with one parameter")
        void shouldCreateLambdaWithOneParam() {
            UnaryOperator<String> toUpper = s -> s.toUpperCase();
            assertEquals("HELLO", toUpper.apply("hello"));
        }

        @Test
        @DisplayName("Should create lambda with multiple parameters")
        void shouldCreateLambdaWithMultipleParams() {
            BinaryOperator<Integer> add = (a, b) -> a + b;
            assertEquals(7, add.apply(3, 4));
        }

        @Test
        @DisplayName("Should use expression body")
        void shouldUseExpressionBody() {
            Function<Integer, Integer> doubleIt = x -> x * 2;
            assertEquals(10, doubleIt.apply(5));
        }

        @Test
        @DisplayName("Should use block body with return")
        void shouldUseBlockBody() {
            Function<String, String> reverse = s -> {
                StringBuilder sb = new StringBuilder(s);
                return sb.reverse().toString();
            };
            assertEquals("olleh", reverse.apply("hello"));
        }
    }

    @Nested
    @DisplayName("Type Inference Tests")
    class TypeInferenceTests {

        @Test
        @DisplayName("Should infer parameter types")
        void shouldInferParameterTypes() {
            Predicate<Integer> isPositive = n -> n > 0;
            assertTrue(isPositive.test(5));
            assertFalse(isPositive.test(-1));
        }

        @Test
        @DisplayName("Should use explicit types")
        void shouldUseExplicitTypes() {
            java.util.Comparator<String> comp =
                (String a, String b) -> a.compareTo(b);
            assertTrue(comp.compare("apple", "banana") < 0);
        }

        @Test
        @DisplayName("Should use var parameters")
        void shouldUseVarParameters() {
            Function<String, Integer> toLength = (var s) -> s.length();
            assertEquals(5, toLength.apply("hello"));
        }
    }

    @Nested
    @DisplayName("Variable Capture Tests")
    class VariableCaptureTests {

        @Test
        @DisplayName("Should capture effectively final variable")
        void shouldCaptureEffectivelyFinalVariable() {
            String prefix = "ITEM-";
            List<String> items = Arrays.asList("A", "B");
            List<String> result = new java.util.ArrayList<>();
            items.forEach(item -> result.add(prefix + item));
            assertEquals(2, result.size());
            assertEquals("ITEM-A", result.get(0));
            assertEquals("ITEM-B", result.get(1));
        }

        @Test
        @DisplayName("Should use AtomicInteger for mutable state")
        void shouldUseAtomicIntegerForMutableState() {
            AtomicInteger counter = new AtomicInteger(0);
            List<String> items = Arrays.asList("x", "y", "z");
            items.forEach(item -> counter.incrementAndGet());
            assertEquals(3, counter.get());
        }
    }

    @Nested
    @DisplayName("Predicate Composition Tests")
    class PredicateCompositionTests {

        @Test
        @DisplayName("Should compose predicates with and")
        void shouldComposeWithAnd() {
            Predicate<Integer> isPositive = n -> n > 0;
            Predicate<Integer> isEven = n -> n % 2 == 0;
            Predicate<Integer> isPositiveEven = isPositive.and(isEven);
            assertFalse(isPositiveEven.test(5));
            assertTrue(isPositiveEven.test(6));
        }

        @Test
        @DisplayName("Should compose predicates with or")
        void shouldComposeWithOr() {
            Predicate<Integer> isPositive = n -> n > 0;
            Predicate<Integer> isLessThan100 = n -> n < 100;
            Predicate<Integer> composed = isPositive.or(isLessThan100);
            assertTrue(composed.test(-5));
            assertTrue(composed.test(50));
        }

        @Test
        @DisplayName("Should negate predicate")
        void shouldNegatePredicate() {
            Predicate<Integer> isPositive = n -> n > 0;
            Predicate<Integer> isNotPositive = isPositive.negate();
            assertTrue(isNotPositive.test(-1));
            assertFalse(isNotPositive.test(1));
        }
    }

    @Nested
    @DisplayName("Function Composition Tests")
    class FunctionCompositionTests {

        @Test
        @DisplayName("Should compose with andThen")
        void shouldComposeWithAndThen() {
            Function<Integer, Integer> doubleIt = x -> x * 2;
            Function<Integer, Integer> addTen = x -> x + 10;
            Function<Integer, Integer> pipeline = doubleIt.andThen(addTen);
            assertEquals(20, pipeline.apply(5));
        }

        @Test
        @DisplayName("Should compose with compose")
        void shouldComposeWithCompose() {
            Function<Integer, Integer> square = x -> x * x;
            Function<Integer, Integer> addTen = x -> x + 10;
            Function<Integer, Integer> pipeline = square.compose(addTen);
            assertEquals(225, pipeline.apply(5));
        }

        @Test
        @DisplayName("Should chain multiple operations")
        void shouldChainMultipleOperations() {
            Function<Integer, Integer> doubleIt = x -> x * 2;
            Function<Integer, Integer> addTen = x -> x + 10;
            Function<Integer, Integer> square = x -> x * x;
            Function<Integer, Integer> pipeline = doubleIt
                .andThen(addTen)
                .andThen(square);
            assertEquals(400, pipeline.apply(5));
        }
    }

    @Nested
    @DisplayName("Block Body Lambda Tests")
    class BlockBodyTests {

        @Test
        @DisplayName("Should process name with block body")
        void shouldProcessNameWithBlockBody() {
            Function<String, String> processName = name -> {
                String trimmed = name.trim();
                return trimmed.substring(0, 1).toUpperCase()
                    + trimmed.substring(1).toLowerCase();
            };
            assertEquals("Alice", processName.apply("  alice  "));
            assertEquals("Bob", processName.apply("  BOB  "));
        }

        @Test
        @DisplayName("Should handle exceptions in block body")
        void shouldHandleExceptionsInBlockBody() {
            Function<String, Integer> safeParse = input -> {
                try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    return -1;
                }
            };
            assertEquals(123, safeParse.apply("123"));
            assertEquals(-1, safeParse.apply("abc"));
        }
    }
}
