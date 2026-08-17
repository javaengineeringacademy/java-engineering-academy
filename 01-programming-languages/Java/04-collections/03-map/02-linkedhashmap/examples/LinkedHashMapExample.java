package academy.javaengineering.collections.map.linkedhashmap.examples;

import java.util.*;

public class LinkedHashMapExample {
    public static void main(String[] args) {
        System.out.println("=== LinkedHashMap Examples ===\n");
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        System.out.println("Insertion order: " + map);

        LinkedHashMap<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true);
        lru.put("A", 1); lru.put("B", 2); lru.put("C", 3);
        lru.get("A");
        System.out.println("LRU order: " + lru);
    }
}
