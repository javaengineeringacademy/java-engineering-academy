package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Topic 05: Wildcards.
 *
 * <p>This class demonstrates unbounded, upper-bounded, and
 * lower-bounded wildcards with the PECS principle.</p>
 */
public final class Wildcards {

    private Wildcards() {
    }

    /**
     * Unbounded wildcard - accepts any type.
     */
    public static void printAll(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    /**
     * Upper bounded wildcard - Producer Extends.
     */
    public static double sum(List<? extends Number> list) {
        return list.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    /**
     * Lower bounded wildcard - Consumer Super.
     */
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }

    /**
     * PECS: Producer Extends, Consumer Super.
     */
    public static <T> void transfer(
            List<? super T> dest,
            List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    /**
     * Wildcard capture for type-safe swap.
     */
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     * Filter with wildcard bounds.
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
     * Map with wildcard bounds.
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
     * Find minimum using wildcards.
     */
    public static <T extends Comparable<T>> T min(List<? extends T> list) {
        T min = list.get(0);
        for (T item : list) {
            if (item.compareTo(min) < 0) {
                min = item;
            }
        }
        return min;
    }

    /**
     * Copy with PECS.
     */
    public static <T> List<T> copy(List<? extends T> source) {
        List<T> result = new ArrayList<>();
        for (T item : source) {
            result.add(item);
        }
        return result;
    }

    /**
     * Demonstrates wildcard usage.
     */
    public static void main(String[] args) {
        // Unbounded wildcard
        List<String> names = List.of("Alice", "Bob", "Charlie");
        List<Integer> numbers = List.of(1, 2, 3);
        System.out.println("Names:");
        printAll(names);
        System.out.println("Numbers:");
        printAll(numbers);

        // Upper bounded wildcard
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.5, 2.5, 3.5);
        System.out.println("Int sum: " + sum(ints));
        System.out.println("Double sum: " + sum(doubles));

        // Lower bounded wildcard
        List<Number> numList = new ArrayList<>();
        addIntegers(numList);
        System.out.println("After addIntegers: " + numList);

        // Transfer (PECS)
        List<Object> dest = new ArrayList<>();
        List<String> src = List.of("hello", "world");
        transfer(dest, src);
        System.out.println("Transfer result: " + dest);

        // Swap with wildcard capture
        List<String> mutable = new ArrayList<>(List.of("a", "b", "c"));
        swap(mutable, 0, 2);
        System.out.println("After swap: " + mutable);

        // Filter
        List<Integer> evens = filter(List.of(1, 2, 3, 4, 5, 6), n -> n % 2 == 0);
        System.out.println("Evens: " + evens);

        // Map
        List<String> strings = map(List.of(1, 2, 3), n -> "Num: " + n);
        System.out.println("Mapped: " + strings);

        // Min
        System.out.println("Min doubles: " + min(doubles));

        // Copy
        List<Integer> copied = copy(List.of(10, 20, 30));
        System.out.println("Copied: " + copied);
    }
}
