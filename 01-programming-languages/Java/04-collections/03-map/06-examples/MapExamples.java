package academy.javaengineering.collections.map.examples;

import java.util.*;

public class MapExamples {
    public static void main(String[] args) {
        System.out.println("=== Map Examples ===\n");

        // HashMap
        System.out.println("--- HashMap ---");
        HashMap<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        System.out.println("Scores: " + scores);
        System.out.println("Alice: " + scores.get("Alice"));

        // TreeMap
        System.out.println("\n--- TreeMap ---");
        TreeMap<String, Integer> sorted = new TreeMap<>(scores);
        System.out.println("Sorted: " + sorted);

        // LinkedHashMap
        System.out.println("\n--- LinkedHashMap ---");
        LinkedHashMap<String, Integer> ordered = new LinkedHashMap<>();
        ordered.put("Java", 1);
        ordered.put("Python", 2);
        System.out.println("Ordered: " + ordered);

        // Map operations
        System.out.println("\n--- Operations ---");
        scores.merge("Alice", 5, Integer::sum);
        scores.putIfAbsent("David", 80);
        System.out.println("After merge: " + scores);
    }
}
