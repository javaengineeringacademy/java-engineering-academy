package academy.javaengineering.collections.iteration.listiterator;

import java.util.*;

/**
 * ListIterator Solutions
 * Complete implementations for all exercises.
 */
public class ListIteratorSolutions {

    // Solution 1: Basic iteration
    public static <T> void printBidirectional(List<T> list) {
        ListIterator<T> it = list.listIterator();
        System.out.println("Forward:");
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println("Backward:");
        while (it.hasPrevious()) {
            System.out.println(it.previous());
        }
    }

    // Solution 2: Modification during iteration
    public static <T> void replaceAll(List<T> list, T oldVal, T newVal) {
        ListIterator<T> it = list.listIterator();
        while (it.hasNext()) {
            if (it.next().equals(oldVal)) {
                it.set(newVal);
            }
        }
    }

    // Solution 3: Edge case handling
    public static <T> boolean safeInsert(List<T> list, int index, T element) {
        if (index < 0 || index > list.size()) {
            return false;
        }
        ListIterator<T> it = list.listIterator(index);
        it.add(element);
        return true;
    }

    // Solution 4: Performance comparison
    public static long timeListIteratorForward(List<Integer> list) {
        long start = System.nanoTime();
        long sum = 0;
        ListIterator<Integer> it = list.listIterator();
        while (it.hasNext()) {
            sum += it.next();
        }
        return System.nanoTime() - start;
    }

    // Solution 5: Real-world scenario
    public static <T> void insertAtPositions(List<T> list, List<T> elements, List<Integer> positions) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            int pos = positions.get(i);
            if (pos >= 0 && pos <= list.size()) {
                list.add(pos, elements.get(i));
            }
        }
    }
}
