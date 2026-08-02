package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Topic 07: Best Practices.
 *
 * <p>This class demonstrates best practices for generic code,
 * including naming conventions, PECS principle, and documentation.</p>
 */
public final class BestPractices {

    private BestPractices() {
    }

    /**
     * Good: Clear naming with standard type parameter letters.
     *
     * @param <T> the element type
     * @param a   first element
     * @param b   second element
     * @return a list containing both elements
     */
    public static <T> List<T> asList(T a, T b) {
        List<T> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        return list;
    }

    /**
     * Good: Bounded type with minimal constraints.
     *
     * @param <T> the comparable type
     * @param a   first value
     * @param b   second value
     * @return the greater value
     */
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * Good: Wildcard for read-only access (Producer Extends).
     *
     * @param list the list to print
     */
    public static void printAll(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    /**
     * Good: Wildcard for write-only access (Consumer Super).
     *
     * @param <T>    the element type
     * @param dest   the destination list
     * @param source the source elements
     */
    @SafeVarargs
    public static <T> void addAll(List<? super T> dest, T... source) {
        for (T item : source) {
            dest.add(item);
        }
    }

    /**
     * Good: PECS in copy operation.
     *
     * @param <T>    the element type
     * @param dest   the destination (Consumer)
     * @param source the source (Producer)
     */
    public static <T> void copy(List<? super T> dest, List<? extends T> source) {
        for (T item : source) {
            dest.add(item);
        }
    }

    /**
     * Good: Filter with proper wildcard bounds.
     *
     * @param <T>       the element type
     * @param source    the source list (Producer)
     * @param predicate the filter predicate (Consumer)
     * @return filtered list
     */
    public static <T> List<T> filter(
            List<? extends T> source,
            Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Good: Map with proper wildcard bounds.
     *
     * @param <T>    the source type
     * @param <R>    the result type
     * @param source the source list (Producer)
     * @param mapper the mapping function
     * @return mapped list
     */
    public static <T, R> List<R> map(
            List<? extends T> source,
            Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : source) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    /**
     * Demonstrates best practices.
     */
    public static void main(String[] args) {
        // Clear naming
        List<String> names = asList("Alice", "Bob");
        System.out.println("Names: " + names);

        // Minimal bounds
        System.out.println("Max: " + max(10, 20));
        System.out.println("Max: " + max("hello", "world"));

        // PECS in action
        List<Number> numbers = new ArrayList<>();
        addAll(numbers, 1, 2, 3);
        System.out.println("Numbers: " + numbers);

        // Copy with PECS
        List<Object> objects = new ArrayList<>();
        copy(objects, List.of("a", "b", "c"));
        System.out.println("Objects: " + objects);

        // Filter
        List<Integer> evens = filter(List.of(1, 2, 3, 4, 5, 6), n -> n % 2 == 0);
        System.out.println("Evens: " + evens);

        // Map
        List<String> strings = map(List.of(1, 2, 3), n -> "Num: " + n);
        System.out.println("Mapped: " + strings);
    }
}
