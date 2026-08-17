package academy.javaengineering.oop.practices;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Practice: Stream API in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating streams from collections, arrays, and ranges
 * - Intermediate operations: filter, map, sorted, distinct, limit, skip
 * - Terminal operations: collect, reduce, forEach, count, findFirst
 * - Understanding lazy evaluation
 * - Building efficient stream pipelines
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 05-stream-api ===\n");

        // Test Exercise 1: filterAndSort
        List<String> result1 = filterAndSort(Arrays.asList("banana", "apple", "cherry", "date", "avocado"), 5);
        System.out.println("Exercise 1 - filterAndSort: "
            + (result1.size() == 3 && "banana".equals(result1.get(0)) ? "PASS" : "FAIL"));

        // Test Exercise 2: sumOfSquares
        int sum = sumOfSquares(Arrays.asList(1, 2, 3, 4));
        System.out.println("Exercise 2 - sumOfSquares: "
            + (sum == 30 ? "PASS" : "FAIL (expected 30, got " + sum + ")"));

        // Test Exercise 3: wordFrequency
        Map<String, Long> freq = wordFrequency("the cat sat on the mat the cat");
        System.out.println("Exercise 3 - wordFrequency: "
            + (freq.getOrDefault("the", 0L) == 3L && freq.getOrDefault("cat", 0L) == 2L ? "PASS" : "FAIL"));

        // Test Exercise 4: firstNPrimes
        List<Integer> primes = firstNPrimes(5);
        System.out.println("Exercise 4 - firstNPrimes: "
            + (primes.size() == 5 && primes.containsAll(Arrays.asList(2, 3, 5, 7, 11)) ? "PASS" : "FAIL"));

        // Test Exercise 5: partitionByLength
        Map<Boolean, List<String>> partitioned = partitionByLength(Arrays.asList("hi", "hello", "hey", "world"), 3);
        System.out.println("Exercise 5 - partitionByLength: "
            + (partitioned.get(true).contains("hello") && partitioned.get(true).contains("world")
            && partitioned.get(false).contains("hi") && partitioned.get(false).contains("hey") ? "PASS" : "FAIL"));
    }

    // TODO 1: Filter strings longer than minLength, then sort alphabetically
    // Use stream().filter().sorted().collect(Collectors.toList())
    static List<String> filterAndSort(List<String> strings, int minLength) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Calculate the sum of squares of all numbers in the list
    // Use stream().mapToInt().sum() or .reduce()
    static int sumOfSquares(List<Integer> numbers) {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 3: Count word frequencies in a space-separated string
    // Split the string, group by word, count occurrences
    // Return Map<String, Long>
    static Map<String, Long> wordFrequency(String text) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 4: Find the first N prime numbers using a stream
    // Hint: Use IntStream.range(2, ...) with filter and limit
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    static List<Integer> firstNPrimes(int n) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Partition strings by whether their length is >= minLength
    // true key = length >= minLength, false key = length < minLength
    // Use Collectors.partitioningBy()
    static Map<Boolean, List<String>> partitionByLength(List<String> strings, int minLength) {
        // YOUR CODE HERE
        return null;
    }
}
