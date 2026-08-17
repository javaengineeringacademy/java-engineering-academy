package transforming.solutions;

import java.util.*;
import java.util.stream.*;

/**
 * Utransforming Operations Solutions
 */
public class UtransformingSolutions {

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        return list.stream()
            .distinct()
            .collect(Collectors.toList());
    }

    public static <T> List<T> takeWhile(List<T> list, Predicate<T> predicate) {
        return list.stream()
            .takeWhile(predicate)
            .collect(Collectors.toList());
    }

    public static <T> List<T> dropWhile(List<T> list, Predicate<T> predicate) {
        return list.stream()
            .dropWhile(predicate)
            .collect(Collectors.toList());
    }

    @SafeVarargs
    public static <T> List<T> filterMultiple(List<T> list, Predicate<T>... predicates) {
        Stream<T> stream = list.stream();
        for (Predicate<T> predicate : predicates) {
            stream = stream.filter(predicate);
        }
        return stream.collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        System.out.println("Filter even numbers: " + filter(numbers, n -> n % 2 == 0));
        System.out.println("Remove duplicates: " + removeDuplicates(List.of(1, 2, 2, 3, 3, 3)));
        System.out.println("Take while < 5: " + takeWhile(numbers, n -> n < 5));
        System.out.println("Drop while < 5: " + dropWhile(numbers, n -> n < 5));
    }
}
