package academy.javaengineering.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Demonstrates HashMap operations, internals, and best practices.
 * HashMap provides O(1) average time for get/put/remove operations.
 */
public class HashMapDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateAdvancedOperations();
        demonstrateCustomKeys();
        demonstratePerformancePatterns();
    }

    /**
     * Demonstrates basic HashMap operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== HashMap Basic Operations ===");

        // Creation
        Map<String, Integer> scores = new HashMap<>();
        Map<String, Integer> withCapacity = new HashMap<>(100);

        // Adding entries
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        scores.putIfAbsent("Diana", 88);
        scores.put("Alice", 96); // Replaces existing

        System.out.println("Map: " + scores);
        System.out.println("Size: " + scores.size());

        // Accessing values
        System.out.println("Alice's score: " + scores.get("Alice"));
        System.out.println("Unknown: " + scores.getOrDefault("Unknown", 0));

        // Checking existence
        System.out.println("Contains Bob: " + scores.containsKey("Bob"));
        System.out.println("Contains 95: " + scores.containsValue(95));

        // Removing entries
        scores.remove("Charlie");
        scores.remove("Diana", 88); // Conditional remove
        System.out.println("After removals: " + scores);

        // Replacing values
        scores.replace("Bob", 90);
        scores.replace("Alice", 96, 100); // Conditional replace

        // Iterating
        System.out.println("All entries:");
        scores.forEach((name, score) ->
                System.out.println("  " + name + ": " + score)
        );
        System.out.println();
    }

    /**
     * Demonstrates advanced HashMap operations.
     */
    private static void demonstrateAdvancedOperations() {
        System.out.println("=== Advanced Operations ===");

        // Word frequency counter
        String text = "the quick brown fox jumps over the lazy dog the fox";
        Map<String, Integer> frequency = wordFrequency(text);
        System.out.println("Word frequency: " + frequency);

        // Group by first letter
        Map<Character, java.util.List<String>> grouped = groupByFirstLetter(
                java.util.List.of("apple", "avocado", "banana", "blueberry", "cherry")
        );
        System.out.println("Grouped by first letter:");
        grouped.forEach((letter, words) ->
                System.out.println("  " + letter + ": " + words)
        );

        // Invert map
        Map<String, Integer> original = Map.of("one", 1, "two", 2, "three", 3);
        Map<Integer, String> inverted = invertMap(original);
        System.out.println("Original: " + original);
        System.out.println("Inverted: " + inverted);

        // Merge maps
        Map<String, Integer> map1 = Map.of("a", 1, "b", 2);
        Map<String, Integer> map2 = Map.of("b", 3, "c", 4);
        Map<String, Integer> merged = mergeMaps(map1, map2);
        System.out.println("Merged: " + merged);
        System.out.println();
    }

    /**
     * Demonstrates custom key objects with proper equals/hashCode.
     */
    private static void demonstrateCustomKeys() {
        System.out.println("=== Custom Key Objects ===");

        Map<Employee, String> departments = new HashMap<>();
        departments.put(new Employee(1, "Alice"), "Engineering");
        departments.put(new Employee(2, "Bob"), "Marketing");
        departments.put(new Employee(1, "Alice"), "Management"); // Replaces

        System.out.println("Departments:");
        departments.forEach((emp, dept) ->
                System.out.println("  " + emp.name() + " -> " + dept)
        );
        System.out.println();
    }

    /**
     * Demonstrates performance patterns.
     */
    private static void demonstratePerformancePatterns() {
        System.out.println("=== Performance Patterns ===");

        // Pattern 1: Compute operations
        Map<String, Integer> wordCount = new HashMap<>();
        String[] words = {"java", "is", "great", "java", "is", "fun", "java"};
        for (String word : words) {
            wordCount.merge(word, 1, Integer::sum);
        }
        System.out.println("Word count: " + wordCount);

        // Pattern 2: Nested maps
        Map<String, Map<String, Integer>> scores = new HashMap<>();
        scores.computeIfAbsent("Math", k -> new HashMap<>()).put("Alice", 95);
        scores.computeIfAbsent("Math", k -> new HashMap<>()).put("Bob", 87);
        scores.computeIfAbsent("Science", k -> new HashMap<>()).put("Alice", 92);

        System.out.println("Nested scores:");
        scores.forEach((subject, studentScores) -> {
            System.out.println("  " + subject + ":");
            studentScores.forEach((student, score) ->
                    System.out.println("    " + student + ": " + score)
            );
        });

        // Pattern 3: Two Sum problem
        int[] nums = {2, 7, 11, 15, 3, 6};
        int target = 9;
        int[] result = twoSum(nums, target);
        System.out.println("Two Sum indices: " + java.util.Arrays.toString(result));
    }

    /**
     * Counts word frequency in a string.
     */
    private static Map<String, Integer> wordFrequency(String text) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : text.split("\\s+")) {
            freq.merge(word, 1, Integer::sum);
        }
        return freq;
    }

    /**
     * Groups words by their first letter.
     */
    private static Map<Character, java.util.List<String>> groupByFirstLetter(java.util.List<String> words) {
        Map<Character, java.util.List<String>> grouped = new HashMap<>();
        for (String word : words) {
            grouped.computeIfAbsent(word.charAt(0), k -> new java.util.ArrayList<>()).add(word);
        }
        return grouped;
    }

    /**
     * Inverts a map (swaps keys and values).
     */
    private static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> inverted = new HashMap<>();
        map.forEach((k, v) -> inverted.put(v, k));
        return inverted;
    }

    /**
     * Merges two maps, summing values for duplicate keys.
     */
    private static Map<String, Integer> mergeMaps(Map<String, Integer> map1, Map<String, Integer> map2) {
        Map<String, Integer> merged = new HashMap<>(map1);
        map2.forEach((k, v) -> merged.merge(k, v, Integer::sum));
        return merged;
    }

    /**
     * Solves the Two Sum problem using HashMap.
     */
    private static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    /**
     * Employee record with proper equals/hashCode for use as HashMap key.
     */
    record Employee(int id, String name) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return id == employee.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
