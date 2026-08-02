package academy.javaengineering.generics;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Topic 04: Bounded Type Parameters.
 *
 * <p>This class demonstrates upper bounds, multiple bounds,
 * and recursive type bounds in generic code.</p>
 */
public final class BoundedTypes {

    private BoundedTypes() {
    }

    /**
     * Upper bounded type parameter - T must extend Number.
     */
    public static <T extends Number> double sum(List<T> list) {
        return list.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    /**
     * Recursive bound - T must be comparable to itself.
     */
    public static <T extends Comparable<T>> T max(List<T> list) {
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
     * Multiple bounds - T must be Number AND Comparable.
     */
    public static <T extends Number & Comparable<T>> T maxNumber(List<T> list) {
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
     * Multiple bounds with Serializable.
     */
    public static <T extends Number & Comparable<T> & Serializable> T serializableMax(
            List<T> list) {
        return maxNumber(list);
    }

    /**
     * Clamp value within range using bounded types.
     */
    public static <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }

    /**
     * Check if value is within range.
     */
    public static <T extends Comparable<T>> boolean inRange(T value, T min, T max) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    /**
     * Bounded box class.
     */
    public static class BoundedBox<T extends Number> {
        private final T value;

        public BoundedBox(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public double doubleValue() {
            return value.doubleValue();
        }

        public int intValue() {
            return value.intValue();
        }

        @Override
        public String toString() {
            return "BoundedBox[" + value + "]";
        }
    }

    /**
     * Demonstrates bounded type usage.
     */
    public static void main(String[] args) {
        // Sum with different numeric types
        List<Integer> ints = List.of(1, 2, 3, 4, 5);
        List<Double> doubles = List.of(1.5, 2.5, 3.5);
        System.out.println("Int sum: " + sum(ints));
        System.out.println("Double sum: " + sum(doubles));

        // Max with comparable types
        List<String> names = List.of("Charlie", "Alice", "Bob");
        System.out.println("Max name: " + max(names));

        // Max number
        List<Integer> nums = List.of(3, 1, 4, 1, 5, 9);
        System.out.println("Max number: " + maxNumber(nums));

        // Clamp
        System.out.println("Clamp 15, 0-10: " + clamp(15, 0, 10));
        System.out.println("Clamp -5, 0-10: " + clamp(-5, 0, 10));
        System.out.println("Clamp 5, 0-10: " + clamp(5, 0, 10));

        // In range
        System.out.println("5 in [0,10]: " + inRange(5, 0, 10));
        System.out.println("15 in [0,10]: " + inRange(15, 0, 10));

        // Bounded box
        BoundedBox<Integer> intBox = new BoundedBox<>(42);
        BoundedBox<Double> doubleBox = new BoundedBox<>(3.14);
        System.out.println("Int box: " + intBox);
        System.out.println("Double box: " + doubleBox);
    }
}
