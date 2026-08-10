package academy.javaengineering.collections.iteration.spliterator;

import java.util.*;
import java.util.stream.*;

/**
 * Spliterator Solutions
 * Complete implementations for all exercises.
 */
public class SpliteratorSolutions {

    // Solution 1: Basic iteration
    public static <T> void printAll(List<T> list) {
        Spliterator<T> spl = list.spliterator();
        spl.forEachRemaining(System.out::println);
    }

    // Solution 2: Modification during iteration
    public static void parallelProcess(List<Integer> list) {
        Spliterator<Integer> spl1 = list.spliterator();
        Spliterator<Integer> spl2 = spl1.trySplit();
        if (spl2 != null) {
            spl1.forEachRemaining(System.out::println);
            spl2.forEachRemaining(System.out::println);
        } else {
            spl1.forEachRemaining(System.out::println);
        }
    }

    // Solution 3: Edge case handling
    public static <T> List<T> safeToList(Spliterator<T> spliterator) {
        List<T> result = new ArrayList<>();
        spliterator.forEachRemaining(result::add);
        return result;
    }

    // Solution 4: Performance comparison
    public static long timeSpliterator(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        Spliterator<Integer> spl = list.spliterator();
        Spliterator<Integer> spl2 = spl.trySplit();
        if (spl2 != null) {
            spl.forEachRemaining(n -> sum += n);
            spl2.forEachRemaining(n -> sum += n);
        } else {
            spl.forEachRemaining(n -> sum += n);
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static <T> long parallelCount(List<T> list, java.util.function.Predicate<T> predicate) {
        return list.stream()
            .filter(predicate)
            .count();
    }
}
