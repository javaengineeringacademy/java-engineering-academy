package academy.javaengineering.generics.wildcards.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Wildcards Exercises
 * Practice using upper-bounded, lower-bounded, and unbounded wildcards.
 */
public class WildcardsExercises {

    // Exercise 1: Create a method that prints any list (unbounded wildcard)
    // TODO: Implement using <?>
    public static void printList(List<?> list) {
        // TODO: Implement
    }

    // Exercise 2: Create a method that sums numbers (upper-bounded wildcard)
    // TODO: Implement using <? extends Number>
    public static double sumList(List<? extends Number> list) {
        // TODO: Implement
        return 0;
    }

    // Exercise 3: Create a method that adds integers to a list (lower-bounded wildcard)
    // TODO: Implement using <? super Integer>
    public static void addNumbers(List<? super Integer> list, int count) {
        // TODO: Implement
    }

    // Exercise 4: Create a method that copies elements between lists
    // TODO: Implement using both upper and lower bounds
    public static <T> void copyList(List<? super T> dest, List<? extends T> src) {
        // TODO: Implement
    }

    // Exercise 5: Create a method that finds the max using wildcards
    // TODO: Implement with upper bound and Comparable
    public static <T extends Comparable<? super T>> T findMax(List<? extends T> list) {
        // TODO: Implement
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== Wildcards Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: printList");
        printList(List.of(1, 2, 3));
        printList(List.of("a", "b", "c"));
        printList(List.of(1.0, 2.0, 3.0));

        // Test Exercise 2
        System.out.println("\nExercise 2: sumList");
        System.out.println("Sum: " + sumList(List.of(1, 2, 3, 4, 5)));
        System.out.println("Sum: " + sumList(List.of(1.5, 2.5, 3.0)));

        // Test Exercise 3
        System.out.println("\nExercise 3: addNumbers");
        List<Number> numberList = new ArrayList<>();
        addNumbers(numberList, 5);
        System.out.println("Added numbers: " + numberList);

        // Test Exercise 4
        System.out.println("\nExercise 4: copyList");
        List<Integer> src = List.of(1, 2, 3);
        List<Number> dest = new ArrayList<>();
        copyList(dest, src);
        System.out.println("Copied: " + dest);

        // Test Exercise 5
        System.out.println("\nExercise 5: findMax");
        System.out.println("Max: " + findMax(List.of(3, 7, 2, 9, 5)));
        System.out.println("Max: " + findMax(List.of("banana", "apple", "cherry")));
    }
}
