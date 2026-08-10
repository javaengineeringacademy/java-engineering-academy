package sorting.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * Usorting Operations Exercises
 * Complete the TODO exercises below.
 */
public class UsortingExercises {

    // TODO 1: Filter elements based on a condition
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        // Your code here
        return null;
    }

    // TODO 2: Remove duplicate elements
    public static <T> List<T> removeDuplicates(List<T> list) {
        // Your code here
        return null;
    }

    // TODO 3: Take elements while condition is true
    public static <T> List<T> takeWhile(List<T> list, Predicate<T> predicate) {
        // Your code here
        return null;
    }

    // TODO 4: Drop elements while condition is true
    public static <T> List<T> dropWhile(List<T> list, Predicate<T> predicate) {
        // Your code here
        return null;
    }

    // TODO 5: Combine multiple filter conditions
    public static <T> List<T> filterMultiple(List<T> list, Predicate<T>... predicates) {
        // Your code here
        return null;
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        System.out.println("Filter even numbers: " + filter(numbers, n -> n % 2 == 0));
        System.out.println("Remove duplicates: " + removeDuplicates(List.of(1, 2, 2, 3, 3, 3)));
        System.out.println("Take while < 5: " + takeWhile(numbers, n -> n < 5));
        System.out.println("Drop while < 5: " + dropWhile(numbers, n -> n < 5));
    }
}
