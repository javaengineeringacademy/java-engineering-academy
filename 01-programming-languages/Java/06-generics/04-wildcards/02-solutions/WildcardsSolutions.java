package academy.javaengineering.generics.wildcards.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Wildcards Solutions - Complete implementations for all exercises.
 */
public class WildcardsSolutions {

    // Exercise 1: Unbounded wildcard - prints any list
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    // Exercise 2: Upper-bounded wildcard - sums numbers
    public static double sumList(List<? extends Number> list) {
        double sum = 0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    // Exercise 3: Lower-bounded wildcard - adds integers
    public static void addNumbers(List<? super Integer> list, int count) {
        for (int i = 1; i <= count; i++) {
            list.add(i);
        }
    }

    // Exercise 4: Copy with both bounds (PECS principle)
    public static <T> void copyList(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    // Exercise 5: Find max with wildcard and recursive bound
    public static <T extends Comparable<? super T>> T findMax(List<? extends T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T max = list.iterator().next();
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println("=== Wildcards Solutions ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: printList");
        System.out.print("Integers: ");
        printList(List.of(1, 2, 3));
        System.out.print("Strings: ");
        printList(List.of("a", "b", "c"));
        System.out.print("Doubles: ");
        printList(List.of(1.0, 2.0, 3.0));

        // Test Exercise 2
        System.out.println("\nExercise 2: sumList");
        System.out.println("Integer sum: " + sumList(List.of(1, 2, 3, 4, 5)));
        System.out.println("Double sum: " + sumList(List.of(1.5, 2.5, 3.0)));
        System.out.println("Mixed sum: " + sumList(List.of(1, 2.5, 3L)));

        // Test Exercise 3
        System.out.println("\nExercise 3: addNumbers");
        List<Number> numberList = new ArrayList<>();
        addNumbers(numberList, 5);
        System.out.println("Added to Number list: " + numberList);
        List<Object> objectList = new ArrayList<>();
        addNumbers(objectList, 3);
        System.out.println("Added to Object list: " + objectList);

        // Test Exercise 4
        System.out.println("\nExercise 4: copyList");
        List<Integer> src = List.of(1, 2, 3, 4, 5);
        List<Number> dest = new ArrayList<>();
        copyList(dest, src);
        System.out.println("Copied: " + dest);

        // Test Exercise 5
        System.out.println("\nExercise 5: findMax");
        System.out.println("Max integer: " + findMax(List.of(3, 7, 2, 9, 5)));
        System.out.println("Max string: " + findMax(List.of("banana", "apple", "cherry")));
    }
}
