package academy.javaengineering.collections.examples;

import java.util.*;
import java.util.concurrent.*;

public class MapExamples {
    public static void main(String[] args) {
        System.out.println("=== Map Examples ===\n");

        // HashMap
        System.out.println("--- HashMap ---");
        HashMap<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        scores.put("David", 78);
        System.out.println("Scores: " + scores);
        System.out.println("Alice's score: " + scores.get("Alice"));
        System.out.println("Contains Bob: " + scores.containsKey("Bob"));
        System.out.println("Contains score 95: " + scores.containsValue(95));
        scores.putIfAbsent("Eve", 88);
        scores.merge("Alice", 5, Integer::sum); // Add 5 to Alice's score
        System.out.println("After merge: " + scores);

        // Map operations
        System.out.println("\n--- Map Operations ---");
        // Iterate entries
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        // GetOrDefault
        int score = scores.getOrDefault("Unknown", 0);
        System.out.println("Unknown score: " + score);

        // Replace
        scores.replace("Bob", 90);
        System.out.println("After replace: " + scores);

        // TreeMap - sorted by keys
        System.out.println("\n--- TreeMap ---");
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("Banana", 3);
        treeMap.put("Apple", 5);
        treeMap.put("Cherry", 2);
        treeMap.put("Date", 8);
        System.out.println("Sorted: " + treeMap);
        System.out.println("First key: " + treeMap.firstKey());
        System.out.println("Last key: " + treeMap.lastKey());

        // LinkedHashMap - maintains insertion order
        System.out.println("\n--- LinkedHashMap ---");
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Java", 1);
        linkedHashMap.put("Python", 2);
        linkedHashMap.put("C++", 3);
        System.out.println("Insertion order: " + linkedHashMap);

        // Access order (LRU cache behavior)
        LinkedHashMap<String, Integer> lruMap = new LinkedHashMap<>(16, 0.75f, true);
        lruMap.put("A", 1);
        lruMap.put("B", 2);
        lruMap.put("C", 3);
        lruMap.get("A"); // Move A to end
        System.out.println("After access A: " + lruMap);

        // ConcurrentHashMap
        System.out.println("\n--- ConcurrentHashMap ---");
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put("One", 1);
        concurrentMap.put("Two", 2);
        concurrentMap.put("Three", 3);
        System.out.println("Concurrent: " + concurrentMap);
    }
}
