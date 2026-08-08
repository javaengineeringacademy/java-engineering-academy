package academy.javaengineering.collections.examples;

import java.util.*;
import java.util.stream.*;

public class StreamExamples {
    public static void main(String[] args) {
        System.out.println("=== Stream Examples ===\n");

        // Create stream
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Filter
        System.out.println("--- Filter ---");
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Evens: " + evens);

        // Map
        System.out.println("\n--- Map ---");
        List<Integer> squared = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("Squared: " + squared);

        // Reduce
        System.out.println("\n--- Reduce ---");
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // Collect
        System.out.println("\n--- Collect ---");
        String joined = numbers.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
        System.out.println("Joined: " + joined);

        // Grouping
        System.out.println("\n--- Grouping ---");
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Evens: " + partitioned.get(true));
        System.out.println("Odds: " + partitioned.get(false));

        // Grouping by value
        Map<String, List<String>> words = Stream.of("apple", "banana", "avocado", "blueberry", "cherry")
            .collect(Collectors.groupingBy(w -> w.substring(0, 1)));
        System.out.println("By first letter: " + words);

        // Sorting
        System.out.println("\n--- Sorting ---");
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob", "David");
        List<String> sorted = names.stream()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Sorted: " + sorted);

        // Statistics
        System.out.println("\n--- Statistics ---");
        IntSummaryStatistics stats = numbers.stream()
            .mapToInt(Integer::intValue)
            .summaryStatistics();
        System.out.println("Count: " + stats.getCount());
        System.out.println("Sum: " + stats.getSum());
        System.out.println("Min: " + stats.getMin());
        System.out.println("Max: " + stats.getMax());
        System.out.println("Avg: " + stats.getAverage());

        // Parallel
        System.out.println("\n--- Parallel ---");
        long count = numbers.parallelStream()
            .filter(n -> n > 5)
            .count();
        System.out.println("Numbers > 5: " + count);

        // FlatMap
        System.out.println("\n--- FlatMap ---");
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2),
            Arrays.asList(3, 4),
            Arrays.asList(5, 6)
        );
        List<Integer> flat = nested.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        System.out.println("Flattened: " + flat);

        // Optional
        System.out.println("\n--- Optional ---");
        Optional<Integer> first = numbers.stream()
            .filter(n -> n > 100)
            .findFirst();
        System.out.println("First > 100: " + first.orElse(-1));

        Optional<Integer> max = numbers.stream()
            .max(Integer::compareTo);
        System.out.println("Max: " + max.orElse(-1));
    }
}
