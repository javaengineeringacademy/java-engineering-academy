package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CollectionOperationsTest {

    private List<Integer> numbers;
    private List<String> names;

    @BeforeEach
    void setUp() {
        numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana", "Eve"));
    }

    @Nested
    @DisplayName("Filter Tests")
    class FilterTests {

        @Test
        @DisplayName("Should filter even numbers")
        void testFilterEven() {
            List<Integer> even = numbers.stream()
                    .filter(n -> n % 2 == 0)
                    .collect(Collectors.toList());
            assertEquals(List.of(2, 4, 6, 8, 10), even);
        }

        @Test
        @DisplayName("Should filter by string length")
        void testFilterByLength() {
            List<String> longNames = names.stream()
                    .filter(name -> name.length() > 4)
                    .collect(Collectors.toList());
            assertEquals(List.of("Alice", "Charlie", "Diana"), longNames);
        }

        @Test
        @DisplayName("Should filter non-null elements")
        void testFilterNonNull() {
            List<String> mixed = List.of("Alice", null, "Bob", null, "Charlie");
            List<String> nonNull = mixed.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            assertEquals(List.of("Alice", "Bob", "Charlie"), nonNull);
        }
    }

    @Nested
    @DisplayName("Map Tests")
    class MapTests {

        @Test
        @DisplayName("Should map to uppercase")
        void testMapToUpperCase() {
            List<String> upper = names.stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            assertEquals(List.of("ALICE", "BOB", "CHARLIE", "DIANA", "EVE"), upper);
        }

        @Test
        @DisplayName("Should map to lengths")
        void testMapToLengths() {
            List<Integer> lengths = names.stream()
                    .map(String::length)
                    .collect(Collectors.toList());
            assertEquals(List.of(5, 3, 7, 5, 3), lengths);
        }
    }

    @Nested
    @DisplayName("Reduce Tests")
    class ReduceTests {

        @Test
        @DisplayName("Should sum numbers")
        void testReduceSum() {
            int sum = numbers.stream().reduce(0, Integer::sum);
            assertEquals(55, sum);
        }

        @Test
        @DisplayName("Should compute product")
        void testReduceProduct() {
            List<Integer> small = List.of(1, 2, 3, 4);
            int product = small.stream().reduce(1, (a, b) -> a * b);
            assertEquals(24, product);
        }

        @Test
        @DisplayName("Should find max")
        void testReduceMax() {
            Optional<Integer> max = numbers.stream().reduce(Integer::max);
            assertTrue(max.isPresent());
            assertEquals(10, max.get());
        }

        @Test
        @DisplayName("Should find min")
        void testReduceMin() {
            Optional<Integer> min = numbers.stream().reduce(Integer::min);
            assertTrue(min.isPresent());
            assertEquals(1, min.get());
        }
    }

    @Nested
    @DisplayName("Collect Tests")
    class CollectTests {

        @Test
        @DisplayName("Should collect to list")
        void testCollectToList() {
            List<Integer> result = numbers.stream()
                    .filter(n -> n > 5)
                    .collect(Collectors.toList());
            assertEquals(List.of(6, 7, 8, 9, 10), result);
        }

        @Test
        @DisplayName("Should collect to set")
        void testCollectToSet() {
            List<String> duplicates = List.of("A", "B", "A", "C", "B");
            Set<String> unique = duplicates.stream()
                    .collect(Collectors.toSet());
            assertEquals(3, unique.size());
            assertTrue(unique.contains("A"));
            assertTrue(unique.contains("B"));
            assertTrue(unique.contains("C"));
        }

        @Test
        @DisplayName("Should collect to map")
        void testCollectToMap() {
            Map<String, Integer> nameLengths = names.stream()
                    .collect(Collectors.toMap(
                            name -> name,
                            String::length
                    ));
            assertEquals(5, nameLengths.get("Alice"));
            assertEquals(3, nameLengths.get("Bob"));
            assertEquals(7, nameLengths.get("Charlie"));
        }

        @Test
        @DisplayName("Should join strings")
        void testCollectJoining() {
            String joined = names.stream()
                    .collect(Collectors.joining(", "));
            assertEquals("Alice, Bob, Charlie, Diana, Eve", joined);
        }
    }

    @Nested
    @DisplayName("Find Tests")
    class FindTests {

        @Test
        @DisplayName("Should find first matching element")
        void testFindFirst() {
            Optional<String> first = names.stream()
                    .filter(name -> name.length() > 4)
                    .findFirst();
            assertTrue(first.isPresent());
            assertEquals("Alice", first.get());
        }

        @Test
        @DisplayName("Should return empty Optional when not found")
        void testFindFirstEmpty() {
            Optional<String> result = names.stream()
                    .filter(name -> name.startsWith("Z"))
                    .findFirst();
            assertFalse(result.isPresent());
        }

        @Test
        @DisplayName("Should find any matching element")
        void testFindAny() {
            Optional<String> any = names.stream()
                    .filter(name -> name.startsWith("D"))
                    .findAny();
            assertTrue(any.isPresent());
            assertEquals("Diana", any.get());
        }
    }

    @Nested
    @DisplayName("Match Tests")
    class MatchTests {

        @Test
        @DisplayName("Should check anyMatch")
        void testAnyMatch() {
            assertTrue(numbers.stream().anyMatch(n -> n == 5));
            assertFalse(numbers.stream().anyMatch(n -> n == 15));
        }

        @Test
        @DisplayName("Should check allMatch")
        void testAllMatch() {
            assertTrue(numbers.stream().allMatch(n -> n > 0));
            assertFalse(numbers.stream().allMatch(n -> n > 5));
        }

        @Test
        @DisplayName("Should check noneMatch")
        void testNoneMatch() {
            assertTrue(numbers.stream().noneMatch(n -> n < 0));
            assertFalse(numbers.stream().noneMatch(n -> n == 5));
        }
    }

    @Nested
    @DisplayName("FlatMap Tests")
    class FlatMapTests {

        @Test
        @DisplayName("Should flatten nested lists")
        void testFlatMapLists() {
            List<List<Integer>> nested = List.of(
                    List.of(1, 2),
                    List.of(3, 4),
                    List.of(5, 6)
            );
            List<Integer> flat = nested.stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            assertEquals(List.of(1, 2, 3, 4, 5, 6), flat);
        }

        @Test
        @DisplayName("Should flatten words to characters")
        void testFlatMapCharacters() {
            List<String> words = List.of("Hi", "Go");
            List<Character> chars = words.stream()
                    .flatMap(word -> word.chars().mapToObj(c -> (char) c))
                    .collect(Collectors.toList());
            assertEquals(List.of('H', 'i', 'G', 'o'), chars);
        }
    }

    @Nested
    @DisplayName("GroupingBy Tests")
    class GroupingByTests {

        @Test
        @DisplayName("Should group by first letter")
        void testGroupByFirstLetter() {
            Map<Character, List<String>> grouped = names.stream()
                    .collect(Collectors.groupingBy(name -> name.charAt(0)));
            assertEquals(3, grouped.size());
            assertEquals(List.of("Alice", "Adam"), grouped.get('A'));
            assertEquals(List.of("Bob"), grouped.get('B'));
        }

        @Test
        @DisplayName("Should group by length with counting")
        void testGroupByLengthCounting() {
            Map<Integer, Long> counts = names.stream()
                    .collect(Collectors.groupingBy(
                            String::length,
                            Collectors.counting()
                    ));
            assertEquals(2L, counts.get(3));
            assertEquals(2L, counts.get(5));
            assertEquals(1L, counts.get(7));
        }
    }

    @Nested
    @DisplayName("PartitioningBy Tests")
    class PartitioningByTests {

        @Test
        @DisplayName("Should partition even and odd")
        void testPartitionEvenOdd() {
            Map<Boolean, List<Integer>> partitioned = numbers.stream()
                    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
            assertEquals(List.of(2, 4, 6, 8, 10), partitioned.get(true));
            assertEquals(List.of(1, 3, 5, 7, 9), partitioned.get(false));
        }

        @Test
        @DisplayName("Should partition by string length")
        void testPartitionByLength() {
            Map<Boolean, List<String>> partitioned = names.stream()
                    .collect(Collectors.partitioningBy(name -> name.length() > 3));
            assertEquals(3, partitioned.get(true).size());
            assertEquals(2, partitioned.get(false).size());
        }
    }

    @Nested
    @DisplayName("Numeric Operations Tests")
    class NumericTests {

        @Test
        @DisplayName("Should compute sum via mapToInt")
        void testSum() {
            int sum = numbers.stream().mapToInt(Integer::intValue).sum();
            assertEquals(55, sum);
        }

        @Test
        @DisplayName("Should compute average")
        void testAverage() {
            OptionalDouble avg = numbers.stream()
                    .mapToInt(Integer::intValue)
                    .average();
            assertTrue(avg.isPresent());
            assertEquals(5.5, avg.getAsDouble(), 0.001);
        }

        @Test
        @DisplayName("Should compute summary statistics")
        void testSummaryStatistics() {
            IntSummaryStatistics stats = numbers.stream()
                    .mapToInt(Integer::intValue)
                    .summaryStatistics();
            assertEquals(10, stats.getCount());
            assertEquals(55, stats.getSum());
            assertEquals(1, stats.getMin());
            assertEquals(10, stats.getMax());
            assertEquals(5.5, stats.getAverage(), 0.001);
        }
    }
}
