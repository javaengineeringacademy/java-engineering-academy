package academy.javaengineering.collections.iteration.lambda;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Lambda/Stream Solutions
 * Complete implementations for all exercises.
 */
public class LambdaSolutions {

    // Solution 1: Basic iteration
    public static <T> void printAll(List<T> list) {
        list.stream().forEach(System.out::println);
    }

    // Solution 2: Modification during iteration
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    // Solution 3: Edge case handling
    public static <T> List<T> safeFilter(List<T> list, Predicate<T> predicate) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
            .filter(Objects::nonNull)
            .filter(predicate)
            .collect(Collectors.toList());
    }

    // Solution 4: Performance comparison
    public static long timeStream(List<Integer> list) {
        long start = System.nanoTime();
        long sum = list.stream().mapToLong(Integer::longValue).sum();
        return System.nanoTime() - start;
    }

    public static long timeForLoop(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        for (int n : list) {
            sum += n;
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static <T, R> List<R> complexPipeline(List<T> list,
                                                   Predicate<T> filter,
                                                   Function<T, R> mapper,
                                                   Comparator<R> sorter) {
        return list.stream()
            .filter(filter)
            .map(mapper)
            .sorted(sorter)
            .collect(Collectors.toList());
    }
}
