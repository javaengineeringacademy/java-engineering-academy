package academy.javaengineering.collections.map.hashmap.examples;

import java.util.*;

public class HashMapExample {
    public static void main(String[] args) {
        System.out.println("=== HashMap Examples ===\n");

        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        System.out.println("Map: " + map);
        System.out.println("Get Java: " + map.get("Java"));
        System.out.println("ContainsKey: " + map.containsKey("Java"));
        System.out.println("ContainsValue 2: " + map.containsValue(2));

        map.putIfAbsent("JavaScript", 4);
        map.merge("Java", 10, Integer::sum);
        System.out.println("After merge: " + map);

        map.remove("C++");
        System.out.println("After remove: " + map);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
