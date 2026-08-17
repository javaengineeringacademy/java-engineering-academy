package academy.javaengineering.oop.practices;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Practice: Collectors in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - toList, toSet, toMap collectors
 * - groupingBy with downstream collectors
 * - partitioningBy
 * - joining collector
 * - summarizingInt for statistics
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 07-collectors ===\n");

        List<String> words = Arrays.asList("apple", "banana", "avocado", "blueberry", "cherry", "apricot");

        // Test Exercise 1: groupByFirstLetter
        Map<Character, List<String>> grouped = groupByFirstLetter(words);
        System.out.println("Exercise 1 - groupByFirstLetter: "
            + (grouped.get('a').size() == 3 && grouped.get('b').size() == 2 ? "PASS" : "FAIL"));

        // Test Exercise 2: joinWithDelimiter
        String joined = joinWithDelimiter(words, ", ");
        System.out.println("Exercise 2 - joinWithDelimiter: "
            + (joined.contains("apple") && joined.contains(", ") ? "PASS" : "FAIL"));

        // Test Exercise 3: countByLength
        Map<Integer, Long> lengthCounts = countByLength(words);
        System.out.println("Exercise 3 - countByLength: "
            + (lengthCounts.getOrDefault(5, 0L) >= 1 ? "PASS" : "FAIL"));

        // Test Exercise 4: summarizeLengths
        String stats = summarizeLengths(words);
        System.out.println("Exercise 4 - summarizeLengths: "
            + (stats != null && stats.contains("count") && stats.contains("min") ? "PASS" : "FAIL"));

        // Test Exercise 5: toMap - word to its length
        Map<String, Integer> wordToLength = toWordLengthMap(words);
        System.out.println("Exercise 5 - toMap: "
            + (Integer.valueOf(5).equals(wordToLength.get("apple")) && Integer.valueOf(6).equals(wordToLength.get("banana")) ? "PASS" : "FAIL"));
    }

    // TODO 1: Group words by their first letter
    // Use Collectors.groupingBy() with a lambda or method reference
    static Map<Character, List<String>> groupByFirstLetter(List<String> words) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Join all words with a delimiter
    // Use Collectors.joining(delimiter)
    static String joinWithDelimiter(List<String> words, String delimiter) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Count how many words have each length
    // Use Collectors.groupingBy() with Collectors.counting()
    static Map<Integer, Long> countByLength(List<String> words) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 4: Get summarizing statistics on word lengths
    // Use Collectors.summarizingInt(String::length) and return toString()
    static String summarizeLengths(List<String> words) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 4: Create a map from word to its length
    // Use Collectors.toMap(word -> word, word -> word.length())
    static Map<String, Integer> toWordLengthMap(List<String> words) {
        // YOUR CODE HERE
        return null;
    }
}
