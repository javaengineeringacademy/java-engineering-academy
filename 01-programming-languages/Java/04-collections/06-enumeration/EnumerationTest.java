import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class EnumerationTest {

    @Test
    void testEnumerationHasMoreElements() {
        Vector<String> vector = new Vector<>(List.of("A", "B", "C"));
        Enumeration<String> enumElements = vector.elements();
        assertTrue(enumElements.hasMoreElements());
        assertEquals("A", enumElements.nextElement());
        assertTrue(enumElements.hasMoreElements());
    }

    @Test
    void testEnumerationOnEmptyVector() {
        Vector<String> vector = new Vector<>();
        Enumeration<String> enumElements = vector.elements();
        assertFalse(enumElements.hasMoreElements());
    }

    @Test
    void testEnumerationTraversesAllElements() {
        Vector<Integer> vector = new Vector<>(List.of(1, 2, 3, 4, 5));
        Enumeration<Integer> enumElements = vector.elements();
        java.util.List<Integer> result = new ArrayList<>();
        while (enumElements.hasMoreElements()) {
            result.add(enumElements.nextElement());
        }
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    void testEnumerationOnHashtable() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("A", 1);
        table.put("B", 2);
        Enumeration<String> keys = table.keys();
        int count = 0;
        while (keys.hasMoreElements()) {
            keys.nextElement();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testEnumerationValues() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("A", 10);
        table.put("B", 20);
        Enumeration<Integer> values = table.elements();
        java.util.List<Integer> result = new ArrayList<>();
        while (values.hasMoreElements()) {
            result.add(values.nextElement());
        }
        assertEquals(2, result.size());
        assertTrue(result.contains(10));
        assertTrue(result.contains(20));
    }

    @Test
    void testCollectionsEnumeration() {
        List<String> list = new ArrayList<>(List.of("X", "Y", "Z"));
        Enumeration<String> enumElements = Collections.enumeration(list);
        java.util.List<String> result = new ArrayList<>();
        while (enumElements.hasMoreElements()) {
            result.add(enumElements.nextElement());
        }
        assertEquals(List.of("X", "Y", "Z"), result);
    }

    @Test
    void testCollectionsEnumerationFromSet() {
        Set<Integer> set = new LinkedHashSet<>(List.of(10, 20, 30));
        Enumeration<Integer> enumElements = Collections.enumeration(set);
        java.util.List<Integer> result = new ArrayList<>();
        while (enumElements.hasMoreElements()) {
            result.add(enumElements.nextElement());
        }
        assertEquals(List.of(10, 20, 30), result);
    }

    @Test
    void testEnumerationIsReadOnly() {
        Vector<String> vector = new Vector<>(List.of("A", "B", "C"));
        Enumeration<String> enumElements = vector.elements();
        while (enumElements.hasMoreElements()) {
            enumElements.nextElement();
        }
        // Enumeration has no remove method — this verifies read-only nature
        assertEquals(3, vector.size());
    }
}
