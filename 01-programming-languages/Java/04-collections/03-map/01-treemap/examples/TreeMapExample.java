package academy.javaengineering.collections.map.treemap.examples;

import java.util.*;

public class TreeMapExample {
    public static void main(String[] args) {
        System.out.println("=== TreeMap Examples ===\n");
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Banana", 3);
        map.put("Apple", 5);
        map.put("Cherry", 2);
        System.out.println("Sorted: " + map);
        System.out.println("First key: " + map.firstKey());
        System.out.println("Last key: " + map.lastKey());
        System.out.println("HeadMap(Cherry): " + map.headMap("Cherry"));
        System.out.println("TailMap(Banana): " + map.tailMap("Banana"));
    }
}
