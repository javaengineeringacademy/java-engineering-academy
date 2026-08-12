package academy.javaengineering.generics.practices;

import java.util.*;

/**
 * Exercises: Generic Classes
 *
 * <p>Complexity: Varies by exercise</p>
 * <p>Thread-safety: Not thread-safe</p>
 * <p>Key characteristics: Practice exercises for implementing generic Pair, Stack, Result, and Cache classes</p>
 */
public class GenericClassExercises {

    // TODO 1: Create a generic Pair class
    // Fields: first (T), second (U)
    // Constructor, getters, equals, hashCode, toString
    // Uncomment and implement:
    /*
    public static class Pair<T, U> {
        // TODO: implement
    }
    */

    // TODO 2: Create a generic Stack class
    // Methods: push(T), pop() (throws NoSuchElementException if empty),
    // peek() (throws NoSuchElementException if empty), isEmpty(), size()
    public static class Stack<T> {
        // TODO: implement using ArrayList or LinkedList
    }

    // TODO 3: Create a generic Result class (like Rust's Result type)
    // Can be either Success(value) or Failure(error)
    // Methods: isSuccess(), isError(), getValue(), getError(),
    // map(Function), orElse(T defaultValue)
    public static class Result<T> {
        // TODO: implement
    }

    // TODO 4: Implement a generic Cache class
    // Stores key-value pairs with a max size
    // When full, removing the oldest entry
    public static class Cache<K, V> {
        private final int maxSize;
        private final LinkedHashMap<K, V> map;

        public Cache(int maxSize) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap<>(maxSize, 0.75f, true);
        }

        public void put(K key, V value) {
            // TODO: implement with eviction logic
        }

        public V get(K key) {
            // TODO: implement
            return null;
        }

        public int size() {
            return map.size();
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        GenericClassExercises exercises = new GenericClassExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== GenericClassExercises Tests ===\n");

        // Test 2 - Stack
        total++;
        try {
            Stack<Integer> stack = new Stack<>();
            stack.push(1);
            stack.push(2);
            stack.push(3);
            if (stack.size() == 3 && stack.peek() == 3 && stack.pop() == 3 && stack.pop() == 2) {
                System.out.println("Test 2 PASSED: Stack basic operations");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: Stack basic operations");
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: Stack - " + e.getMessage());
        }

        total++;
        try {
            Stack<String> stack = new Stack<>();
            stack.pop();
            System.out.println("Test 2b FAILED: should throw on empty pop");
        } catch (NoSuchElementException e) {
            System.out.println("Test 2b PASSED: Stack empty pop throws");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 2b FAILED: wrong exception type");
        }

        // Test 4 - Cache
        total++;
        try {
            Cache<String, Integer> cache = new Cache<>(2);
            cache.put("a", 1);
            cache.put("b", 2);
            cache.put("c", 3);
            if (cache.get("a") == null && cache.get("b") == 2 && cache.get("c") == 3 && cache.size() == 2) {
                System.out.println("Test 4 PASSED: Cache eviction");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: Cache eviction - size=" + cache.size());
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: Cache - " + e.getMessage());
        }

        total++;
        try {
            Cache<String, String> cache = new Cache<>(3);
            cache.put("x", "1");
            cache.get("x");
            cache.put("y", "2");
            cache.put("z", "3");
            cache.put("w", "4");
            if ("1".equals(cache.get("x")) && cache.size() == 3) {
                System.out.println("Test 4b PASSED: Cache access ordering");
                passed++;
            } else {
                System.out.println("Test 4b FAILED: Cache access ordering");
            }
        } catch (Exception e) {
            System.out.println("Test 4b FAILED: Cache - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
