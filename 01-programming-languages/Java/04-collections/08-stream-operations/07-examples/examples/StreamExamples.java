package academy.javaengineering.collections.streamoperations.examples;

import java.util.*;
import java.util.stream.*;

public class StreamExamples {
    public static void main(String[] args) {
        System.out.println("=== 10 Stream Examples ===\n");

        // Example 1: Filter and collect
        System.out.println("--- Example 1: Filter ---");
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evens = nums.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
        System.out.println("Evens: " + evens);

        // Example 2: Map transformation
        System.out.println("\n--- Example 2: Map ---");
        List<String> words = Arrays.asList("hello", "world", "java");
        List<String> upper = words.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println("Upper: " + upper);

        // Example 3: Reduce
        System.out.println("\n--- Example 3: Reduce ---");
        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // Example 4: Collect to Map
        System.out.println("\n--- Example 4: Collect to Map ---");
        Map<String, Integer> nameLengths = words.stream()
            .collect(Collectors.toMap(w -> w, String::length));
        System.out.println("Name lengths: " + nameLengths);

        // Example 5: Grouping
        System.out.println("\n--- Example 5: Grouping ---");
        Map<Boolean, List<Integer>> partitioned = nums.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Partitioned: " + partitioned);

        // Example 6: Statistics
        System.out.println("\n--- Example 6: Statistics ---");
        IntSummaryStatistics stats = nums.stream().mapToInt(Integer::intValue).summaryStatistics();
        System.out.println("Stats: avg=" + stats.getAverage() + ", min=" + stats.getMin() + ", max=" + stats.getMax());

        // Example 7: Joining
        System.out.println("\n--- Example 7: Joining ---");
        String joined = words.stream().collect(Collectors.joining(", "));
        System.out.println("Joined: " + joined);

        // Example 8: FlatMap
        System.out.println("\n--- Example 8: FlatMap ---");
        List<List<Integer>> nested = Arrays.asList(Arrays.asList(1,2), Arrays.asList(3,4));
        List<Integer> flat = nested.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println("Flat: " + flat);

        // Example 9: Distinct and sorted
        System.out.println("\n--- Example 9: Distinct + Sorted ---");
        List<Integer> dups = Arrays.asList(3, 1, 2, 1, 3, 4, 2);
        List<Integer> unique = dups.stream().distinct().sorted().collect(Collectors.toList());
        System.out.println("Unique sorted: " + unique);

        // Example 10: Parallel
        System.out.println("\n--- Example 10: Parallel ---");
        long count = nums.parallelStream().filter(n -> n > 5).count();
        System.out.println("Count > 5: " + count);
    }
}
