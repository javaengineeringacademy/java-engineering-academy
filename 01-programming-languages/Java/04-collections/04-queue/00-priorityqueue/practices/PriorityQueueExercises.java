package academy.javaengineering.collections.queue.priorityqueue.exercises;

import java.util.*;

/**
 * PriorityQueue Exercises
 * 
 * Complete each method using PriorityQueue.
 * Each method has a TODO comment describing what to implement.
 * Run the main method to verify your solutions.
 */
public class PriorityQueueExercises {

    /**
     * Exercise 1: Find K Largest Elements
     * 
     * Given an array of integers and an integer k, return the k largest elements
     * in descending order using a PriorityQueue.
     * 
     * Example: kLargest([3, 1, 5, 12, 2, 11], 3) → [12, 11, 5]
     * 
     * TODO: Implement using PriorityQueue
     * Hint: Use a min-heap of size k, or use a max-heap and extract k times
     */
    public static List<Integer> kLargest(int[] arr, int k) {
        // TODO: Implement this method
        return null;
    }

    /**
     * Exercise 2: Merge Sorted Arrays
     * 
     * Given multiple sorted arrays, merge them into a single sorted array.
     * Use a PriorityQueue to efficiently merge them.
     * 
     * Example: mergeSorted([[1,4,7], [2,5,8], [3,6,9]]) → [1,2,3,4,5,6,7,8,9]
     * 
     * TODO: Implement using PriorityQueue
     * Hint: Add the first element of each array to the PQ, then track which array each element came from
     */
    public static List<Integer> mergeSorted(List<List<Integer>> lists) {
        // TODO: Implement this method
        return null;
    }

    /**
     * Exercise 3: Task Scheduler
     * 
     * Given a list of tasks with priorities (lower number = higher priority),
     * return the order in which tasks should be executed.
     * 
     * Example: taskScheduler([[1, "Low"], [0, "High"], [2, "Medium"]]) → ["High", "Low", "Medium"]
     * 
     * TODO: Implement using PriorityQueue
     * Hint: Create a Task class that implements Comparable, or use a Comparator
     */
    public static List<String> taskScheduler(List<int[]> tasks) {
        // TODO: Implement this method
        return null;
    }

    /**
     * Exercise 4: Kth Smallest Element
     * 
     * Find the kth smallest element in an unsorted array using a PriorityQueue.
     * 
     * Example: kthSmallest([7, 10, 4, 3, 20, 15], 3) → 7
     * 
     * TODO: Implement using PriorityQueue
     * Hint: Use a max-heap of size k, or use a min-heap and extract k times
     */
    public static int kthSmallest(int[] arr, int k) {
        // TODO: Implement this method
        return -1;
    }

    /**
     * Exercise 5: Sort Characters By Frequency
     * 
     * Given a string, sort characters by frequency (most frequent first).
     * If two characters have same frequency, any order is fine.
     * 
     * Example: sortCharacters("tree") → "eert" or "eetr"
     * 
     * TODO: Implement using PriorityQueue
     * Hint: Count frequencies first, then use a PQ with a custom comparator
     */
    public static String sortCharacters(String s) {
        // TODO: Implement this method
        return "";
    }

    /**
     * Exercise 6: Find Median from Data Stream
     * 
     * Design a data structure that supports:
     * - addNum(int num): Add a number to the data structure
     * - findMedian(): Return the median of all numbers added so far
     * 
     * Example:
     * addNum(1), addNum(2), findMedian() → 1.5
     * addNum(3), findMedian() → 2
     * 
     * TODO: Implement using two PriorityQueues (max-heap and min-heap)
     * Hint: Maintain max-heap for lower half, min-heap for upper half
     */
    static class MedianFinder {
        // TODO: Add fields
        
        public MedianFinder() {
            // TODO: Initialize
        }
        
        public void addNum(int num) {
            // TODO: Implement
        }
        
        public double findMedian() {
            // TODO: Implement
            return -1;
        }
    }

    // Test your solutions
    public static void main(String[] args) {
        System.out.println("=== PriorityQueue Exercises ===\n");
        
        // Test Exercise 1
        System.out.println("Exercise 1: K Largest Elements");
        int[] arr1 = {3, 1, 5, 12, 2, 11};
        List<Integer> result1 = kLargest(arr1, 3);
        System.out.println("Input: [3, 1, 5, 12, 2, 11], k=3");
        System.out.println("Expected: [12, 11, 5]");
        System.out.println("Got: " + result1);
        System.out.println();
        
        // Test Exercise 2
        System.out.println("Exercise 2: Merge Sorted Arrays");
        List<List<Integer>> lists = Arrays.asList(
            Arrays.asList(1, 4, 7),
            Arrays.asList(2, 5, 8),
            Arrays.asList(3, 6, 9)
        );
        List<Integer> result2 = mergeSorted(lists);
        System.out.println("Input: [[1,4,7], [2,5,8], [3,6,9]]");
        System.out.println("Expected: [1, 2, 3, 4, 5, 6, 7, 8, 9]");
        System.out.println("Got: " + result2);
        System.out.println();
        
        // Test Exercise 3
        System.out.println("Exercise 3: Task Scheduler");
        List<int[]> tasks = Arrays.asList(
            new int[]{1, 0},  // Low
            new int[]{0, 1},  // High
            new int[]{2, 2}   // Medium
        );
        List<String> result3 = taskScheduler(tasks);
        System.out.println("Input: [[1,Low], [0,High], [2,Medium]]");
        System.out.println("Expected: [High, Low, Medium]");
        System.out.println("Got: " + result3);
        System.out.println();
        
        // Test Exercise 4
        System.out.println("Exercise 4: Kth Smallest Element");
        int[] arr4 = {7, 10, 4, 3, 20, 15};
        int result4 = kthSmallest(arr4, 3);
        System.out.println("Input: [7, 10, 4, 3, 20, 15], k=3");
        System.out.println("Expected: 7");
        System.out.println("Got: " + result4);
        System.out.println();
        
        // Test Exercise 5
        System.out.println("Exercise 5: Sort Characters By Frequency");
        String result5 = sortCharacters("tree");
        System.out.println("Input: \"tree\"");
        System.out.println("Expected: \"eert\" or \"eetr\"");
        System.out.println("Got: " + result5);
        System.out.println();
        
        // Test Exercise 6
        System.out.println("Exercise 6: Find Median from Data Stream");
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        double result6a = medianFinder.findMedian();
        medianFinder.addNum(3);
        double result6b = medianFinder.findMedian();
        System.out.println("addNum(1), addNum(2), findMedian() → 1.5");
        System.out.println("addNum(3), findMedian() → 2.0");
        System.out.println("Got: " + result6a + ", " + result6b);
    }
}
