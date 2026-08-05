import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class IteratorDemoTest {

    @Test
    void testForwardIteration() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        StringBuilder result = new StringBuilder();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            result.append(it.next()).append(" ");
        }
        assertEquals("A B C D E ", result.toString());
    }

    @Test
    void testRemoveDuringIteration() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals("C")) {
                it.remove();
            }
        }
        assertEquals(4, list.size());
        assertFalse(list.contains("C"));
    }

    @Test
    void testEnhancedForLoop() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        List<String> collected = new ArrayList<>();
        for (String s : list) {
            collected.add(s);
        }
        assertEquals(list, collected);
    }

    @Test
    void testForwardListIterator() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        ListIterator<String> it = list.listIterator();
        List<String> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(it.next());
        }
        assertEquals(list, result);
    }

    @Test
    void testBackwardListIterator() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        ListIterator<String> it = list.listIterator(list.size());
        List<String> result = new ArrayList<>();
        while (it.hasPrevious()) {
            result.add(it.previous());
        }
        assertEquals(List.of("C", "B", "A"), result);
    }

    @Test
    void testReplaceWithSet() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        ListIterator<String> it = list.listIterator();
        while (it.hasNext()) {
            it.set(it.next().toLowerCase());
        }
        assertEquals(List.of("a", "b", "c"), list);
    }

    @Test
    void testConcurrentModificationException() {
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
    void testSafeRemovalWithIterator() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals("C")) {
                it.remove();
            }
        }
        assertFalse(list.contains("C"));
        assertEquals(4, list.size());
    }
}
