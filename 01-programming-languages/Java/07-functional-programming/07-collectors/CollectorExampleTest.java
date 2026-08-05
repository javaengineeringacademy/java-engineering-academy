import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Collector Tests")
class CollectorExampleTest {

    @Nested
    @DisplayName("Basic Collector Tests")
    class BasicCollectorTests {

        @Test
        @DisplayName("Should collect to list")
        void shouldCollectToList() {
            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
            List<String> filtered = names.stream()
                .filter(n -> n.length() > 3)
                .collect(Collectors.toList());
            assertEquals(2, filtered.size());
            assertTrue(filtered.contains("Alice"));
            assertTrue(filtered.contains("Charlie"));
        }

        @Test
        @DisplayName("Should collect to set")
        void shouldCollectToSet() {
            List<String> names = Arrays.asList("Alice", "Bob", "alice");
            Set<String> lowerCase = names.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
            assertEquals(2, lowerCase.size());
            assertTrue(lowerCase.contains("alice"));
            assertTrue(lowerCase.contains("bob"));
        }

        @Test
        @DisplayName("Should collect to map")
        void shouldCollectToMap() {
            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
            Map<String, Integer> nameLengths = names.stream()
                .collect(Collectors.toMap(
                    name -> name,
                    String::length
                ));
            assertEquals(3, nameLengths.size());
            assertEquals(5, nameLengths.get("Alice"));
            assertEquals(3, nameLengths.get("Bob"));
            assertEquals(7, nameLengths.get("Charlie"));
        }

        @Test
        @DisplayName("Should join strings")
        void shouldJoinStrings() {
            List<String> names = Arrays.asList("A", "B", "C");
            String joined = names.stream()
                .collect(Collectors.joining(", "));
            assertEquals("A, B, C", joined);
        }

        @Test
        @DisplayName("Should join with prefix and suffix")
        void shouldJoinWithPrefixSuffix() {
            List<String> names = Arrays.asList("A", "B", "C");
            String joined = names.stream()
                .collect(Collectors.joining(", ", "[", "]"));
            assertEquals("[A, B, C]", joined);
        }
    }

    @Nested
    @DisplayName("Grouping Collector Tests")
    class GroupingTests {

        @Test
        @DisplayName("Should group by classifier")
        void shouldGroupByClassifier() {
            List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "Diana", "Eve");
            Map<Character, List<String>> byFirstLetter = names.stream()
                .collect(Collectors.groupingBy(
                    name -> name.charAt(0)));
            assertEquals(5, byFirstLetter.size());
            assertTrue(byFirstLetter.containsKey('A'));
            assertEquals(1, byFirstLetter.get('A').size());
        }

        @Test
        @DisplayName("Should group with downstream counting")
        void shouldGroupWithDownstreamCounting() {
            List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "Diana", "Eve");
            Map<Character, Long> countByLetter = names.stream()
                .collect(Collectors.groupingBy(
                    name -> name.charAt(0),
                    Collectors.counting()));
            assertEquals(1L, countByLetter.get('A'));
            assertEquals(1L, countByLetter.get('D'));
        }

        @Test
        @DisplayName("Should partition by predicate")
        void shouldPartitionByPredicate() {
            List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "Diana", "Eve");
            Map<Boolean, List<String>> partitioned = names.stream()
                .collect(Collectors.partitioningBy(
                    name -> name.length() > 3));
            assertEquals(3, partitioned.get(true).size());
            assertEquals(2, partitioned.get(false).size());
        }

        @Test
        @DisplayName("Should group by string length")
        void shouldGroupByStringLength() {
            List<String> names = Arrays.asList("A", "BB", "CCC", "DD");
            Map<Integer, List<String>> byLength = names.stream()
                .collect(Collectors.groupingBy(String::length));
            assertEquals(3, byLength.size());
            assertEquals(2, byLength.get(2).size());
        }
    }

    @Nested
    @DisplayName("Downstream Collector Tests")
    class DownstreamTests {

        @Test
        @DisplayName("Should collect names by length")
        void shouldCollectNamesByLength() {
            List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "Diana", "Eve");
            Map<Integer, List<String>> namesByLength = names.stream()
                .collect(Collectors.groupingBy(
                    String::length,
                    Collectors.mapping(name -> name,
                        Collectors.toList())));
            assertEquals(2, namesByLength.get(3).size());
        }

        @Test
        @DisplayName("Should compute averaging double")
        void shouldComputeAveragingDouble() {
            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
            Map<Integer, Double> avgByLength = names.stream()
                .collect(Collectors.groupingBy(
                    String::length,
                    Collectors.averagingDouble(String::length)));
            assertEquals(5.0, avgByLength.get(5), 0.001);
        }

        @Test
        @DisplayName("Should summarize integers")
        void shouldSummarizeIntegers() {
            List<String> names = Arrays.asList("A", "BB", "CCC");
            IntSummaryStatistics stats = names.stream()
                .collect(Collectors.summarizingInt(String::length));
            assertEquals(3, stats.getCount());
            assertEquals(1, stats.getMin());
            assertEquals(3, stats.getMax());
            assertEquals(6, stats.getSum());
        }
    }

    @Nested
    @DisplayName("Custom Collector Tests")
    class CustomCollectorTests {

        @Test
        @DisplayName("Should create custom sorted list collector")
        void shouldCreateCustomSortedListCollector() {
            Collector<Integer, List<Integer>, List<Integer>> toSortedList = Collector.of(
                () -> new ArrayList<>(),
                List::add,
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                },
                list -> {
                    list.sort(Comparator.naturalOrder());
                    return Collections.unmodifiableList(list);
                }
            );

            List<Integer> numbers = Arrays.asList(5, 3, 1, 4, 2);
            List<Integer> sorted = numbers.stream()
                .collect(toSortedList);
            assertEquals(List.of(1, 2, 3, 4, 5), sorted);
        }

        @Test
        @DisplayName("Should create custom joining collector")
        void shouldCreateCustomJoiningCollector() {
            Collector<String, ?, String> joiningWithLimit = Collector.of(
                StringBuilder::new,
                (sb, s) -> {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(s);
                },
                (sb1, sb2) -> {
                    if (sb1.length() > 0 && sb2.length() > 0) {
                        sb1.append(", ");
                    }
                    sb1.append(sb2);
                    return sb1;
                },
                StringBuilder::toString
            );

            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
            String result = names.stream()
                .collect(joiningWithLimit);
            assertEquals("Alice, Bob, Charlie", result);
        }

        @Test
        @DisplayName("Should collect with custom reducer")
        void shouldCollectWithCustomReducer() {
            Collector<String, ?, String> join = Collector.of(
                StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append,
                StringBuilder::toString
            );

            List<String> words = Arrays.asList("Hello", " ", "World");
            String result = words.stream().collect(join);
            assertEquals("Hello World", result);
        }
    }
}
