package academy.javaengineering.collections.solutions;

import java.util.*;
import java.util.function.*;

public class IteratorSolutions {

    // Exercise 16: Remove all even numbers using Iterator
    public static void removeEvens(List<Integer> list) {
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) {
                it.remove();
            }
        }
    }

    // Exercise 17: Search from end using ListIterator
    public static <T> int searchFromEnd(List<T> list, T target) {
        ListIterator<T> it = list.listIterator(list.size());
        while (it.hasPrevious()) {
            if (it.previous().equals(target)) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    // Exercise 18: Remove entries > 50
    public static void removeHighValues(Map<String, Integer> map) {
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() > 50) {
                it.remove();
            }
        }
    }

    // Exercise 19: Custom filtered iterator
    public static Iterator<String> filterByLength(List<String> list, int minLength) {
        return list.stream()
            .filter(s -> s.length() >= minLength)
            .iterator();
    }

    // Exercise 20: Count even numbers using Spliterator
    public static long countEvenNumbers(List<Integer> list) {
        return list.stream().filter(n -> n % 2 == 0).count();
    }
}