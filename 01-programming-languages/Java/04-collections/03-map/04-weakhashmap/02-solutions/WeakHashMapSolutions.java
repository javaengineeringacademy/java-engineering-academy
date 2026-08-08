package academy.javaengineering.collections.map.weakhashmap.solutions;

import java.util.*;

public class WeakHashMapSolutions {
    public static WeakHashMap<Object, String> createCache() {
        return new WeakHashMap<>();
    }
    public static void main(String[] args) {
        WeakHashMap<Object, String> cache = createCache();
        cache.put(new Object(), "value");
        System.out.println("Cache: " + cache);
    }
}
