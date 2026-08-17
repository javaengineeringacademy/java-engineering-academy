package academy.javaengineering.collections.map.linkedhashmap.solutions;

import java.util.*;

public class LinkedHashMapSolutions {
    public static <K, V> LinkedHashMap<K, V> createLRUCache(int capacity) {
        return new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> lru = createLRUCache(3);
        lru.put("A", 1); lru.put("B", 2); lru.put("C", 3); lru.put("D", 4);
        System.out.println(lru);
    }
}
