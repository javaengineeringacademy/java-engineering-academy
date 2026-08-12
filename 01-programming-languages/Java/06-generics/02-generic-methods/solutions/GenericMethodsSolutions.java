package academy.javaengineering.generics.generic-methods.solutions;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Generic Methods Solutions - Complete implementations for all exercises.
 */
public class GenericMethodsSolutions {

    // Exercise 1: Generic method to find maximum
    public static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    // Exercise 2: Generic method to swap elements
    public static <T> void swap(T[] array, int i, int j) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (i < 0 || i >= array.length || j < 0 || j >= array.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Exercise 3: Generic method to convert array to List
    public static <T> List<T> arrayToList(T[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        List<T> list = new ArrayList<>();
        for (T element : array) {
            list.add(element);
        }
        return list;
    }

    // Exercise 4: Generic method to count occurrences
    public static <T> int countOccurrences(T[] array, T element) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        int count = 0;
        for (T item : array) {
            if (item == null ? element == null : item.equals(element)) {
                count++;
            }
        }
        return count;
    }

    // Exercise 5: Generic methods with Number bounds
    public static double sumOfList(List<? extends Number> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    public static <T extends Number> double sumWithBound(List<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (T num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("=== Generic Methods Solutions ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: findMax");
        List<Integer> numbers = Arrays.asList(3, 7, 2, 9, 5);
        System.out.println("Max: " + findMax(numbers));
        List<String> strings = Arrays.asList("banana", "apple", "cherry");
        System.out.println("Max string: " + findMax(strings));

        // Test Exercise 2
        System.out.println("\nExercise 2: swap");
        String[] arr = {"A", "B", "C", "D"};
        System.out.println("Before: " + Arrays.toString(arr));
        swap(arr, 0, 3);
        System.out.println("After:  " + Arrays.toString(arr));

        // Test Exercise 3
        System.out.println("\nExercise 3: arrayToList");
        Integer[] intArray = {1, 2, 3, 4, 5};
        List<Integer> intList = arrayToList(intArray);
        System.out.println("List: " + intList);

        // Test Exercise 4
        System.out.println("\nExercise 4: countOccurrences");
        String[] words = {"hello", "world", "hello", "java", "hello"};
        System.out.println("Count of 'hello': " + countOccurrences(words, "hello"));
        System.out.println("Count of 'java': " + countOccurrences(words, "java"));

        // Test Exercise 5
        System.out.println("\nExercise 5: sumOfList");
        List<Integer> intNumbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("Sum (wildcard): " + sumOfList(intNumbers));
        System.out.println("Sum (bounded):  " + sumWithBound(intNumbers));

        List<Double> doubleNumbers = Arrays.asList(1.5, 2.5, 3.0);
        System.out.println("Double sum: " + sumOfList(doubleNumbers));
    }
}
