package academy.javaengineering.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * Exercises: Wildcards and Type Erasure
 *
 * Complete the TODO sections below.
 */
public class WildcardExercises {

    // TODO 1: Implement a method that sums all Numbers in a list
    // Use upper bounded wildcard: List<? extends Number>
    public double sumNumbers(List<? extends Number> numbers) {
        // TODO: implement
        return 0.0;
    }

    // TODO 2: Implement a method that copies elements from source to dest
    // Use wildcards: source is List<? extends T>, dest is List<? super T>
    public <T> void copy(List<? extends T> source, List<? super T> dest) {
        // TODO: implement
    }

    // TODO 3: Implement a method that finds the max in a list
    // Use upper bounded wildcard
    public <T extends Comparable<? super T>> T findMax(List<? extends T> list) {
        // TODO: implement
        return null;
    }

    // TODO 4: Implement a method that adds elements to a list
    // Use lower bounded wildcard: List<? super T>
    public <T> void addElements(List<? super T> list, T... elements) {
        // TODO: implement
    }

    // TODO 5: Demonstrate type erasure
    // Return the name of the raw type of a generic class
    // For List<String>, return "java.util.List"
    // For Map<Integer, String>, return "java.util.Map"
    public String getRawTypeName(Object obj) {
        // TODO: implement using getClass().getName()
        return "";
    }

    // TODO 6: Implement a method that merges two sorted lists
    // Both lists are sorted in ascending order
    // Result should also be sorted
    public <T extends Comparable<? super T>> List<T> mergeSorted(
            List<? extends T> list1, List<? extends T> list2) {
        // TODO: implement
        return new ArrayList<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        WildcardExercises exercises = new WildcardExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== WildcardExercises Tests ===\n");

        // Test 1
        total++;
        List<Integer> ints = List.of(1, 2, 3, 4, 5);
        List<Double> doubles = List.of(1.5, 2.5, 3.5);
        double intSum = exercises.sumNumbers(ints);
        double doubleSum = exercises.sumNumbers(doubles);
        if (intSum == 15.0 && doubleSum == 7.5) {
            System.out.println("Test 1 PASSED: sumNumbers");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: sumNumbers - int=" + intSum + " double=" + doubleSum);
        }

        // Test 2
        total++;
        List<Integer> source = List.of(1, 2, 3);
        List<Number> dest = new ArrayList<>();
        exercises.copy(source, dest);
        if (dest.size() == 3 && dest.get(0).intValue() == 1) {
            System.out.println("Test 2 PASSED: copy");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: copy - dest=" + dest);
        }

        // Test 3
        total++;
        Integer maxInt = exercises.findMax(List.of(3, 1, 4, 1, 5));
        String maxStr = exercises.findMax(List.of("apple", "banana", "cherry"));
        if (maxInt != null && maxInt == 5 && "cherry".equals(maxStr)) {
            System.out.println("Test 3 PASSED: findMax");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: findMax");
        }

        // Test 4
        total++;
        List<Number> numList = new ArrayList<>();
        exercises.addElements(numList, 1, 2.5, 3L);
        if (numList.size() == 3) {
            System.out.println("Test 4 PASSED: addElements");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: addElements - size=" + numList.size());
        }

        // Test 5
        total++;
        String listName = exercises.getRawTypeName(new ArrayList<String>());
        String mapName = exercises.getRawTypeName(new HashMap<Integer, String>());
        if ("java.util.ArrayList".equals(listName) && "java.util.HashMap".equals(mapName)) {
            System.out.println("Test 5 PASSED: getRawTypeName");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: getRawTypeName - list=" + listName + " map=" + mapName);
        }

        // Test 6
        total++;
        List<Integer> l1 = List.of(1, 3, 5);
        List<Integer> l2 = List.of(2, 4, 6);
        List<Integer> merged = exercises.mergeSorted(l1, l2);
        if (merged.equals(List.of(1, 2, 3, 4, 5, 6))) {
            System.out.println("Test 6 PASSED: mergeSorted");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: mergeSorted - " + merged);
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
