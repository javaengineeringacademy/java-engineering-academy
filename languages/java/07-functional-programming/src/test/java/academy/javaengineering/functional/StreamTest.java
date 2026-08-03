package academy.javaengineering.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Stream API Tests")
class StreamTest {

    @Nested
    @DisplayName("Stream Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create stream from collection")
        void shouldCreateFromCollection() {
            List<String> list = Arrays.asList("a", "b", "c");
            Stream<String> stream = list.stream();
            assertEquals(3, stream.count());
        }

        @Test
        @DisplayName("Should create stream from array")
        void shouldCreateFromArray() {
            int[] array = {1, 2, 3, 4, 5};
            IntStream stream = Arrays.stream(array);
            assertEquals(5, stream.count());
        }

        @Test
        @DisplayName("Should create stream from values")
        void shouldCreateFromValues() {
            Stream<String> stream = Stream.of("x", "y", "z");
            assertEquals(3, stream.count());
        }

        @Test
        @DisplayName("Should create stream with range")
        void shouldCreateWithRange() {
            List<Integer> list = IntStream.range(0, 5).boxed().toList();
            assertEquals(List.of(0, 1, 2, 3, 4), list);
        }

        @Test
        @DisplayName("Should create stream with rangeClosed")
        void shouldCreateWithRangeClosed() {
            List<Integer> list = IntStream.rangeClosed(1, 5).boxed().toList();
            assertEquals(List.of(1, 2, 3, 4, 5), list);
        }

        @Test
        @DisplayName("Should create stream with iterate")
        void shouldCreateWithIterate() {
            List<Integer> list = Stream.iterate(0, n -> n + 2)
                .limit(5)
                .toList();
            assertEquals(List.of(0, 2, 4, 6, 8), list);
        }
    }

    @Nested
    @DisplayName("Intermediate Operations Tests")
    class IntermediateTests {

        @Test
        @DisplayName("Should filter elements")
        void shouldFilterElements() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
            List<Integer> even = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toList();
            assertEquals(List.of(2, 4, 6), even);
        }

        @Test
        @DisplayName("Should map elements")
        void shouldMapElements() {
            List<String> names = Arrays.asList("alice", "bob");
            List<String> upper = names.stream()
                .map(String::toUpperCase)
                .toList();
            assertEquals(List.of("ALICE", "BOB"), upper);
        }

        @Test
        @DisplayName("Should flatMap elements")
        void shouldFlatMapElements() {
            List<List<Integer>> nested = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
            );
            List<Integer> flat = nested.stream()
                .flatMap(Collection::stream)
                .toList();
            assertEquals(List.of(1, 2, 3, 4), flat);
        }

        @Test
        @DisplayName("Should get distinct elements")
        void shouldGetDistinctElements() {
            List<Integer> duplicates = Arrays.asList(1, 2, 2, 3, 3, 3);
            List<Integer> unique = duplicates.stream()
                .distinct()
                .toList();
            assertEquals(List.of(1, 2, 3), unique);
        }

        @Test
        @DisplayName("Should sort elements")
        void shouldSortElements() {
            List<Integer> unsorted = Arrays.asList(5, 3, 1, 4, 2);
            List<Integer> sorted = unsorted.stream()
                .sorted()
                .toList();
            assertEquals(List.of(1, 2, 3, 4, 5), sorted);
        }

        @Test
        @DisplayName("Should limit elements")
        void shouldLimitElements() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            List<Integer> limited = numbers.stream()
                .limit(3)
                .toList();
            assertEquals(List.of(1, 2, 3), limited);
        }

        @Test
        @DisplayName("Should skip elements")
        void shouldSkipElements() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            List<Integer> skipped = numbers.stream()
                .skip(2)
                .toList();
            assertEquals(List.of(3, 4, 5), skipped);
        }
    }

    @Nested
    @DisplayName("Terminal Operations Tests")
    class TerminalTests {

        @Test
        @DisplayName("Should reduce to sum")
        void shouldReduceToSum() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            int sum = numbers.stream()
                .reduce(0, Integer::sum);
            assertEquals(15, sum);
        }

        @Test
        @DisplayName("Should count elements")
        void shouldCountElements() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            long count = numbers.stream()
                .filter(n -> n > 3)
                .count();
            assertEquals(2, count);
        }

        @Test
        @DisplayName("Should check anyMatch")
        void shouldCheckAnyMatch() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            boolean anyEven = numbers.stream()
                .anyMatch(n -> n % 2 == 0);
            assertTrue(anyEven);
        }

        @Test
        @DisplayName("Should check allMatch")
        void shouldCheckAllMatch() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            boolean allPositive = numbers.stream()
                .allMatch(n -> n > 0);
            assertTrue(allPositive);
        }

        @Test
        @DisplayName("Should check noneMatch")
        void shouldCheckNoneMatch() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            boolean noneNegative = numbers.stream()
                .noneMatch(n -> n < 0);
            assertTrue(noneNegative);
        }

        @Test
        @DisplayName("Should findFirst")
        void shouldFindFirst() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            Optional<Integer> first = numbers.stream()
                .filter(n -> n > 3)
                .findFirst();
            assertTrue(first.isPresent());
            assertEquals(4, first.get());
        }

        @Test
        @DisplayName("Should find min and max")
        void shouldFindMinAndMax() {
            List<Integer> numbers = Arrays.asList(5, 3, 1, 4, 2);
            Optional<Integer> min = numbers.stream()
                .min(Integer::compareTo);
            Optional<Integer> max = numbers.stream()
                .max(Integer::compareTo);
            assertEquals(1, min.orElse(-1));
            assertEquals(5, max.orElse(-1));
        }

        @Test
        @DisplayName("Should collect to list")
        void shouldCollectToList() {
            List<Integer> numbers = Arrays.asList(1, 2, 3);
            List<Integer> result = numbers.stream()
                .filter(n -> n > 1)
                .toList();
            assertEquals(List.of(2, 3), result);
        }
    }

    @Nested
    @DisplayName("Parallel Stream Tests")
    class ParallelTests {

        @Test
        @DisplayName("Should process parallel stream correctly")
        void shouldProcessParallelStream() {
            List<Integer> numbers = IntStream.rangeClosed(1, 100)
                .boxed()
                .toList();
            long sumSeq = numbers.stream()
                .mapToLong(Integer::longValue)
                .sum();
            long sumPar = numbers.parallelStream()
                .mapToLong(Integer::longValue)
                .sum();
            assertEquals(sumSeq, sumPar);
        }
    }
}
