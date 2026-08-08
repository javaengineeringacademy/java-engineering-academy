import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ForLoopTest {

    private List<String> names;

    @BeforeEach
    void setUp() {
        names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana", "Eve"));
    }

    @Test
    void testForwardIteration() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            result.add(names.get(i));
        }
        assertEquals(5, result.size());
        assertEquals("Alice", result.get(0));
        assertEquals("Eve", result.get(4));
    }

    @Test
    void testReverseIteration() {
        List<String> result = new ArrayList<>();
        for (int i = names.size() - 1; i >= 0; i--) {
            result.add(names.get(i));
        }
        assertEquals("Eve", result.get(0));
        assertEquals("Alice", result.get(4));
    }

    @Test
    void testEveryNthElement() {
        int[] everyThird = {0, 3};
        List<String> result = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (i % 3 == 0) {
                result.add(names.get(i));
            }
        }
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0));
        assertEquals("Diana", result.get(1));
    }

    @Test
    void testSafeRemovalBackwards() {
        for (int i = names.size() - 1; i >= 0; i--) {
            if (names.get(i).length() <= 3) {
                names.remove(i);
            }
        }
        assertEquals(3, names.size());
        assertTrue(names.contains("Alice"));
        assertTrue(names.contains("Charlie"));
        assertTrue(names.contains("Diana"));
    }

    @Test
    void testOffByOneEdge() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3));
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testEmptyList() {
        List<String> empty = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < empty.size(); i++) {
            count++;
        }
        assertEquals(0, count);
    }

    @Test
    void testCompareAdjacentElements() {
        List<Integer> numbers = new ArrayList<>(List.of(1, 3, 5, 7, 9));
        boolean sorted = true;
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) < numbers.get(i - 1)) {
                sorted = false;
                break;
            }
        }
        assertTrue(sorted);
    }
}
