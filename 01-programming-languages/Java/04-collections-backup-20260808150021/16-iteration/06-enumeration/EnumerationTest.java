import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class EnumerationTest {

    @Test
    void testBasicEnumeration() {
        Vector<String> vector = new Vector<>(List.of("A", "B", "C"));
        Enumeration<String> e = vector.elements();
        List<String> result = new ArrayList<>();

        while (e.hasMoreElements()) {
            result.add(e.nextElement());
        }

        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testEnumerationHasMoreElements() {
        Vector<String> vector = new Vector<>();
        Enumeration<String> e = vector.elements();

        assertFalse(e.hasMoreElements());
    }

    @Test
    void testHashtableKeys() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("a", 1);
        table.put("b", 2);

        Enumeration<String> keys = table.keys();
        int count = 0;
        while (keys.hasMoreElements()) {
            keys.nextElement();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testEnumerationToCollectionsList() {
        Vector<Integer> vector = new Vector<>(List.of(1, 2, 3, 4, 5));
        List<Integer> list = Collections.list(vector.elements());

        assertEquals(List.of(1, 2, 3, 4, 5), list);
    }

    @Test
    void testEnumerationNoRemove() {
        Vector<String> vector = new Vector<>(List.of("A", "B"));
        Enumeration<String> e = vector.elements();
        e.nextElement();

        // Enumeration has no remove() method
        assertThrows(NoSuchMethodException.class, () -> {
            Enumeration.class.getMethod("remove");
        });
    }

    @Test
    void testEnumerationSize() {
        Vector<Integer> vector = new Vector<>(List.of(1, 2, 3));
        Enumeration<Integer> e = vector.elements();

        int count = 0;
        while (e.hasMoreElements()) {
            e.nextElement();
            count++;
        }
        assertEquals(vector.size(), count);
    }

    @Test
    void testEmptyEnumeration() {
        Vector<String> vector = new Vector<>();
        Enumeration<String> e = vector.elements();

        assertFalse(e.hasMoreElements());
    }
}
