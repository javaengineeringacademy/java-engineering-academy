package academy.javaengineering.generics.bounded-type-parameters.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Bounded Type Parameters Exercises
 * Practice using bounded type parameters with extends and super.
 */
public class BoundedTypeExercises {

    // Exercise 1: Create a method that works only with Number subclasses
    // TODO: Implement using upper bound
    public static double sum(List<? extends Number> list) {
        // TODO: Implement
        return 0;
    }

    // Exercise 2: Create a generic class with an upper bound
    // TODO: Implement Statistics class that calculates average of numbers
    static class Statistics<T extends Number> {
        private List<T> numbers;

        public Statistics() {
            this.numbers = new ArrayList<>();
        }

        public void add(T number) {
            // TODO: Implement
        }

        public double getAverage() {
            // TODO: Implement
            return 0;
        }

        public T getMax() {
            // TODO: Implement
            return null;
        }
    }

    // Exercise 3: Create a method with multiple bounds
    // TODO: Implement a method requiring Comparable and Serializable
    public static <T extends Comparable<T> & java.io.Serializable> T findMin(List<T> list) {
        // TODO: Implement
        return null;
    }

    // Exercise 4: Create a generic method with recursive bound
    // TODO: Implement using <T extends Comparable<T>>
    public static <T extends Comparable<T>> int countGreaterThan(List<T> list, T value) {
        // TODO: Implement
        return 0;
    }

    // Exercise 5: Create a generic method that copies between compatible types
    // TODO: Implement with lower bound
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        // TODO: Implement
    }

    public static void main(String[] args) {
        System.out.println("=== Bounded Type Parameters Exercises ===\n");

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

        // Test Exercise 3
        System.out.println("\nExercise 3: findMin");
        System.out.println("Min: " + findMin(List.of(5, 3, 8, 1, 9)));

        // Test Exercise 4
        System.out.println("\nExercise 4: countGreaterThan");
        System.out.println("Count > 5: " + countGreaterThan(List.of(1, 7, 3, 9, 5), 5));

        // Test Exercise 5
        System.out.println("\nExercise 5: copy");
        List<Integer> src = List.of(1, 2, 3);
        List<Number> dest = new ArrayList<>();
        copy(dest, src);
        System.out.println("Copied: " + dest);
    }
}
