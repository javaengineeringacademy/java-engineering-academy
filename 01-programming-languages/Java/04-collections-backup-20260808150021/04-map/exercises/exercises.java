package collections.map.exercises;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAP EXERCISES — HashMap, LinkedHashMap, TreeMap, ConcurrentHashMap
 *
 * Complete each TODO. Run tests to verify your solutions.
 */
public class exercises {

    // =========================================================================
    // EXERCISE 1: HashMap — Word Frequency Counter
    // =========================================================================
    /**
     * Given a sentence (string), return a HashMap mapping each word
     * to its frequency count. Words should be lowercased and trimmed.
     * Ignore empty strings.
     *
     * Example: "Hello hello world" → {hello=2, world=1}
     *
     * TODO: Implement this method
     */
    public static Map<String, Integer> wordFrequency(String sentence) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: LinkedHashMap — LRU Cache
    // =========================================================================
    /**
     * Implement a simple LRU (Least Recently Used) cache using LinkedHashMap.
     * The cache has a fixed capacity. When the cache exceeds capacity,
     * remove the least recently accessed entry.
     *
     * Your LRUCache class should have:
     *   - constructor LRUCache(int capacity)
     *   - int get(int key) — returns value or -1 if not found
     *   - void put(int key, int value) — insert or update
     *
     * TODO: Implement the LRUCache inner class
     */
    public static class LRUCache {
        private final int capacity;
        private final LinkedHashMap<Integer, Integer> map;

        public LRUCache(int capacity) {
            // TODO: Initialize the LinkedHashMap with accessOrder=true
            this.capacity = capacity;
            this.map = null; // Replace with proper initialization
        }

        public int get(int key) {
            // TODO: Your code here
            return -1;
        }

        public void put(int key, int value) {
            // TODO: Your code here
        }
    }

    // =========================================================================
    // EXERCISE 3: TreeMap — Range Count
    // =========================================================================
    /**
     * Given a TreeMap<String, Integer> (name → score), return the count
     * of entries whose keys fall alphabetically within [startKey, endKey]
     * inclusive. Use TreeMap's subMap or headMap/tailMap.
     *
     * Example: {"alice":90, "bob":85, "charlie":95, "diana":88}
     *          startKey="bob", endKey="diana" → 3
     *
     * TODO: Implement this method
     */
    public static int rangeCount(TreeMap<String, Integer> scores,
                                  String startKey, String endKey) {
        // TODO: Your code here
        return 0;
    }

    // =========================================================================
    // EXERCISE 4: ConcurrentHashMap — Thread-Safe Aggregator
    // =========================================================================
    /**
     * Given a list of strings, use ConcurrentHashMap to count word frequencies
     * concurrently. Use compute() or merge() for atomic updates.
     * Return the final map after all elements are processed.
     *
     * TODO: Implement this method
     */
    public static Map<String, Integer> concurrentWordCount(List<String> words) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 5: HashMap — Two Sum
    // =========================================================================
    /**
     * Given an array of integers and a target sum, return the indices
     * of two numbers that add up to the target as an int[2].
     * Use a HashMap for O(n) solution. If no solution, return null.
     *
     * Example: nums=[2,7,11,15], target=9 → [0,1]
     *
     * TODO: Implement this method
     */
    public static int[] twoSum(int[] nums, int target) {
        // TODO: Your code here
        return null;
    }
}
