package academy.javaengineering.collections.iteration.whileloop;

import java.util.*;

/**
 * While Loop Solutions
 * Complete implementations for all exercises.
 */
public class WhileLoopSolutions {

    // Solution 1: Basic iteration
    public static <T> void printAll(List<T> list) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    // Solution 2: Modification during iteration
    public static void removeNegatives(List<Integer> list) {
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() < 0) {
                it.remove();
            }
        }
    }

    // Solution 3: Edge case handling
    public static <T> List<T> safeCopy(List<T> original) {
        List<T> copy = new ArrayList<>(original);
        Iterator<T> it = copy.iterator();
        while (it.hasNext()) {
            it.next();
        }
        return copy;
    }

    // Solution 4: Performance comparison
    public static long timeWhileLoop(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            sum += it.next();
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static <T> List<T> drainQueue(Queue<T> queue) {
        List<T> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        return result;
    }
}
