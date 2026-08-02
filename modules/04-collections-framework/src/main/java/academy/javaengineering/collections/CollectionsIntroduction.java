package academy.javaengineering.collections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Demonstrates basic Java Collections Framework usage.
 * Covers List, Set, Map, and Queue interfaces with their common implementations.
 */
public class CollectionsIntroduction {

    public static void main(String[] args) {
        demonstrateList();
        demonstrateSet();
        demonstrateMap();
        demonstrateQueue();
        demonstrateConversions();
    }

    /**
     * Demonstrates List interface - ordered, allows duplicates.
     */
    private static void demonstrateList() {
        System.out.println("=== List Demonstration ===");

        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate allowed

        System.out.println("List: " + fruits);
        System.out.println("Size: " + fruits.size());
        System.out.println("Contains Apple: " + fruits.contains("Apple"));
        System.out.println("Index of Banana: " + fruits.indexOf("Banana"));

        // Remove by value
        fruits.remove("Banana");
        System.out.println("After removing Banana: " + fruits);

        // Remove by index
        fruits.remove(0);
        System.out.println("After removing index 0: " + fruits);

        // Iterate
        System.out.print("Iterating: ");
        for (String fruit : fruits) {
            System.out.print(fruit + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates Set interface - no duplicates.
     */
    private static void demonstrateSet() {
        System.out.println("=== Set Demonstration ===");

        Set<Integer> numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(1); // Duplicate ignored
        numbers.add(2); // Duplicate ignored

        System.out.println("Set: " + numbers);
        System.out.println("Size (no duplicates): " + numbers.size());
        System.out.println("Contains 1: " + numbers.contains(1));
        System.out.println("Contains 4: " + numbers.contains(4));
        System.out.println();
    }

    /**
     * Demonstrates Map interface - key-value pairs.
     */
    private static void demonstrateMap() {
        System.out.println("=== Map Demonstration ===");

        Map<String, Integer> ages = new HashMap<>();
        ages.put("Alice", 30);
        ages.put("Bob", 25);
        ages.put("Charlie", 35);

        System.out.println("Map: " + ages);
        System.out.println("Size: " + ages.size());
        System.out.println("Alice's age: " + ages.get("Alice"));
        System.out.println("Contains Bob: " + ages.containsKey("Bob"));

        // Iterate entries
        System.out.println("All entries:");
        for (Map.Entry<String, Integer> entry : ages.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        // Update value
        ages.put("Alice", 31);
        System.out.println("Updated Alice's age: " + ages.get("Alice"));

        // Remove entry
        ages.remove("Charlie");
        System.out.println("After removing Charlie: " + ages);
        System.out.println();
    }

    /**
     * Demonstrates Queue interface - FIFO processing.
     */
    private static void demonstrateQueue() {
        System.out.println("=== Queue Demonstration ===");

        Queue<String> queue = new LinkedList<>();
        queue.offer("Order 1");
        queue.offer("Order 2");
        queue.offer("Order 3");

        System.out.println("Queue: " + queue);
        System.out.println("Peek: " + queue.peek());

        System.out.println("Processing orders:");
        while (!queue.isEmpty()) {
            System.out.println("  Processing: " + queue.poll());
        }
        System.out.println("Queue empty: " + queue.isEmpty());
        System.out.println();
    }

    /**
     * Demonstrates converting between collection types.
     */
    private static void demonstrateConversions() {
        System.out.println("=== Collection Conversions ===");

        // List to Set (remove duplicates)
        List<String> listWithDuplicates = List.of("A", "B", "C", "A", "B");
        Set<String> uniqueElements = new HashSet<>(listWithDuplicates);
        System.out.println("Original list: " + listWithDuplicates);
        System.out.println("Unique set: " + uniqueElements);

        // Set to List
        List<String> sortedList = new ArrayList<>(uniqueElements);
        sortedList.sort(String::compareTo);
        System.out.println("Sorted list: " + sortedList);

        // Map to List of entries
        Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        System.out.println("Map entries: " + entries);

        // List to Map
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Map<String, Integer> nameLengths = new HashMap<>();
        for (String name : names) {
            nameLengths.put(name, name.length());
        }
        System.out.println("Name lengths: " + nameLengths);
    }
}
