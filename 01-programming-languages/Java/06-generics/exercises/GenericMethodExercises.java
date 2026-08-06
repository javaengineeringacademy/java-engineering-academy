package academy.javaengineering.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * Exercises: Generic Methods and Bounded Types
 *
 * Complete the TODO sections below.
 */
public class GenericMethodExercises {

    // TODO 1: Implement a generic method to find the maximum in a list
    // Must work for any Comparable type
    // Throw IllegalArgumentException if list is null or empty
    public <T extends Comparable<T>> T findMax(List<T> list) {
        // TODO: implement
        return null;
    }

    // TODO 2: Implement a generic method to filter a list by a predicate
    // Return a new list containing only elements matching the predicate
    public <T> List<T> filter(List<T> list, java.util.function.Predicate<T> predicate) {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 3: Implement a generic method to convert a list of one type to another
    // Using a converter function
    public <T, R> List<R> convert(List<T> source, java.util.function.Function<T, R> converter) {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 4: Implement a generic method that safely casts objects
    // If the object is of the target type, return it casted
    // Otherwise return null
    @SuppressWarnings("unchecked")
    public <T> T safeCast(Object obj, Class<T> type) {
        // TODO: implement
        return null;
    }

    // TODO 5: Implement a generic method to zip two lists
    // [1, 2, 3] and ["a", "b", "c"] -> [(1, "a"), (2, "b"), (3, "c")]
    // Use a Pair class or create a record
    public <T, U> List<Map.Entry<T, U>> zip(List<T> list1, List<U> list2) {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 6: Implement a generic swap method for arrays
    public <T> void swap(T[] array, int i, int j) {
        // TODO: implement
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        GenericMethodExercises exercises = new GenericMethodExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== GenericMethodExercises Tests ===\n");

        // Test 1
        total++;
        try {
            Integer max = exercises.findMax(List.of(3, 1, 4, 1, 5, 9));
            if (max != null && max == 9) {
                System.out.println("Test 1a PASSED: findMax integers");
                passed++;
            } else {
                System.out.println("Test 1a FAILED: findMax integers");
            }
        } catch (Exception e) {
            System.out.println("Test 1a FAILED: findMax - " + e.getMessage());
        }

        total++;
        try {
            String max = exercises.findMax(List.of("apple", "banana", "cherry"));
            if ("cherry".equals(max)) {
                System.out.println("Test 1b PASSED: findMax strings");
                passed++;
            } else {
                System.out.println("Test 1b FAILED: findMax strings");
            }
        } catch (Exception e) {
            System.out.println("Test 1b FAILED: findMax - " + e.getMessage());
        }

        // Test 2
        total++;
        List<Integer> filtered = exercises.filter(List.of(1, 2, 3, 4, 5, 6), x -> x % 2 == 0);
        if (filtered.equals(List.of(2, 4, 6))) {
            System.out.println("Test 2 PASSED: filter");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: filter - " + filtered);
        }

        // Test 3
        total++;
        List<String> converted = exercises.convert(List.of(1, 2, 3), Object::toString);
        if (converted.equals(List.of("1", "2", "3"))) {
            System.out.println("Test 3 PASSED: convert");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: convert - " + converted);
        }

        // Test 4
        total++;
        Integer casted = exercises.safeCast(42, Integer.class);
        String nullCasted = exercises.safeCast(42, String.class);
        if (casted != null && casted == 42 && nullCasted == null) {
            System.out.println("Test 4 PASSED: safeCast");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: safeCast");
        }

        // Test 5
        total++;
        List<Map.Entry<Integer, String>> zipped = exercises.zip(List.of(1, 2, 3), List.of("a", "b", "c"));
        if (zipped.size() == 3
            && zipped.get(0).getKey() == 1 && "a".equals(zipped.get(0).getValue())
            && zipped.get(2).getKey() == 3 && "c".equals(zipped.get(2).getValue())) {
            System.out.println("Test 5 PASSED: zip");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: zip - " + zipped);
        }

        // Test 6
        total++;
        Integer[] arr = {1, 2, 3, 4, 5};
        exercises.swap(arr, 1, 3);
        if (arr[0] == 1 && arr[1] == 4 && arr[2] == 3 && arr[3] == 2 && arr[4] == 5) {
            System.out.println("Test 6 PASSED: swap");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: swap - " + Arrays.toString(arr));
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
