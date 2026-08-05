import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Functional Interface Tests")
class FunctionalInterfaceExampleTest {

    @Nested
    @DisplayName("Predicate Tests")
    class PredicateTests {

        @Test
        @DisplayName("Should test predicate")
        void shouldTestPredicate() {
            Predicate<String> isLong = s -> s.length() > 5;
            assertFalse(isLong.test("Hi"));
            assertTrue(isLong.test("Hello World"));
        }

        @Test
        @DisplayName("Should compose predicates with and")
        void shouldComposeWithAnd() {
            Predicate<Integer> isPositive = n -> n > 0;
            Predicate<Integer> isEven = n -> n % 2 == 0;
            Predicate<Integer> combined = isPositive.and(isEven);
            assertTrue(combined.test(4));
            assertFalse(combined.test(3));
        }

        @Test
        @DisplayName("Should compose predicates with or")
        void shouldComposeWithOr() {
            Predicate<Integer> isLessThan10 = n -> n < 10;
            Predicate<Integer> isEven = n -> n % 2 == 0;
            Predicate<Integer> combined = isLessThan10.or(isEven);
            assertTrue(combined.test(3));
            assertTrue(combined.test(12));
            assertFalse(combined.test(11));
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
    @DisplayName("Function Tests")
    class FunctionTests {

        @Test
        @DisplayName("Should apply function")
        void shouldApplyFunction() {
            Function<String, Integer> toLength = String::length;
            assertEquals(4, toLength.apply("Java"));
        }

        @Test
        @DisplayName("Should compose functions with andThen")
        void shouldComposeWithAndThen() {
            Function<Integer, Integer> doubleIt = x -> x * 2;
            Function<Integer, Integer> addTen = x -> x + 10;
            Function<Integer, Integer> pipeline = doubleIt.andThen(addTen);
            assertEquals(20, pipeline.apply(5));
        }

        @Test
        @DisplayName("Should compose functions with compose")
        void shouldComposeWithCompose() {
            Function<Integer, Integer> square = x -> x * x;
            Function<Integer, Integer> addTen = x -> x + 10;
            Function<Integer, Integer> pipeline = square.compose(addTen);
            assertEquals(225, pipeline.apply(5));
        }
    }

    @Nested
    @DisplayName("Consumer Tests")
    class ConsumerTests {

        @Test
        @DisplayName("Should consume value")
        void shouldConsumeValue() {
            List<String> results = new java.util.ArrayList<>();
            Consumer<String> collector = results::add;
            collector.accept("Hello");
            collector.accept("World");
            assertEquals(2, results.size());
            assertEquals("Hello", results.get(0));
        }

        @Test
        @DisplayName("Should chain consumers with andThen")
        void shouldChainConsumers() {
            List<String> list1 = new java.util.ArrayList<>();
            List<String> list2 = new java.util.ArrayList<>();
            Consumer<String> c1 = list1::add;
            Consumer<String> c2 = list2::add;
            Consumer<String> combined = c1.andThen(c2);
            combined.accept("Test");
            assertEquals(1, list1.size());
            assertEquals(1, list2.size());
        }
    }

    @Nested
    @DisplayName("Supplier Tests")
    class SupplierTests {

        @Test
        @DisplayName("Should supply value")
        void shouldSupplyValue() {
            Supplier<String> greeting = () -> "Hello";
            assertEquals("Hello", greeting.get());
        }

        @Test
        @DisplayName("Should supply new instance each time")
        void shouldSupplyNewInstance() {
            Supplier<List<String>> listFactory = java.util.ArrayList::new;
            List<String> list1 = listFactory.get();
            List<String> list2 = listFactory.get();
            assertNotNull(list1);
            assertNotNull(list2);
            assertTrue(list1 != list2);
        }
    }

    @Nested
    @DisplayName("UnaryOperator Tests")
    class UnaryOperatorTests {

        @Test
        @DisplayName("Should apply unary operator")
        void shouldApplyUnaryOperator() {
            UnaryOperator<String> toUpper = String::toUpperCase;
            assertEquals("HELLO", toUpper.apply("hello"));
        }

        @Test
        @DisplayName("Should compose unary operators")
        void shouldComposeUnaryOperators() {
            UnaryOperator<String> trim = String::trim;
            UnaryOperator<String> upper = String::toUpperCase;
            Function<String, String> pipeline = trim.andThen(upper);
            assertEquals("HELLO", pipeline.apply("  hello  "));
        }
    }

    @Nested
    @DisplayName("BinaryOperator Tests")
    class BinaryOperatorTests {

        @Test
        @DisplayName("Should apply binary operator")
        void shouldApplyBinaryOperator() {
            BinaryOperator<Integer> add = Integer::sum;
            assertEquals(7, add.apply(3, 4));
        }

        @Test
        @DisplayName("Should use minBy and maxBy")
        void shouldUseMinByAndMaxBy() {
            BinaryOperator<Integer> min = BinaryOperator.minBy(Integer::compareTo);
            BinaryOperator<Integer> max = BinaryOperator.maxBy(Integer::compareTo);
            assertEquals(3, min.apply(3, 5));
            assertEquals(5, max.apply(3, 5));
        }
    }

    @Nested
    @DisplayName("Primitive Specialized Interfaces Tests")
    class PrimitiveInterfaceTests {

        @Test
        @DisplayName("Should use IntPredicate without boxing")
        void shouldUseIntPredicate() {
            IntPredicate isEven = n -> n % 2 == 0;
            assertTrue(isEven.test(4));
            assertFalse(isEven.test(5));
        }

        @Test
        @DisplayName("Should use IntUnaryOperator without boxing")
        void shouldUseIntUnaryOperator() {
            IntUnaryOperator square = x -> x * x;
            assertEquals(25, square.applyAsInt(5));
        }

        @Test
        @DisplayName("Should use IntConsumer")
        void shouldUseIntConsumer() {
            int[] holder = {0};
            IntConsumer setVal = x -> holder[0] = x;
            setVal.accept(42);
            assertEquals(42, holder[0]);
        }

        @Test
        @DisplayName("Should use IntSupplier")
        void shouldUseIntSupplier() {
            IntSupplier constant = () -> 99;
            assertEquals(99, constant.getAsInt());
        }
    }
}
