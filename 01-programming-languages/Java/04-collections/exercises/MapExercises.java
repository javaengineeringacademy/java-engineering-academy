package academy.javaengineering.exercises;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Exercises: Map (HashMap, TreeMap) Operations
 *
 * Complete the TODO sections below.
 */
public class MapExercises {

    // TODO 1: Count frequency of each character in a string
    public Map<Character, Integer> charFrequency(String text) {
        // TODO: implement this
        return new HashMap<>();
    }

    // TODO 2: Merge two maps, summing values for common keys
    public Map<String, Integer> mergeMaps(Map<String, Integer> map1, Map<String, Integer> map2) {
        // TODO: implement this
        return new HashMap<>();
    }

    // TODO 3: Invert a map (swap keys and values)
    // If multiple keys have same value, keep the last one
    public <K, V> Map<V, K> invertMap(Map<K, V> map) {
        // TODO: implement this
        return new HashMap<>();
    }

    // TODO 4: Group words by their first character
    public Map<Character, String[]> groupByFirstChar(String[] words) {
        // TODO: implement this
        return new HashMap<>();
    }

    // TODO 5: Find the most frequent value in a map
    public <K, V> V mostFrequentValue(Map<K, V> map) {
        // TODO: implement this
        return null;
    }

    // TODO 6: Sort a map by values using TreeMap
    // Returns a TreeMap sorted by values in ascending order
    public TreeMap<String, Integer> sortByValue(Map<String, Integer> map) {
        // TODO: implement this using stream or manual sorting
        return new TreeMap<>();
    }

    // TODO 7: Implement a simple cache with LRU-like behavior
    // Store key-value pairs with a max capacity
    // When capacity is exceeded, remove the first (oldest) entry
    public static class SimpleCache<K, V> {
        private final int capacity;
        private final Map<K, V> cache;

        public SimpleCache(int capacity) {
            this.capacity = capacity;
            this.cache = new LinkedHashMap<>(capacity, 0.75f, true);
        }

        public void put(K key, V value) {
            // TODO: implement this - remove oldest if at capacity
        }

        public V get(K key) {
            // TODO: implement this
            return null;
        }

        public int size() {
            return cache.size();
        }

        public void clear() {
            cache.clear();
        }

        // Need LinkedHashMap for access-order
        private static class LinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> {
            private final int maxCapacity;

            LinkedHashMap(int capacity, float loadFactor, boolean accessOrder) {
                super(capacity, loadFactor, accessOrder);
                this.maxCapacity = capacity;
            }

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxCapacity;
            }
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        MapExercises exercises = new MapExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== MapExercises Tests ===\n");

        // Test 1
        total++;
        Map<Character, Integer> freq = exercises.charFrequency("hello");
        if (freq.get('h') == 1 && freq.get('e') == 1 && freq.get('l') == 2 && freq.get('o') == 1) {
            System.out.println("Test 1 PASSED: charFrequency");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: charFrequency - got " + freq);
        }

        // Test 2
        total++;
        Map<String, Integer> m1 = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> m2 = Map.of("b", 4, "c", 5, "d", 6);
        Map<String, Integer> merged = exercises.mergeMaps(m1, m2);
        if (merged.get("a") == 1 && merged.get("b") == 6 && merged.get("c") == 8 && merged.get("d") == 6) {
            System.out.println("Test 2 PASSED: mergeMaps");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: mergeMaps - got " + merged);
        }

        // Test 3
        total++;
        Map<String, Integer> original = Map.of("one", 1, "two", 2, "three", 3);
        Map<Integer, String> inverted = exercises.invertMap(original);
        if ("one".equals(inverted.get(1)) && "two".equals(inverted.get(2)) && "three".equals(inverted.get(3))) {
            System.out.println("Test 3 PASSED: invertMap");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: invertMap - got " + inverted);
        }

        // Test 4
        total++;
        String[] words = {"apple", "banana", "avocado", "blueberry", "cherry"};
        Map<Character, String[]> grouped = exercises.groupByFirstChar(words);
        if (grouped.get('a').length == 2 && grouped.get('b').length == 2 && grouped.get('c').length == 1) {
            System.out.println("Test 4 PASSED: groupByFirstChar");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: groupByFirstChar - got " + grouped);
        }

        // Test 5
        total++;
        Map<String, Integer> votes = Map.of("Alice", 5, "Bob", 10, "Charlie", 10);
        Integer maxVotes = exercises.mostFrequentValue(votes);
        if (maxVotes != null && maxVotes == 10) {
            System.out.println("Test 5 PASSED: mostFrequentValue");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: mostFrequentValue - got " + maxVotes);
        }

        // Test 6
        total++;
        Map<String, Integer> unsorted = Map.of("c", 3, "a", 1, "b", 2);
        TreeMap<String, Integer> sorted = exercises.sortByValue(unsorted);
        String[] keys = sorted.keySet().toArray(new String[0]);
        if (keys.length == 3 && "a".equals(keys[0]) && "b".equals(keys[1]) && "c".equals(keys[2])) {
            System.out.println("Test 6 PASSED: sortByValue");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: sortByValue - got " + sorted);
        }

        // Test 7
        total++;
        try {
            SimpleCache<String, Integer> cache = new SimpleCache<>(3);
            cache.put("a", 1);
            cache.put("b", 2);
            cache.put("c", 3);
            cache.put("d", 4); // should evict "a"
            if (cache.size() == 3 && cache.get("a") == null && cache.get("d") == 4) {
                System.out.println("Test 7 PASSED: SimpleCache");
                passed++;
            } else {
                System.out.println("Test 7 FAILED: SimpleCache");
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Test 7 FAILED: SimpleCache - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
