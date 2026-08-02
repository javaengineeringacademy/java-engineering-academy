package academy.javaengineering.generics;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Topic 03: Generic Methods.
 *
 * <p>This class demonstrates generic methods with their own
 * type parameters, type inference, and bounded type methods.</p>
 */
public final class GenericMethods {

    private GenericMethods() {
    }

    /**
     * Basic generic method - identity function.
     */
    public static <T> T identity(T value) {
        return value;
    }

    /**
     * Generic method creating a list from varargs.
     */
    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }

    /**
     * Generic method with upper bound.
     */
    public static <T extends Number> double sum(List<T> numbers) {
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    /**
     * Generic method with multiple bounds.
     */
    public static <T extends Number & Comparable<T>> T max(List<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    /**
     * Generic method with recursive bound.
     */
    public static <T extends Comparable<T>> T maxOfTwo(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * Generic method with multiple type parameters.
     */
    public static <T, R> List<R> map(List<T> source, Function<T, R> mapper) {
        return source.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * Generic method with wildcard capture.
     */
    public static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     * Type-safe casting method.
     */
    @SuppressWarnings("unchecked")
    public static <T> T checkedCast(Object obj, Class<T> type) {
        return type.cast(obj);
    }

    /**
     * Demonstrates generic method usage.
     */
    public static void main(String[] args) {
        // Identity
        String s = identity("hello");
        Integer i = identity(42);
        System.out.println("Identity: " + s + ", " + i);

        // AsList
        List<String> strings = asList("a", "b", "c");
        List<Integer> numbers = asList(1, 2, 3);
        System.out.println("Strings: " + strings);
        System.out.println("Numbers: " + numbers);

        // Sum
        List<Integer> ints = List.of(1, 2, 3, 4, 5);
        System.out.println("Sum: " + sum(ints));

        // Max
        List<Double> doubles = List.of(3.14, 2.71, 1.41);
        System.out.println("Max: " + max(doubles));

        // Max of two
        System.out.println("Max of 10, 20: " + maxOfTwo(10, 20));

        // Map
        List<String> lengths = map(strings, String::length);
        System.out.println("Lengths: " + lengths);

        // Swap
        List<String> mutable = new java.util.ArrayList<>(strings);
        swap(mutable, 0, 2);
        System.out.println("After swap: " + mutable);

        // Checked cast
        Object obj = "hello";
        String str = checkedCast(obj, String.class);
        System.out.println("Cast: " + str);
    }
}
