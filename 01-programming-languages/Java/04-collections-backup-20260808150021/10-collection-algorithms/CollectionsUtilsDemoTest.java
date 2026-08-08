import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class CollectionsUtilsDemoTest {

    @Test
    void testNaturalOrder() {
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        Collections.sort(numbers);
        assertEquals(List.of(1, 2, 3, 5, 8, 9), numbers);
    }

    @Test
    void testReverseOrder() {
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        Collections.sort(numbers, Comparator.reverseOrder());
        assertEquals(List.of(9, 8, 5, 3, 2, 1), numbers);
    }

    @Test
    void testSortByLength() {
        List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob", "Diana"));
        Collections.sort(names, Comparator.comparingInt(String::length));
        assertEquals(List.of("Bob", "Alice", "Diana", "Charlie"), names);
    }

    @Test
    void testBinarySearch() {
        List<Integer> sorted = new ArrayList<>(List.of(1, 3, 5, 7, 9, 11));
        int index = Collections.binarySearch(sorted, 7);
        assertEquals(3, index);
    }

    @Test
    void testBinarySearchNotFound() {
        List<Integer> sorted = new ArrayList<>(List.of(1, 3, 5, 7, 9, 11));
        int index = Collections.binarySearch(sorted, 6);
        assertTrue(index < 0);
    }

    @Test
    void testFrequency() {
        List<String> names = List.of("Alice", "Bob", "Alice", "Charlie", "Alice");
        assertEquals(3, Collections.frequency(names, "Alice"));
        assertEquals(1, Collections.frequency(names, "Bob"));
        assertEquals(0, Collections.frequency(names, "David"));
    }

    @Test
    void testMinMax() {
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        assertEquals(1, Collections.min(numbers));
        assertEquals(9, Collections.max(numbers));
    }

    @Test
    void testShuffle() {
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        int sizeBefore = numbers.size();
        Collections.shuffle(numbers);
        assertEquals(sizeBefore, numbers.size());
    }

    @Test
    void testUnmodifiable() {
        List<String> original = new ArrayList<>(List.of("A", "B", "C"));
        List<String> unmodifiable = Collections.unmodifiableList(original);
        assertEquals(original, unmodifiable);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("D"));
    }

    @Test
    void testReplaceAll() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "B"));
        Collections.replaceAll(list, "B", "X");
        assertEquals(List.of("A", "X", "C", "X"), list);
    }
}
