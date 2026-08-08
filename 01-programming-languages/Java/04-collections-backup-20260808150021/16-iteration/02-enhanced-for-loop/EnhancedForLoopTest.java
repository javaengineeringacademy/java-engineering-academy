import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.ConcurrentModificationException;

class EnhancedForLoopTest {

    @Test
    void testArrayIteration() {
        int[] numbers = {1, 2, 3, 4, 5};
        List<Integer> result = new ArrayList<>();
        for (int num : numbers) {
            result.add(num);
        }
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    void testListIteration() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<String> result = new ArrayList<>();
        for (String name : names) {
            result.add(name);
        }
        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0));
    }

    @Test
    void testSetIteration() {
        Set<Integer> set = Set.of(10, 20, 30);
        int sum = 0;
        for (int num : set) {
            sum += num;
        }
        assertEquals(60, sum);
    }

    @Test
    void testMapEntrySetIteration() {
        Map<String, Integer> map = Map.of("a", 1, "b", 2);
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            result.add(entry.getKey() + "=" + entry.getValue());
        }
        assertEquals(2, result.size());
    }

    @Test
    void testConcurrentModificationException() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        assertThrows(ConcurrentModificationException.class, () -> {
            for (String s : list) {
                if (s.equals("B")) {
                    list.add("X");
                }
            }
        });
    }

    @Test
    void testArrayModificationDoesNotAffectOriginal() {
        int[] original = {1, 2, 3};
        List<Integer> modified = new ArrayList<>();
        for (int num : original) {
            modified.add(num * 2);
        }
        assertEquals(1, original[0]);  // Original unchanged
        assertEquals(List.of(2, 4, 6), modified);
    }

    @Test
    void testEmptyCollection() {
        List<String> empty = List.of();
        int count = 0;
        for (String s : empty) {
            count++;
        }
        assertEquals(0, count);
    }
}
