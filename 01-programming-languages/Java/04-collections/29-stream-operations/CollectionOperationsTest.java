import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class CollectionOperationsTest {

    @Test
    void testFilterEven() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> even = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(java.util.stream.Collectors.toList());
        assertEquals(List.of(2, 4, 6, 8, 10), even);
    }

    @Test
    void testFilterByLength() {
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");
        List<String> longNames = names.stream()
                .filter(name -> name.length() > 4)
                .collect(java.util.stream.Collectors.toList());
        assertEquals(List.of("Alice", "Charlie", "Diana"), longNames);
    }

    @Test
    void testMapToUpperCase() {
        List<String> names = List.of("Alice", "Bob");
        List<String> upper = names.stream()
                .map(String::toUpperCase)
                .collect(java.util.stream.Collectors.toList());
        assertEquals(List.of("ALICE", "BOB"), upper);
    }

    @Test
    void testReduceSum() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        int sum = numbers.stream().reduce(0, Integer::sum);
        assertEquals(15, sum);
    }

    @Test
    void testReduceProduct() {
        List<Integer> small = List.of(1, 2, 3, 4);
        int product = small.stream().reduce(1, (a, b) -> a * b);
        assertEquals(24, product);
    }

    @Test
    void testCollectToList() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> result = numbers.stream()
                .filter(n -> n > 5)
                .collect(java.util.stream.Collectors.toList());
        assertEquals(List.of(6, 7, 8, 9, 10), result);
    }

    @Test
    void testCollectToSet() {
        List<String> duplicates = List.of("A", "B", "A", "C", "B");
        Set<String> unique = duplicates.stream()
                .collect(java.util.stream.Collectors.toSet());
        assertEquals(3, unique.size());
    }

    @Test
    void testFindFirst() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Optional<String> first = names.stream()
                .filter(name -> name.length() > 4)
                .findFirst();
        assertTrue(first.isPresent());
        assertEquals("Alice", first.get());
    }

    @Test
    void testAnyMatch() {
        List<Integer> numbers = List.of(2, 4, 6, 8, 10);
        assertTrue(numbers.stream().anyMatch(n -> n == 5));
        assertFalse(numbers.stream().anyMatch(n -> n == 15));
    }

    @Test
    void testAllMatch() {
        List<Integer> numbers = List.of(2, 4, 6, 8, 10);
        assertTrue(numbers.stream().allMatch(n -> n > 0));
        assertFalse(numbers.stream().allMatch(n -> n > 5));
    }

    @Test
    void testNoneMatch() {
        List<Integer> numbers = List.of(2, 4, 6, 8, 10);
        assertTrue(numbers.stream().noneMatch(n -> n < 0));
        assertFalse(numbers.stream().noneMatch(n -> n == 5));
    }

    @Test
    void testFlatMap() {
        List<List<Integer>> nested = List.of(
                List.of(1, 2),
                List.of(3, 4),
                List.of(5, 6)
        );
        List<Integer> flat = nested.stream()
                .flatMap(Collection::stream)
                .collect(java.util.stream.Collectors.toList());
        assertEquals(List.of(1, 2, 3, 4, 5, 6), flat);
    }

    @Test
    void testGroupByFirstLetter() {
        List<String> names = List.of("Alice", "Bob", "Charlie", "Adam");
        Map<Character, List<String>> grouped = names.stream()
                .collect(java.util.stream.Collectors.groupingBy(name -> name.charAt(0)));
        assertEquals(List.of("Alice", "Adam"), grouped.get('A'));
        assertEquals(List.of("Bob"), grouped.get('B'));
    }

    @Test
    void testPartitionEvenOdd() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
                .collect(java.util.stream.Collectors.partitioningBy(n -> n % 2 == 0));
        assertEquals(List.of(2, 4, 6, 8, 10), partitioned.get(true));
        assertEquals(List.of(1, 3, 5, 7, 9), partitioned.get(false));
    }

    @Test
    void testJoining() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        String joined = names.stream()
                .collect(java.util.stream.Collectors.joining(", "));
        assertEquals("Alice, Bob, Charlie", joined);
    }

    @Test
    void testSum() {
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);
        int sum = numbers.stream().mapToInt(Integer::intValue).sum();
        assertEquals(150, sum);
    }

    @Test
    void testAverage() {
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);
        OptionalDouble avg = numbers.stream()
                .mapToInt(Integer::intValue)
                .average();
        assertTrue(avg.isPresent());
        assertEquals(30.0, avg.getAsDouble(), 0.001);
    }
}
