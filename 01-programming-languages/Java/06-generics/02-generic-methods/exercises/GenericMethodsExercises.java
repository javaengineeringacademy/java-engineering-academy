package academy.javaengineering.generics.generic-methods.exercises;

import java.util.Arrays;
import java.util.List;

/**
 * Generic Methods Exercises
 * Practice creating and using generic methods.
 */
public class GenericMethodsExercises {

    // Exercise 1: Create a generic method that finds the maximum in a list
    // TODO: Implement the method with proper type bounds
    public static <T extends Comparable<T>> T findMax(List<T> list) {
        // TODO: Implement
        return null;
    }

    // Exercise 2: Create a generic method that swaps two elements in an array
    // TODO: Implement the swap method
    public static <T> void swap(T[] array, int i, int j) {
        // TODO: Implement
    }

    // Exercise 3: Create a generic method that converts an array to a List
    // TODO: Implement the conversion method
    public static <T> List<T> arrayToList(T[] array) {
        // TODO: Implement
        return null;
    }

    // Exercise 4: Create a generic method that counts occurrences of an element
    // TODO: Implement the count method
    public static <T> int countOccurrences(T[] array, T element) {
        // TODO: Implement
        return 0;
    }

    // Exercise 5: Create multiple overloaded generic methods with different bounds
    // TODO: Implement methods that work with Number types
    public static double sumOfList(List<? extends Number> list) {
        // TODO: Implement
        return 0;
    }

    public static <T extends Number> double sumWithBound(List<T> list) {
        // TODO: Implement
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("=== Generic Methods Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: findMax");
        List<Integer> numbers = Arrays.asList(3, 7, 2, 9, 5);
        System.out.println("Max: " + findMax(numbers));
        // Expected: 9

        List<String> strings = Arrays.asList("banana", "apple", "cherry");
        System.out.println("Max string: " + findMax(strings));

        // Test Exercise 2
        System.out.println("\nExercise 2: swap");
        String[] arr = {"A", "B", "C", "D"};
        System.out.println("Before swap: " + Arrays.toString(arr));
        swap(arr, 0, 3);
        System.out.println("After swap: " + Arrays.toString(arr));

        // Test Exercise 3
        System.out.println("\nExercise 3: arrayToList");
        Integer[] intArray = {1, 2, 3, 4, 5};
        List<Integer> intList = arrayToList(intArray);
        System.out.println("List: " + intList);

        // Test Exercise 4
        System.out.println("\nExercise 4: countOccurrences");
        String[] words = {"hello", "world", "hello", "java", "hello"};
        System.out.println("Count of 'hello': " + countOccurrences(words, "hello"));

        // Test Exercise 5
        System.out.println("\nExercise 5: sumOfList");
        List<Integer> intNumbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("Sum: " + sumOfList(intNumbers));
        System.out.println("Sum with bound: " + sumWithBound(intNumbers));
    }
}
