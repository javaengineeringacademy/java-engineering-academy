import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class StreamIterationTest {

    @Test
    void testBasicStream() {
        List<String> list = List.of("A", "B", "C");
        long count = list.stream().count();
        assertEquals(3, count);
    }

    @Test
    void testFilterMapCollect() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        List<Integer> result = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * 2)
            .collect(Collectors.toList());

        assertEquals(List.of(4, 8), result);
    }

    @Test
    void testLazyEvaluation() {
        // Stream is lazy - no processing until terminal
        StringBuilder sb = new StringBuilder();
        Stream<Integer> stream = List.of(1, 2, 3).stream()
            .filter(n -> {
                sb.append(n);
                return true;
            });

        assertEquals("", sb.toString());  // Nothing processed yet

        stream.count();
        assertEquals("123", sb.toString());  // Now processed
    }

    @Test
    void testParallelSum() {
        List<Integer> numbers = IntStream.rangeClosed(1, 100).boxed().toList();
        int sum = numbers.parallelStream().reduce(0, Integer::sum);
        assertEquals(5050, sum);
    }

    @Test
    void testfindFirst() {
        List<String> list = List.of("Alice", "Bob", "Charlie");
        String first = list.stream()
            .filter(s -> s.length() > 3)
            .findFirst()
            .orElse("");

        assertEquals("Alice", first);
    }

    @Test
    void testAnyMatch() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        assertTrue(hasEven);
    }

    @Test
    void testReduce() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        assertEquals(120, product);
    }
}
