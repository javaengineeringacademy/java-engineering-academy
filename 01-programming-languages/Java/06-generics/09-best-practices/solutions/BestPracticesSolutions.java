package academy.javaengineering.generics.best-practices.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Best Practices Solutions - Complete implementations for all exercises.
 */
public class BestPracticesSolutions {

    // Exercise 1: Use bounded types properly
    static class NumberProcessor {
        // Bad: Uses wildcard but needs to modify the list
        public static void processNumbersBad(List<? extends Number> list) {
            // Cannot add to this list!
            // for (Number num : list) { ... }  // Can only read
        }

        // Good: Use bounded type parameter when you need to modify
        public static <T extends Number> void processNumbersGood(List<T> list) {
            // Can both read and add (same type)
            for (T num : list) {
                System.out.print(num + " ");
            }
            System.out.println();
        }

        // Best: Use wildcard for read-only, bounded type for read-write
        public static double sum(List<? extends Number> list) {
            double total = 0;
            for (Number num : list) {
                total += num.doubleValue();
            }
            return total;
        }
    }

    // Exercise 2: Prefer List over array
    public static <T> void badApproach(T[] array) {
        // Bad: Uses raw array - runtime error possible
        Object[] objArray = array;
        // objArray[0] = "String";  // ArrayStoreException at runtime!
    }

    // Good: Use List for type safety
    public static <T> void goodApproach(List<T> list) {
        // Good: Type safe - compiler prevents errors
        // list.add("String");  // COMPILE ERROR if T is Integer
    }

    // Exercise 3: Use @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> List<T> badVarargs(T... items) {
        // Bad: Creates unchecked warning
        List<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    // Good: Use @SafeVarargs for final/immutable varargs
    @SafeVarargs
    public static <T> List<T> goodVarargs(T... items) {
        // Good: No warning with @SafeVarargs
        List<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    // Exercise 4: Document generic type parameters
    /**
     * A cache implementation with generic key and value types.
     *
     * @param <K> the type of cache keys, must be immutable
     * @param <V> the type of cached values
     */
    static class DocumentedCache<K, V> {
        private final java.util.Map<K, V> map = new java.util.HashMap<>();

        /**
         * Retrieves a value from the cache.
         *
         * @param key the key to look up
         * @return the value associated with the key, or null
         */
        public V get(K key) {
            return map.get(key);
        }

        /**
         * Stores a value in the cache.
         *
         * @param key the key to store under
         * @param value the value to cache
         */
        public void put(K key, V value) {
            map.put(key, value);
        }
    }

    // Exercise 5: Type witnesses
    public static <T> T identity(T value) {
        return value;
    }

    public static void exercise5() {
        // Type inference works in most cases
        String result1 = identity("Hello");
        System.out.println("Inferred: " + result1);

        // Type witness needed when inference fails
        Object result2 = BestPracticesSolutions.<String>identity("Hello");
        System.out.println("Explicit: " + result2);

        // Type witness in static context
        List<String> list = BestPracticesSolutions.<String>new ArrayList<>();
        list.add("Test");
        System.out.println("Type witness for constructor: " + list);
    }

    public static void main(String[] args) {
        System.out.println("=== Best Practices Solutions ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Bounded Types");
        List<Integer> nums = List.of(1, 2, 3, 4, 5);
        NumberProcessor.processNumbersGood(nums);
        System.out.println("Sum: " + NumberProcessor.sum(nums));

        // Test Exercise 5
        System.out.println("\nExercise 5: Type Witnesses");
        exercise5();
    }
}
