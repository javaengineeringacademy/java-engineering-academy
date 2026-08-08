import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.jupiter.api.Test;

class FailFastVsFailSafeTest {

    @Test
    void testFailFastArrayList() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        assertThrows(ConcurrentModificationException.class, () -> {
            for (String s : list) {
                if (s.equals("B")) {
                    list.remove(s);
                }
            }
        });
    }

    @Test
    void testFailFastHashMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        assertThrows(ConcurrentModificationException.class, () -> {
            for (String key : map.keySet()) {
                if (key.equals("A")) {
                    map.remove(key);
                }
            }
        });
    }

    @Test
    void testFailFastHashSet() {
        Set<String> set = new HashSet<>(List.of("A", "B", "C"));
        assertThrows(ConcurrentModificationException.class, () -> {
            for (String s : set) {
                if (s.equals("B")) {
                    set.remove(s);
                }
            }
        });
    }

    @Test
    void testFailSafeCopyOnWriteArrayList() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));
        assertDoesNotThrow(() -> {
            for (String s : list) {
                if (s.equals("B")) {
                    list.add("D");
                }
            }
        });
    }

    @Test
    void testFailSafeConcurrentHashMap() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        assertDoesNotThrow(() -> {
            for (String key : map.keySet()) {
                if (key.equals("A")) {
                    map.put("C", 3);
                }
            }
        });
    }

    @Test
    void testIteratorSafeRemoval() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals("B")) {
                it.remove();
            }
        }
        assertEquals(List.of("A", "C", "D"), list);
    }

    @Test
    void testRemoveIfMethod() {
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        numbers.removeIf(n -> n % 2 == 0);
        assertEquals(List.of(1, 3, 5), numbers);
    }

    @Test
    void testCopyOnWriteArrayListSnapshot() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("A", "B"));
        Iterator<String> it = list.iterator();
        list.add("C");

        java.util.List<String> seen = new ArrayList<>();
        while (it.hasNext()) {
            seen.add(it.next());
        }
        assertEquals(2, seen.size());
        assertFalse(seen.contains("C"));
    }
}
