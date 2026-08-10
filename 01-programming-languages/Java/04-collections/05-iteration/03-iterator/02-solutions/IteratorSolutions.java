package academy.javaengineering.collections.iteration.iterator;

import java.util.*;

/**
 * Iterator Solutions
 * Complete implementations for all exercises.
 */
public class IteratorSolutions {

    // Solution 1: Basic iteration
    public static <T> void printAll(List<T> list) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    // Solution 2: Modification during iteration
    public static <T> void removeMatching(List<T> list, T target) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals(target)) {
                it.remove();
            }
        }
    }

    // Solution 3: Edge case handling
    public static <T> List<T> safeFilter(List<T> list, java.util.function.Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        Iterator<T> it = list.iterator();
        try {
            while (it.hasNext()) {
                T item = it.next();
                if (predicate.test(item)) {
                    result.add(item);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.err.println("Collection was modified during iteration");
        }
        return result;
    }

    // Solution 4: Performance comparison
    public static long timeIteratorRemoval(List<Integer> list, int threshold) {
        long start = System.nanoTime();
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() > threshold) {
                it.remove();
            }
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static <T> List<T> filter(List<T> list, java.util.function.Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T item = it.next();
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
