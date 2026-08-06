package academy.javaengineering.exercises;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Exercises: List (ArrayList, LinkedList) Operations
 *
 * Complete the TODO sections below.
 */
public class ListExercises {

    // TODO 1: Remove duplicates from a list while preserving order
    public <T> List<T> removeDuplicates(List<T> list) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 2: Interleave two lists
    // Given [1, 2, 3] and [a, b, c], return [1, a, 2, b, 3, c]
    // If lists are different lengths, append remaining elements
    public <T> List<T> interleave(List<T> list1, List<T> list2) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 3: Rotate list by k positions to the right
    // [1, 2, 3, 4, 5] rotated by 2 becomes [4, 5, 1, 2, 3]
    public <T> List<T> rotate(List<T> list, int k) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 4: Chunk a list into sublists of size n
    // [1, 2, 3, 4, 5, 6, 7] chunked by 3 becomes [[1, 2, 3], [4, 5, 6], [7]]
    public <T> List<List<T>> chunk(List<T> list, int chunkSize) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 5: Find the intersection of two lists (elements present in both)
    // Preserve order from first list, no duplicates
    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 6: Flatten a nested list
    // [[1, 2], [3], [4, 5, 6]] becomes [1, 2, 3, 4, 5, 6]
    public <T> List<T> flatten(List<List<T>> nested) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        ListExercises exercises = new ListExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ListExercises Tests ===\n");

        // Test 1
        total++;
        List<Integer> dupList = List.of(1, 2, 2, 3, 3, 3, 4);
        List<Integer> noDups = exercises.removeDuplicates(dupList);
        if (noDups.equals(List.of(1, 2, 3, 4))) {
            System.out.println("Test 1 PASSED: removeDuplicates");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: removeDuplicates - got " + noDups);
        }

        // Test 2
        total++;
        List<String> l1 = List.of("a", "b", "c");
        List<Integer> l2 = List.of(1, 2, 3);
        List<Object> interleaved = exercises.interleave(new ArrayList<>(l1), new ArrayList<>(l2));
        List<Object> expected = List.of("a", 1, "b", 2, "c", 3);
        if (interleaved.equals(expected)) {
            System.out.println("Test 2 PASSED: interleave");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: interleave - got " + interleaved);
        }

        // Test 3
        total++;
        List<Integer> rotateList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> rotated = exercises.rotate(rotateList, 2);
        if (rotated.equals(List.of(4, 5, 1, 2, 3))) {
            System.out.println("Test 3 PASSED: rotate");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: rotate - got " + rotated);
        }

        // Test 4
        total++;
        List<Integer> chunkList = List.of(1, 2, 3, 4, 5, 6, 7);
        List<List<Integer>> chunked = exercises.chunk(chunkList, 3);
        if (chunked.size() == 3
            && chunked.get(0).equals(List.of(1, 2, 3))
            && chunked.get(1).equals(List.of(4, 5, 6))
            && chunked.get(2).equals(List.of(7))) {
            System.out.println("Test 4 PASSED: chunk");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: chunk - got " + chunked);
        }

        // Test 5
        total++;
        List<Integer> inter1 = List.of(1, 2, 3, 4, 5);
        List<Integer> inter2 = List.of(3, 4, 5, 6, 7);
        List<Integer> intersection = exercises.intersection(inter1, inter2);
        if (intersection.equals(List.of(3, 4, 5))) {
            System.out.println("Test 5 PASSED: intersection");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: intersection - got " + intersection);
        }

        // Test 6
        total++;
        List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3), List.of(4, 5, 6));
        List<Integer> flat = exercises.flatten(nested);
        if (flat.equals(List.of(1, 2, 3, 4, 5, 6))) {
            System.out.println("Test 6 PASSED: flatten");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: flatten - got " + flat);
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
