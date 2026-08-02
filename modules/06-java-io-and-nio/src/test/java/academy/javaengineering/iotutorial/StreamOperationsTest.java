package academy.javaengineering.iotutorial;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Stream Operations Tests")
class StreamOperationsTest {

    @Nested
    @DisplayName("Stream Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create stream from collection")
        void shouldCreateFromCollection() {
            List<String> list = List.of("a", "b", "c");
            long count = list.stream().count();
            assertEquals(3, count);
        }

        @Test
        @DisplayName("Should create stream from array")
        void shouldCreateFromArray() {
            int[] array = {1, 2, 3, 4, 5};
            int sum = Arrays.stream(array).sum();
            assertEquals(15, sum);
        }

        @Test
        @DisplayName("Should create stream from values")
        void shouldCreateFromValues() {
            long count = Stream.of(10, 20, 30).count();
            assertEquals(3, count);
        }

        @Test
        @DisplayName("Should create stream from range")
        void shouldCreateFromRange() {
            int sum = IntStream.rangeClosed(1, 10).sum();
            assertEquals(55, sum);
        }
    }

    @Nested
    @DisplayName("Intermediate Operations Tests")
    class IntermediateTests {

        @Test
        @DisplayName("Should filter elements")
        void shouldFilter() {
            List<Integer> result = List.of(1, 2, 3, 4, 5).stream()
                .filter(n -> n > 3)
                .toList();
            assertEquals(List.of(4, 5), result);
        }

        @Test
        @DisplayName("Should map elements")
        void shouldMap() {
            List<String> result = List.of("a", "b", "c").stream()
                .map(String::toUpperCase)
                .toList();
            assertEquals(List.of("A", "B", "C"), result);
        }

        @Test
        @DisplayName("Should flatMap elements")
        void shouldFlatMap() {
            List<String> sentences = List.of("hello world", "java streams");
            List<String> words = sentences.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .toList();
            assertEquals(4, words.size());
        }

        @Test
        @DisplayName("Should sort elements")
        void shouldSort() {
            List<Integer> result = List.of(3, 1, 4, 1, 5).stream()
                .sorted()
                .toList();
            assertEquals(List.of(1, 1, 3, 4, 5), result);
        }

        @Test
        @DisplayName("Should remove duplicates")
        void shouldDistinct() {
            List<Integer> result = List.of(1, 2, 2, 3, 3, 3).stream()
                .distinct()
                .toList();
            assertEquals(List.of(1, 2, 3), result);
        }

        @Test
        @DisplayName("Should limit elements")
        void shouldLimit() {
            List<Integer> result = IntStream.range(1, 100).boxed()
                .limit(5)
                .toList();
            assertEquals(List.of(1, 2, 3, 4, 5), result);
        }

        @Test
        @DisplayName("Should skip elements")
        void shouldSkip() {
            List<Integer> result = List.of(1, 2, 3, 4, 5).stream()
                .skip(2)
                .toList();
            assertEquals(List.of(3, 4, 5), result);
        }
    }

    @Nested
    @DisplayName("Terminal Operations Tests")
    class TerminalTests {

        @Test
        @DisplayName("Should count elements")
        void shouldCount() {
            long count = List.of(1, 2, 3).stream().count();
            assertEquals(3, count);
        }

        @Test
        @DisplayName("Should reduce to sum")
        void shouldReduce() {
            Optional<Integer> sum = List.of(1, 2, 3, 4, 5).stream()
                .reduce(Integer::sum);
            assertTrue(sum.isPresent());
            assertEquals(15, sum.get());
        }

        @Test
        @DisplayName("Should find first")
        void shouldFindFirst() {
            Optional<Integer> first = List.of(10, 20, 30).stream()
                .findFirst();
            assertTrue(first.isPresent());
            assertEquals(10, first.get());
        }

        @Test
        @DisplayName("Should check anyMatch")
        void shouldAnyMatch() {
            boolean has = List.of(1, 2, 3).stream()
                .anyMatch(n -> n > 2);
            assertTrue(has);
        }

        @Test
        @DisplayName("Should check allMatch")
        void shouldAllMatch() {
            boolean all = List.of(2, 4, 6).stream()
                .allMatch(n -> n % 2 == 0);
            assertTrue(all);
        }

        @Test
        @DisplayName("Should check noneMatch")
        void shouldNoneMatch() {
            boolean none = List.of(1, 3, 5).stream()
                .noneMatch(n -> n % 2 == 0);
            assertTrue(none);
        }

        @Test
        @DisplayName("Should find min and max")
        void shouldFindMinMax() {
            List<Integer> numbers = List.of(5, 2, 8, 1, 9);
            Optional<Integer> min = numbers.stream().min(Integer::compareTo);
            Optional<Integer> max = numbers.stream().max(Integer::compareTo);
            assertEquals(1, min.get());
            assertEquals(9, max.get());
        }
    }

    @Nested
    @DisplayName("Collector Tests")
    class CollectorTests {

        @Test
        @DisplayName("Should collect to list")
        void shouldCollectToList() {
            List<String> result = Stream.of("a", "b", "c")
                .collect(Collectors.toList());
            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("Should group by")
        void shouldGroupBy() {
            List<String> words = List.of("apple", "banana", "avocado", "blueberry");
            Map<Character, List<String>> grouped = words.stream()
                .collect(Collectors.groupingBy(w -> w.charAt(0)));
            assertEquals(2, grouped.size());
            assertEquals(2, grouped.get('a').size());
        }

        @Test
        @DisplayName("Should partition")
        void shouldPartition() {
            Map<Boolean, List<Integer>> partitioned = List.of(1, 2, 3, 4, 5).stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
            assertEquals(2, partitioned.get(true).size());
            assertEquals(3, partitioned.get(false).size());
        }

        @Test
        @DisplayName("Should join strings")
        void shouldJoin() {
            String result = Stream.of("a", "b", "c")
                .collect(Collectors.joining(", "));
            assertEquals("a, b, c", result);
        }

        @Test
        @DisplayName("Should compute statistics")
        void shouldComputeStats() {
            DoubleSummaryStatistics stats = List.of(10.0, 20.0, 30.0).stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
            assertEquals(3, stats.getCount());
            assertEquals(20.0, stats.getAverage());
            assertEquals(60.0, stats.getSum());
        }
    }

    @Nested
    @DisplayName("Parallel Stream Tests")
    class ParallelTests {

        @Test
        @DisplayName("Should process in parallel")
        void shouldProcessParallel() {
            long count = IntStream.range(0, 10000).parallel()
                .filter(n -> n % 2 == 0)
                .count();
            assertEquals(5000, count);
        }

        @Test
        @DisplayName("Should parallel reduce")
        void shouldParallelReduce() {
            int sum = IntStream.rangeClosed(1, 1000).parallel()
                .sum();
            assertEquals(500500, sum);
        }

        @Test
        @DisplayName("Should parallel grouping")
        void shouldParallelGrouping() {
            Map<String, Long> counts = IntStream.range(0, 100).parallel()
                .boxed()
                .collect(Collectors.groupingConcurrent(
                    n -> n % 2 == 0 ? "even" : "odd",
                    Collectors.counting()
                ));
            assertEquals(50L, counts.get("even"));
            assertEquals(50L, counts.get("odd"));
        }
    }
}
