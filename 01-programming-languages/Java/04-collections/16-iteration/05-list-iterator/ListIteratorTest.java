import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

class ListIteratorTest {

    @Test
    void testForwardTraversal() {
        List<String> list = List.of("A", "B", "C");
        ListIterator<String> it = list.listIterator();
        List<String> result = new ArrayList<>();

        while (it.hasNext()) {
            result.add(it.next());
        }

        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testBackwardTraversal() {
        List<String> list = List.of("A", "B", "C");
        ListIterator<String> it = list.listIterator(list.size());
        List<String> result = new ArrayList<>();

        while (it.hasPrevious()) {
            result.add(it.previous());
        }

        assertEquals(List.of("C", "B", "A"), result);
    }

    @Test
    void testAddDuringIteration() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        ListIterator<String> it = list.listIterator();

        while (it.hasNext()) {
            String s = it.next();
            if (s.equals("B")) {
                it.add("X");
            }
        }

        assertEquals(List.of("A", "B", "X", "C"), list);
    }

    @Test
    void testSetDuringIteration() {
        List<String> list = new ArrayList<>(List.of("a", "b", "c"));
        ListIterator<String> it = list.listIterator();

        while (it.hasNext()) {
            it.set(it.next().toUpperCase());
        }

        assertEquals(List.of("A", "B", "C"), list);
    }

    @Test
    void testStartFromIndex() {
        List<String> list = List.of("A", "B", "C", "D", "E");
        ListIterator<String> it = list.listIterator(2);

        assertEquals("C", it.next());
        assertEquals("B", it.previous());
    }

    @Test
    void testNextAndPreviousIndex() {
        List<String> list = List.of("A", "B", "C");
        ListIterator<String> it = list.listIterator();

        assertEquals(0, it.nextIndex());
        assertEquals(-1, it.previousIndex());

        it.next();
        assertEquals(1, it.nextIndex());
        assertEquals(0, it.previousIndex());
    }

    @Test
    void testEmptyList() {
        List<String> list = new ArrayList<>();
        ListIterator<String> it = list.listIterator();

        assertFalse(it.hasNext());
        assertFalse(it.hasPrevious());
        assertEquals(0, it.nextIndex());
        assertEquals(-1, it.previousIndex());
    }
}
