package academy.javaengineering.collections.map.hashmap.cache;

import java.util.*;
import java.util.function.*;

public class CacheExample {
    static class SimpleCache<K, V> {
        private final Map<K, V> cache = new HashMap<>();
        private final Function<K, V> loader;

        public SimpleCache(Function<K, V> loader) { this.loader = loader; }
        public V get(K key) { return cache.computeIfAbsent(key, loader); }
        public void invalidate(K key) { cache.remove(key); }
        public int size() { return cache.size(); }
    }

    public static void main(String[] args) {
        SimpleCache<String, Integer> cache = new SimpleCache<>(String::length);
        System.out.println("Length of Java: " + cache.get("Java"));
        System.out.println("Length of Python: " + cache.get("Python"));
        System.out.println("Cache size: " + cache.size());
    }
}
