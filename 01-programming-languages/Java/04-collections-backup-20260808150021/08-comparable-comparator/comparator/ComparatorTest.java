import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class ComparatorTest {

    @Test
    void testNaturalOrderComparator() {
        List<String> list = new ArrayList<>(List.of("C", "A", "B"));
        list.sort(Comparator.naturalOrder());
        assertEquals(List.of("A", "B", "C"), list);
    }

    @Test
    void testReverseOrderComparator() {
        List<String> list = new ArrayList<>(List.of("A", "C", "B"));
        list.sort(Comparator.reverseOrder());
        assertEquals(List.of("C", "B", "A"), list);
    }

    @Test
    void testComparingInt() {
        List<String> list = new ArrayList<>(List.of("Banana", "Apple", "Cherry"));
        list.sort(Comparator.comparingInt(String::length));
        assertEquals(List.of("Apple", "Banana", "Cherry"), list);
    }

    @Test
    void testThenComparing() {
        List<String> list = new ArrayList<>(List.of("bb", "aaa", "cc", "dd"));
        list.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        assertEquals(List.of("bb", "cc", "dd", "aaa"), list);
    }

    @Test
    void testNullsFirst() {
        List<String> list = new ArrayList<>(List.of("B", null, "A"));
        list.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertEquals(null, list.get(0));
        assertEquals("A", list.get(1));
        assertEquals("B", list.get(2));
    }

    @Test
    void testNullsLast() {
        List<String> list = new ArrayList<>(List.of("B", null, "A"));
        list.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals(null, list.get(2));
    }

    @Test
    void testReversedComparator() {
        Comparator<Integer> natural = Comparator.naturalOrder();
        Comparator<Integer> reversed = natural.reversed();
        List<Integer> list = new ArrayList<>(List.of(1, 3, 2));
        list.sort(reversed);
        assertEquals(List.of(3, 2, 1), list);
    }

    @Test
    void testLambdaComparator() {
        List<String> list = new ArrayList<>(List.of("CCC", "A", "BB"));
        list.sort((a, b) -> Integer.compare(a.length(), b.length()));
        assertEquals(List.of("A", "BB", "CCC"), list);
    }
}
