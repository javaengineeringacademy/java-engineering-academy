import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

class SpliteratorTest {

    @Test
    void testBasicSpliterator() {
        List<String> list = List.of("A", "B", "C");
        Spliterator<String> spliterator = list.spliterator();

        assertEquals(3, spliterator.estimateSize());
        assertEquals(3, spliterator.getExactSizeIfKnown());
    }

    @Test
    void testTryAdvance() {
        List<String> list = List.of("A", "B", "C");
        Spliterator<String> spliterator = list.spliterator();
        List<String> result = new ArrayList<>();

        while (spliterator.tryAdvance(result::add)) {
            // Processed
        }

        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testTrySplit() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        Spliterator<Integer> original = list.spliterator();
        Spliterator<Integer> left = original.trySplit();

        assertNotNull(left);
        assertTrue(left.estimateSize() > 0);
        assertTrue(original.estimateSize() > 0);
    }

    @Test
    void testCharacteristicsOrdered() {
        List<String> list = List.of("A", "B");
        Spliterator<String> spliterator = list.spliterator();
        int chars = spliterator.characteristics();

        assertTrue((chars & Spliterator.ORDERED) != 0);
    }

    @Test
    void testCharacteristicsSized() {
        List<String> list = List.of("A", "B");
        Spliterator<String> spliterator = list.spliterator();
        int chars = spliterator.characteristics();

        assertTrue((chars & Spliterator.SIZED) != 0);
    }

    @Test
    void testForEachRemaining() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        Spliterator<Integer> spliterator = list.spliterator();
        spliterator.tryAdvance(n -> {});  // Skip first

        List<Integer> result = new ArrayList<>();
        spliterator.forEachRemaining(result::add);

        assertEquals(List.of(2, 3, 4, 5), result);
    }

    @Test
    void testEmptySpliterator() {
        List<String> list = List.of();
        Spliterator<String> spliterator = list.spliterator();

        assertEquals(0, spliterator.estimateSize());
        assertFalse(spliterator.tryAdvance(s -> {}));
    }
}
