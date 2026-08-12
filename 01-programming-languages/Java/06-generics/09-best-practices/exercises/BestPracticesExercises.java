package academy.javaengineering.generics.best-practices.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Best Practices Exercises
 * Apply best practices when using Java generics.
 */
public class BestPracticesExercises {

    // Exercise 1: Use bounded types instead of wildcards when needed
    // TODO: Refactor to use proper type bounds
    static class NumberProcessor {
        // Bad: Uses wildcard but needs to modify the list
        // TODO: Fix this method
        public static void processNumbers(List<? extends Number> list) {
            // Cannot add to this list!
            // list.add(42);  // COMPILE ERROR
        }

        // TODO: Create a better version with proper bounds
    }

    // Exercise 2: Prefer List over array for generics
    // TODO: Refactor code to use List instead of array
    public static <T> void badApproach(T[] array) {
        // Bad: Uses raw array
        Object[] objArray = array;
        objArray[0] = "String";  // Runtime error!
    }

    // TODO: Create a better version using List
    public static <T> void goodApproach(List<T> list) {
        // Good: Type safe
    }

    // Exercise 3: Use @SafeVarargs for varargs with generics
    // TODO: Fix the unchecked warning
    @SuppressWarnings("unchecked")
    public static <T> List<T> badVarargs(T... items) {
        // Bad: Creates unchecked warning
        List<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    // TODO: Create a better version with @SafeVarargs

    // Exercise 4: Document generic type parameters
    // TODO: Add proper Javadoc for generic classes
    // Bad: No documentation
    static class Cache<K, V> {
        private final java.util.Map<K, V> map = new java.util.HashMap<>();

        public V get(K key) {
            return map.get(key);
        }

        public void put(K key, V value) {
            map.put(key, value);
        }
    }

    // TODO: Create a documented version

    // Exercise 5: Use type witnesses sparingly
    // TODO: Demonstrate when type witnesses are needed
    public static <T> T identity(T value) {
        return value;
    }

    public static void exercise5() {
        // TODO: Show when type witnesses are needed vs inferred
        // String result = identity("Hello");  // Inferred
        // Object result2 = identity("Hello");  // What happens here?
    }

    public static void main(String[] args) {
        System.out.println("=== Best Practices Exercises ===\n");
        exercise5();
    }
}
