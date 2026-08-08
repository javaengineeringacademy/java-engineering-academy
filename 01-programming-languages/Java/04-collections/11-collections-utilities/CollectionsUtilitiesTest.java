import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class CollectionsUtilitiesTest {

    @Test
    void testUnmodifiableList() {
        List<String> original = new ArrayList<>(List.of("A", "B"));
        List<String> unmodifiable = Collections.unmodifiableList(original);
        assertEquals(2, unmodifiable.size());
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("C"));
    }

    @Test
    void testUnmodifiableMap() {
        Map<String, Integer> original = new HashMap<>();
        original.put("A", 1);
        Map<String, Integer> unmodifiable = Collections.unmodifiableMap(original);
        assertEquals(1, unmodifiable.size());
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.put("B", 2));
    }

    @Test
    void testUnmodifiableSet() {
        Set<String> original = new HashSet<>(List.of("A"));
        Set<String> unmodifiable = Collections.unmodifiableSet(original);
        assertEquals(1, unmodifiable.size());
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("B"));
    }

    @Test
    void testSynchronizedList() {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        list.add("A");
        assertEquals(1, list.size());
    }

    @Test
    void testSort() {
        List<Integer> list = new ArrayList<>(List.of(5, 2, 8, 1));
        Collections.sort(list);
        assertEquals(List.of(1, 2, 5, 8), list);
    }

    @Test
    void testBinarySearch() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        assertEquals(2, Collections.binarySearch(list, 3));
        assertTrue(Collections.binarySearch(list, 6) < 0);
    }

    @Test
    void testReverse() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        Collections.reverse(list);
        assertEquals(List.of("C", "B", "A"), list);
    }

    @Test
    void testShuffle() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.shuffle(list);
        assertEquals(5, list.size());
        assertTrue(list.containsAll(List.of(1, 2, 3, 4, 5)));
    }

    @Test
    void testFrequency() {
        List<String> list = List.of("A", "B", "A", "C", "A");
        assertEquals(3, Collections.frequency(list, "A"));
        assertEquals(0, Collections.frequency(list, "Z"));
    }

    @Test
    void testDisjoint() {
        List<String> list1 = List.of("A", "B");
        List<String> list2 = List.of("C", "D");
        List<String> list3 = List.of("B", "C");
        assertTrue(Collections.disjoint(list1, list2));
        assertFalse(Collections.disjoint(list1, list3));
    }

    @Test
    void testMinMax() {
        List<Integer> list = List.of(3, 1, 4, 1, 5, 9);
        assertEquals(Integer.valueOf(1), Collections.min(list));
        assertEquals(Integer.valueOf(9), Collections.max(list));
    }

    @Test
    void testSwap() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        Collections.swap(list, 0, 2);
        assertEquals(List.of("C", "B", "A"), list);
    }

    @Test
    void testRotate() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.rotate(list, 2);
        assertEquals(List.of(4, 5, 1, 2, 3), list);
    }

    @Test
    void testNCopies() {
        List<String> list = Collections.nCopies(3, "X");
        assertEquals(3, list.size());
        assertEquals("X", list.get(0));
    }

    @Test
    void testFill() {
        List<String> list = new ArrayList<>(Arrays.asList(new String[3]));
        Collections.fill(list, "A");
        assertEquals(List.of("A", "A", "A"), list);
    }

    @Test
    void testReplaceAll() {
        List<String> list = new ArrayList<>(List.of("A", "B", "A"));
        Collections.replaceAll(list, "A", "X");
        assertEquals(List.of("X", "B", "X"), list);
    }
}
