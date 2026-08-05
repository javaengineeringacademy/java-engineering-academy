import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Method Reference Tests")
class MethodReferenceExampleTest {

    @Nested
    @DisplayName("Static Method Reference Tests")
    class StaticMethodTests {

        @Test
        @DisplayName("Should parse integer with method reference")
        void shouldParseInteger() {
            Function<String, Integer> parseInt = Integer::parseInt;
            assertEquals(123, parseInt.apply("123"));
        }

        @Test
        @DisplayName("Should sum integers with method reference")
        void shouldSumIntegers() {
            BinaryOperator<Integer> add = Integer::sum;
            assertEquals(7, add.apply(3, 4));
        }

        @Test
        @DisplayName("Should get absolute value with method reference")
        void shouldGetAbsoluteValue() {
            Function<Integer, Integer> abs = Math::abs;
            assertEquals(5, abs.apply(-5));
        }
    }

    @Nested
    @DisplayName("Instance Method Reference Tests")
    class InstanceMethodTests {

        @Test
        @DisplayName("Should concatenate with particular object reference")
        void shouldConcatenateWithParticularObject() {
            String prefix = "Mr. ";
            Function<String, String> addPrefix = prefix::concat;
            assertEquals("Mr. Smith", addPrefix.apply("Smith"));
        }

        @Test
        @DisplayName("Should get length with arbitrary object reference")
        void shouldGetLengthWithArbitraryObject() {
            Function<String, Integer> length = String::length;
            assertEquals(5, length.apply("hello"));
        }

        @Test
        @DisplayName("Should check isEmpty with method reference")
        void shouldCheckIsEmpty() {
            Predicate<String> isEmpty = String::isEmpty;
            assertTrue(isEmpty.test(""));
        }

        @Test
        @DisplayName("Should convert to uppercase with method reference")
        void shouldConvertToUppercase() {
            UnaryOperator<String> toUpper = String::toUpperCase;
            assertEquals("HELLO", toUpper.apply("hello"));
        }
    }

    @Nested
    @DisplayName("Constructor Reference Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create StringBuilder with constructor reference")
        void shouldCreateStringBuilder() {
            Supplier<StringBuilder> builder = StringBuilder::new;
            StringBuilder sb = builder.get();
            sb.append("Hello");
            assertEquals("Hello", sb.toString());
        }

        @Test
        @DisplayName("Should create ArrayList with constructor reference")
        void shouldCreateArrayList() {
            Supplier<List<String>> listFactory = ArrayList::new;
            List<String> list = listFactory.get();
            list.add("test");
            assertEquals(1, list.size());
        }

        @Test
        @DisplayName("Should create array with constructor reference")
        void shouldCreateArray() {
            Function<Integer, int[]> arrayFactory = int[]::new;
            int[] arr = arrayFactory.apply(5);
            assertEquals(5, arr.length);
        }
    }

    @Nested
    @DisplayName("Stream Method Reference Tests")
    class StreamMethodTests {

        @Test
        @DisplayName("Should map with method reference")
        void shouldMapWithMethodReference() {
            List<String> names = Arrays.asList("alice", "bob", "charlie");
            List<String> upper = names.stream()
                .map(String::toUpperCase)
                .toList();
            assertEquals(List.of("ALICE", "BOB", "CHARLIE"), upper);
        }

        @Test
        @DisplayName("Should sort with method reference")
        void shouldSortWithMethodReference() {
            List<String> names = Arrays.asList("charlie", "alice", "bob");
            List<String> sorted = names.stream()
                .sorted(String::compareToIgnoreCase)
                .toList();
            assertEquals(List.of("alice", "bob", "charlie"), sorted);
        }

        @Test
        @DisplayName("Should forEach with method reference")
        void shouldForEachWithMethodReference() {
            List<String> result = new java.util.ArrayList<>();
            List.of("a", "b", "c").stream()
                .forEach(result::add);
            assertEquals(3, result.size());
        }
    }

    @Nested
    @DisplayName("Composition Tests")
    class CompositionTests {

        @Test
        @DisplayName("Should compose method references")
        void shouldComposeMethodReferences() {
            Function<String, String> trim = String::trim;
            Function<String, String> toLower = String::toLowerCase;
            Function<String, Integer> length = String::length;

            Function<String, Integer> pipeline = trim
                .andThen(toLower)
                .andThen(length);

            assertEquals(5, pipeline.apply("  Hello  "));
        }

        @Test
        @DisplayName("Should compose predicates")
        void shouldComposePredicates() {
            Predicate<String> isNotEmpty = s -> !s.isEmpty();
            Predicate<String> hasMinLength = s -> s.length() >= 3;

            Predicate<String> isValid = isNotEmpty.and(hasMinLength);

            assertTrue(!isValid.test("ab"));
            assertTrue(isValid.test("abc"));
        }
    }
}
