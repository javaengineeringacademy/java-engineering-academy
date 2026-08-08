import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class ComparableTest {

    @Test
    void testStringComparison() {
        assertTrue("Apple".compareTo("Banana") < 0);
        assertTrue("Banana".compareTo("Apple") > 0);
        assertEquals(0, "Apple".compareTo("Apple"));
    }

    @Test
    void testStringComparisonCaseSensitive() {
        assertTrue("Apple".compareTo("apple") < 0);
        assertTrue("apple".compareTo("Apple") > 0);
    }

    @Test
    void testStringCaseInsensitive() {
        assertEquals(0, "Apple".compareToIgnoreCase("apple"));
        assertTrue("Apple".compareToIgnoreCase("banana") < 0);
    }

    @Test
    void testIntegerComparison() {
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        Collections.sort(numbers);
        assertEquals(List.of(1, 2, 3, 5, 8, 9), numbers);
    }

    @Test
    void testTreeSetNaturalOrdering() {
        TreeSet<String> set = new TreeSet<>();
        set.add("Banana");
        set.add("Apple");
        set.add("Cherry");
        assertEquals("Apple", set.first());
        assertEquals("Cherry", set.last());
    }

    @Test
    void testTreeMapNaturalOrdering() {
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(3, "C");
        map.put(1, "A");
        map.put(2, "B");
        assertEquals(Integer.valueOf(1), map.firstKey());
        assertEquals(Integer.valueOf(3), map.lastKey());
    }

    @Test
    void testComparableConsistency() {
        String a = "Test";
        String b = "Test";
        assertEquals(0, a.compareTo(b));
        assertTrue(a.equals(b));
    }

    @Test
    void testComparableTransitivity() {
        String a = "A";
        String b = "B";
        String c = "C";
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(c) < 0);
        assertTrue(a.compareTo(c) < 0);
    }
}
