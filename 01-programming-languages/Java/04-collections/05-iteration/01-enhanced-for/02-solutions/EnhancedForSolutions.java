package academy.javaengineering.collections.iteration.enhanced;

import java.util.*;

/**
 * Enhanced For Loop Solutions
 * Complete implementations for all exercises.
 */
public class EnhancedForSolutions {

    // Solution 1: Basic iteration
    public static <T> void printAll(Set<T> set) {
        for (T element : set) {
            System.out.println(element);
        }
    }

    // Solution 2: Modification during iteration
    public static void safeRemove(List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.startsWith("A")) {
                it.remove();
            }
        }
    }

    // Solution 3: Edge case handling
    public static String findLongest(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        String longest = "";
        for (String s : list) {
            if (s.length() > longest.length()) {
                longest = s;
            }
        }
        return longest;
    }

    // Solution 4: Performance comparison
    public static long timeEnhancedFor(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        for (int n : list) {
            sum += n;
        }
        return System.nanoTime() - start;
    }

    public static long timeForLoop(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static List<String> filterByLength(List<String> list, int minLength) {
        List<String> result = new ArrayList<>();
        for (String s : list) {
            if (s.length() >= minLength) {
                result.add(s);
            }
        }
        return result;
    }
}
