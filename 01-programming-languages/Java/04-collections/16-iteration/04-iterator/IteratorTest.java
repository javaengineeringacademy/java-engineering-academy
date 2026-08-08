import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentModificationException;

class IteratorTest {

    @Test
    void testBasicIteration() {
        List<String> list = List.of("A", "B", "C");
        Iterator<String> it = list.iterator();
        StringBuilder sb = new StringBuilder();

        while (it.hasNext()) {
            sb.append(it.next());
        }

        assertEquals("ABC", sb.toString());
    }

    @Test
    void testSafeRemoval() {
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        Iterator<Integer> it = numbers.iterator();

        while (it.hasNext()) {
            if (it.next() % 2 == 0) {
                it.remove();
            }
        }

        assertEquals(List.of(1, 3, 5), numbers);
    }

    @Test
    void testNoSuchElementException() {
        List<String> list = List.of("A");
        Iterator<String> it = list.iterator();
        it.next();  // Consumes "A"
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testConcurrentModificationException() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        Iterator<String> it = list.iterator();
        it.next();
        list.add("D");
        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    @Test
    void testForEachRemaining() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        Iterator<Integer> it = list.iterator();
        it.next();  // Skip first

        List<Integer> result = new ArrayList<>();
        it.forEachRemaining(result::add);

        assertEquals(List.of(2, 3, 4, 5), result);
    }

    @Test
    void testRemoveWithoutNext() {
        List<String> list = new ArrayList<>(List.of("A", "B"));
        Iterator<String> it = list.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void testEmptyIterator() {
        List<String> empty = List.of();
        Iterator<String> it = empty.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
}
