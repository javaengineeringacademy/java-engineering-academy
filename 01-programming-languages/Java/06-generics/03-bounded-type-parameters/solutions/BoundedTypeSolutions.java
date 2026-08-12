package academy.javaengineering.generics.bounded-type-parameters.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Bounded Type Parameters Solutions - Complete implementations for all exercises.
 */
public class BoundedTypeSolutions {

    // Exercise 1: Sum method with upper bound
    public static double sum(List<? extends Number> list) {
        double total = 0;
        for (Number num : list) {
            total += num.doubleValue();
        }
        return total;
    }

    // Exercise 2: Statistics class with upper bound
    static class Statistics<T extends Number> {
        private List<T> numbers;

        public Statistics() {
            this.numbers = new ArrayList<>();
        }

        public void add(T number) {
            numbers.add(number);
        }

        public double getAverage() {
            if (numbers.isEmpty()) {
                throw new RuntimeException("No numbers added");
            }
            double sum = 0;
            for (T num : numbers) {
                sum += num.doubleValue();
            }
            return sum / numbers.size();
        }

        @SuppressWarnings("unchecked")
        public T getMax() {
            if (numbers.isEmpty()) {
                throw new RuntimeException("No numbers added");
            }
            T max = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++) {
                if (((Comparable<T>) numbers.get(i)).compareTo(max) > 0) {
                    max = numbers.get(i);
                }
            }
            return max;
        }
    }

    // Exercise 3: Multiple bounds
    public static <T extends Comparable<T> & java.io.Serializable> T findMin(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(min) < 0) {
                min = list.get(i);
            }
        }
        return min;
    }

    // Exercise 4: Recursive bound with countGreaterThan
    public static <T extends Comparable<T>> int countGreaterThan(List<T> list, T value) {
        int count = 0;
        for (T item : list) {
            if (item.compareTo(value) > 0) {
                count++;
            }
        }
        return count;
    }

    // Exercise 5: Copy with lower and upper bounds
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Bounded Type Parameters Solutions ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: sum");
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        System.out.println("Sum of integers: " + sum(integers));
        List<Double> doubles = List.of(1.5, 2.5, 3.0);
        System.out.println("Sum of doubles: " + sum(doubles));

        // Test Exercise 2
        System.out.println("\nExercise 2: Statistics");
        Statistics<Integer> stats = new Statistics<>();
        stats.add(10);
        stats.add(20);
        stats.add(30);
        System.out.println("Average: " + stats.getAverage());
        System.out.println("Max: " + stats.getMax());

        Statistics<Double> doubleStats = new Statistics<>();
        doubleStats.add(1.5);
        doubleStats.add(2.7);
        doubleStats.add(3.14);
        System.out.println("Double average: " + doubleStats.getAverage());

        // Test Exercise 3
        System.out.println("\nExercise 3: findMin");
        System.out.println("Min integer: " + findMin(List.of(5, 3, 8, 1, 9)));
        System.out.println("Min string: " + findMin(List.of("banana", "apple", "cherry")));

        // Test Exercise 4
        System.out.println("\nExercise 4: countGreaterThan");
        System.out.println("Count > 5: " + countGreaterThan(List.of(1, 7, 3, 9, 5), 5));
        System.out.println("Count > 'm': " + countGreaterThan(List.of("apple", "mango", "banana"), "m"));

        // Test Exercise 5
        System.out.println("\nExercise 5: copy");
        List<Integer> src = List.of(1, 2, 3, 4, 5);
        List<Number> dest = new ArrayList<>();
        copy(dest, src);
        System.out.println("Copied list: " + dest);
    }
}
