package academy.javaengineering.collections.iteration.forloop;

import java.util.*;

/**
 * For Loop Solutions
 * Complete implementations for all exercises.
 */
public class ForLoopSolutions {

    // Solution 1: Basic iteration
    public static <T> void printAll(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ": " + list.get(i));
        }
    }

    // Solution 2: Modification during iteration
    public static void removeOdds(List<Integer> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) % 2 != 0) {
                list.remove(i);
            }
        }
    }

    // Solution 3: Edge case handling
    public static int findMax(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        int max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
            }
        }
        return max;
    }

    // Solution 4: Performance comparison
    public static long timeForLoop(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        return System.nanoTime() - start;
    }

    public static long timeEnhancedFor(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        for (int n : list) {
            sum += n;
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static <T> List<List<T>> paginate(List<T> list, int pageSize) {
        List<List<T>> pages = new ArrayList<>();
        for (int i = 0; i < list.size(); i += pageSize) {
            int end = Math.min(i + pageSize, list.size());
            pages.add(new ArrayList<>(list.subList(i, end)));
        }
        return pages;
    }
}
