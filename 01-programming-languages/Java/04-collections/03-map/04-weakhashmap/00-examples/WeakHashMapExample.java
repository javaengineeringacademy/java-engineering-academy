package academy.javaengineering.collections.map.weakhashmap.examples;

import java.util.*;

public class WeakHashMapExample {
    public static void main(String[] args) {
        System.out.println("=== WeakHashMap Examples ===\n");
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        String key = new String("Java");
        map.put(key, 1);
        System.out.println("Before GC: " + map);
        key = null;
        System.gc();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        System.out.println("After GC: " + map);
    }
}
