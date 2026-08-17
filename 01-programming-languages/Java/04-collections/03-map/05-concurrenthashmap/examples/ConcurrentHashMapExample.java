package academy.javaengineering.collections.map.concurrentexamples;

import java.util.concurrent.*;

public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        System.out.println("=== ConcurrentHashMap Examples ===\n");
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        System.out.println("Map: " + map);
        map.merge("Java", 10, Integer::sum);
        System.out.println("After merge: " + map);
    }
}
