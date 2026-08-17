package academy.javaengineering.oop.practices;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Practice: Java Streams API (IO/NIO context)
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating streams from various sources
 * - Intermediate and terminal operations
 * - Parallel streams for performance
 * - Collectors for aggregation
 * - Stream pipeline building
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 08-streams-api ===\n");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace");

        // Test Exercise 1: filterAndCount
        long count = filterAndCount(names, 4);
        System.out.println("Exercise 1 - filterAndCount: "
            + (count == 5 ? "PASS" : "FAIL (expected 5, got " + count + ")"));

        // Test Exercise 2: stringStats
        Map<String, Integer> stats = stringStats(names);
        System.out.println("Exercise 2 - stringStats: "
            + (Integer.valueOf(7).equals(stats.get("count"))
            && Integer.valueOf(5).equals(stats.get("min"))
            && Integer.valueOf(7).equals(stats.get("max")) ? "PASS" : "FAIL"));

        // Test Exercise 3: parallelSum
        long sum = parallelSum(100);
        System.out.println("Exercise 3 - parallelSum: "
            + (sum == 5050 ? "PASS" : "FAIL (expected 5050, got " + sum + ")"));

        // Test Exercise 4: groupByFirstChar
        Map<Character, List<String>> grouped = groupByFirstChar(names);
        System.out.println("Exercise 4 - groupByFirstChar: "
            + (grouped.get('A') != null && grouped.get('A').contains("Alice") ? "PASS" : "FAIL"));

        // Test Exercise 5: flattenAndSort
        List<String> nested = Arrays.asList("hello world", "java streams");
        List<String> flat = flattenAndSort(nested);
        System.out.println("Exercise 5 - flattenAndSort: "
            + (flat.contains("hello") && flat.contains("world") && flat.contains("java") ? "PASS" : "FAIL"));
    }

    // TODO 1: Count strings longer than minLength using stream
    // Use stream().filter().count()
    static long filterAndCount(List<String> strings, int minLength) {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 2: Get basic statistics about string lengths
    // Return map with keys: "count", "min", "max"
    // Use IntStream with mapToInt(String::length)
    static Map<String, Integer> stringStats(List<String> strings) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Sum numbers from 1 to n using parallel stream
    // Use IntStream.rangeClosed(1, n).parallel().sum()
    static long parallelSum(int n) {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 4: Group strings by their first character
    // Use Collectors.groupingBy(s -> s.charAt(0))
    static Map<Character, List<String>> groupByFirstChar(List<String> strings) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Split each string by space, flatten all words, sort alphabetically
    // Use flatMap and sorted
    static List<String> flattenAndSort(List<String> sentences) {
        // YOUR CODE HERE
        return null;
    }
}
