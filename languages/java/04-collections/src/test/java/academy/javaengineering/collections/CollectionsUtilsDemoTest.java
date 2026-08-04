package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CollectionsUtilsDemoTest {

    private List<Integer> numbers;

    @BeforeEach
    void setUp() {
        numbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));
    }

    @Nested
    @DisplayName("Sorting Tests")
    class SortingTests {

        @Test
        @DisplayName("Should sort in natural order")
        void testNaturalOrder() {
            Collections.sort(numbers);
            assertEquals(List.of(1, 2, 3, 5, 8, 9), numbers);
        }

        @Test
        @DisplayName("Should sort in reverse order")
        void testReverseOrder() {
            Collections.sort(numbers, Comparator.reverseOrder());
            assertEquals(List.of(9, 8, 5, 3, 2, 1), numbers);
        }

        @Test
        @DisplayName("Should sort by string length")
        void testSortByLength() {
            List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob", "Diana"));
            Collections.sort(names, Comparator.comparingInt(String::length));
            assertEquals(List.of("Bob", "Alice", "Diana", "Charlie"), names);
        }
    }

    @Nested
    @DisplayName("Searching Tests")
    class SearchingTests {

        @Test
        @DisplayName("Should find element with binarySearch")
        void testBinarySearch() {
            List<Integer> sorted = new ArrayList<>(List.of(1, 3, 5, 7, 9, 11));
            int index = Collections.binarySearch(sorted, 7);
            assertEquals(3, index);
        }

        @Test
        @DisplayName("Should return negative for missing element")
        void testBinarySearchNotFound() {
            List<Integer> sorted = new ArrayList<>(List.of(1, 3, 5, 7, 9, 11));
            int index = Collections.binarySearch(sorted, 6);
            assertTrue(index < 0);
        }

        @Test
        @DisplayName("Should count frequency")
        void testFrequency() {
            List<String> names = List.of("Alice", "Bob", "Alice", "Charlie", "Alice");
            assertEquals(3, Collections.frequency(names, "Alice"));
            assertEquals(1, Collections.frequency(names, "Bob"));
            assertEquals(0, Collections.frequency(names, "David"));
        }
    }

    @Nested
    @DisplayName("Utility Operations Tests")
    class UtilityTests {

        @Test
        @DisplayName("Should find min and max")
        void testMinMax() {
            assertEquals(1, Collections.min(numbers));
            assertEquals(9, Collections.max(numbers));
        }

        @Test
        @DisplayName("Should shuffle and maintain size")
        void testShuffle() {
            int sizeBefore = numbers.size();
            Collections.shuffle(numbers);
            assertEquals(sizeBefore, numbers.size());
        }

        @Test
        @DisplayName("Should create unmodifiable list")
        void testUnmodifiable() {
            List<String> original = new ArrayList<>(List.of("A", "B", "C"));
            List<String> unmodifiable = Collections.unmodifiableList(original);
            assertEquals(original, unmodifiable);
            assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("D"));
        }

        @Test
        @DisplayName("Should replace all occurrences")
        void testReplaceAll() {
            List<String> list = new ArrayList<>(List.of("A", "B", "C", "B"));
            Collections.replaceAll(list, "B", "X");
            assertEquals(List.of("A", "X", "C", "X"), list);
        }
    }
}
