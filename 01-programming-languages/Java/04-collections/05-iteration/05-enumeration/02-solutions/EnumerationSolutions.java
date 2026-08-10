package academy.javaengineering.collections.iteration.enumeration;

import java.util.*;

/**
 * Enumeration Solutions
 * Complete implementations for all exercises.
 */
public class EnumerationSolutions {

    // Solution 1: Basic iteration
    public static <T> void printAll(Vector<T> vector) {
        Enumeration<T> en = vector.elements();
        while (en.hasMoreElements()) {
            System.out.println(en.nextElement());
        }
    }

    // Solution 2: Modification during iteration
    public static void safeRemove(Vector<String> vector) {
        ListIterator<String> it = vector.listIterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.startsWith("A")) {
                it.remove();
            }
        }
    }

    // Solution 3: Edge case handling
    public static <T> List<T> toList(Vector<T> vector) {
        List<T> result = new ArrayList<>();
        Enumeration<T> en = vector.elements();
        while (en.hasMoreElements()) {
            T element = en.nextElement();
            if (element != null) {
                result.add(element);
            }
        }
        return result;
    }

    // Solution 4: Performance comparison
    public static long timeEnumeration(Vector<Integer> vector) {
        long start = System.nanoTime();
        long sum = 0;
        Enumeration<Integer> en = vector.elements();
        while (en.hasMoreElements()) {
            sum += en.nextElement();
        }
        return System.nanoTime() - start;
    }

    public static long timeIterator(Vector<Integer> vector) {
        long start = System.nanoTime();
        long sum = 0;
        Iterator<Integer> it = vector.iterator();
        while (it.hasNext()) {
            sum += it.next();
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static <T> Vector<T> legacyFilter(Vector<T> vector, java.util.function.Predicate<T> predicate) {
        Vector<T> result = new Vector<>();
        Enumeration<T> en = vector.elements();
        while (en.hasMoreElements()) {
            T element = en.nextElement();
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }
}
