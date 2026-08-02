package academy.javaengineering.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Optional Tests")
class OptionalTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create empty Optional")
        void shouldCreateEmpty() {
            Optional<String> empty = Optional.empty();
            assertFalse(empty.isPresent());
        }

        @Test
        @DisplayName("Should create Optional with value")
        void shouldCreateWithValue() {
            Optional<String> present = Optional.of("Hello");
            assertTrue(present.isPresent());
            assertEquals("Hello", present.get());
        }

        @Test
        @DisplayName("Should create Optional with nullable null")
        void shouldCreateWithNullableNull() {
            Optional<String> nullable = Optional.ofNullable(null);
            assertFalse(nullable.isPresent());
        }

        @Test
        @DisplayName("Should create Optional with nullable non-null")
        void shouldCreateWithNullableNonNull() {
            Optional<String> nonNull = Optional.ofNullable("World");
            assertTrue(nonNull.isPresent());
            assertEquals("World", nonNull.get());
        }
    }

    @Nested
    @DisplayName("Value Access Tests")
    class ValueAccessTests {

        @Test
        @DisplayName("Should get value from present Optional")
        void shouldGetValueFromPresent() {
            Optional<String> present = Optional.of("Hello");
            assertEquals("Hello", present.get());
        }

        @Test
        @DisplayName("Should throw from empty Optional get")
        void shouldThrowFromEmptyGet() {
            Optional<String> empty = Optional.empty();
            assertThrows(NoSuchElementException.class, empty::get);
        }

        @Test
        @DisplayName("Should return default with orElse")
        void shouldReturnDefaultWithOrElse() {
            Optional<String> empty = Optional.empty();
            assertEquals("Default", empty.orElse("Default"));
        }

        @Test
        @DisplayName("Should return value with orElse on present")
        void shouldReturnValueWithOrElseOnPresent() {
            Optional<String> present = Optional.of("Hello");
            assertEquals("Hello", present.orElse("Default"));
        }

        @Test
        @DisplayName("Should compute default with orElseGet")
        void shouldComputeDefaultWithOrElseGet() {
            Optional<String> empty = Optional.empty();
            String result = empty.orElseGet(() -> "Computed");
            assertEquals("Computed", result);
        }

        @Test
        @DisplayName("Should throw with orElseThrow")
        void shouldThrowWithOrElseThrow() {
            Optional<String> empty = Optional.empty();
            assertThrows(RuntimeException.class,
                () -> empty.orElseThrow(() -> new RuntimeException("Missing")));
        }
    }

    @Nested
    @DisplayName("Optional Operations Tests")
    class OperationsTests {

        @Test
        @DisplayName("Should execute ifPresent on present")
        void shouldExecuteIfPresentOnPresent() {
            Optional<String> present = Optional.of("Hello");
            String[] holder = {""};
            present.ifPresent(v -> holder[0] = v);
            assertEquals("Hello", holder[0]);
        }

        @Test
        @DisplayName("Should not execute ifPresent on empty")
        void shouldNotExecuteIfPresentOnEmpty() {
            Optional<String> empty = Optional.empty();
            boolean[] executed = {false};
            empty.ifPresent(v -> executed[0] = true);
            assertFalse(executed[0]);
        }

        @Test
        @DisplayName("Should execute ifPresentOrElse on present")
        void shouldExecuteIfPresentOrElseOnPresent() {
            Optional<String> present = Optional.of("Hello");
            String[] holder = {""};
            present.ifPresentOrElse(
                v -> holder[0] = v,
                () -> holder[0] = "empty"
            );
            assertEquals("Hello", holder[0]);
        }

        @Test
        @DisplayName("Should execute ifPresentOrElse on empty")
        void shouldExecuteIfPresentOrElseOnEmpty() {
            Optional<String> empty = Optional.empty();
            String[] holder = {""};
            empty.ifPresentOrElse(
                v -> holder[0] = v,
                () -> holder[0] = "empty"
            );
            assertEquals("empty", holder[0]);
        }

        @Test
        @DisplayName("Should map value")
        void shouldMapValue() {
            Optional<String> present = Optional.of("hello");
            Optional<Integer> length = present.map(String::length);
            assertTrue(length.isPresent());
            assertEquals(5, length.get());
        }

        @Test
        @DisplayName("Should map empty to empty")
        void shouldMapEmptyToEmpty() {
            Optional<String> empty = Optional.empty();
            Optional<Integer> result = empty.map(String::length);
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should filter matching")
        void shouldFilterMatching() {
            Optional<String> present = Optional.of("hello");
            Optional<String> filtered = present.filter(s -> s.length() > 3);
            assertTrue(filtered.isPresent());
        }

        @Test
        @DisplayName("Should filter non-matching")
        void shouldFilterNonMatching() {
            Optional<String> present = Optional.of("hi");
            Optional<String> filtered = present.filter(s -> s.length() > 3);
            assertFalse(filtered.isPresent());
        }
    }

    @Nested
    @DisplayName("Chaining Tests")
    class ChainingTests {

        @Test
        @DisplayName("Should chain map operations")
        void shouldChainMapOperations() {
            Optional<String> name = Optional.of("  Alice  ");
            Optional<String> result = name
                .map(String::trim)
                .map(String::toUpperCase);
            assertEquals("ALICE", result.orElse(""));
        }

        @Test
        @DisplayName("Should chain with flatMap")
        void shouldChainWithFlatMap() {
            Optional<Integer> value = Optional.of(10);
            Optional<Integer> result = value
                .flatMap(v -> Optional.of(v * 2));
            assertEquals(20, result.orElse(-1));
        }

        @Test
        @DisplayName("Should handle null in chain")
        void shouldHandleNullInChain() {
            Optional<String> value = Optional.ofNullable(null);
            Optional<String> result = value
                .map(String::toUpperCase)
                .map(s -> s + "!");
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle Optional.of with null")
        void shouldHandleOfWithNull() {
            assertThrows(NullPointerException.class,
                () -> Optional.of(null));
        }

        @Test
        @DisplayName("Should handle orElse with null default")
        void shouldHandleOrElseWithNullDefault() {
            Optional<String> empty = Optional.empty();
            String result = empty.orElse(null);
            assertEquals(null, result);
        }

        @Test
        @DisplayName("Should handle isPresent correctly")
        void shouldHandleIsPresent() {
            assertTrue(Optional.of("val").isPresent());
            assertFalse(Optional.empty().isPresent());
            assertTrue(Optional.ofNullable("val").isPresent());
            assertFalse(Optional.ofNullable(null).isPresent());
        }
    }
}
