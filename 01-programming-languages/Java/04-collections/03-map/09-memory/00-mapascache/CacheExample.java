package academy.javaengineering.collections.map.cache;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class CacheExample {

    // 1. Basic HashMap Cache
    static class SimpleCache<K, V> {
        private final Map<K, V> cache = new HashMap<>();
        private final Function<K, V> loader;

        public SimpleCache(Function<K, V> loader) { this.loader = loader; }
        public V get(K key) { return cache.computeIfAbsent(key, loader); }
        public void put(K key, V value) { cache.put(key, value); }
        public void invalidate(K key) { cache.remove(key); }
        public int size() { return cache.size(); }
    }

    // 2. Synchronized Cache
    static class SynchronizedCache<K, V> {
        private final Map<K, V> cache = Collections.synchronizedMap(new HashMap<>());
        private final Function<K, V> loader;

        public SynchronizedCache(Function<K, V> loader) { this.loader = loader; }
        public V get(K key) { return cache.computeIfAbsent(key, loader); }
        public void put(K key, V value) { cache.put(key, value); }
        public void invalidate(K key) { cache.remove(key); }
    }

    // 3. ConcurrentHashMap Cache
    static class ConcurrentCache<K, V> {
        private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
        private final Function<K, V> loader;

        public ConcurrentCache(Function<K, V> loader) { this.loader = loader; }
        public V get(K key) { return cache.computeIfAbsent(key, loader); }
        public void put(K key, V value) { cache.put(key, value); }
        public void invalidate(K key) { cache.remove(key); }
    }

    // 4. LRU Cache
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;
        public LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    // 5. TTL Cache
    static class TTLCache<K, V> {
        private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
        private final long ttlMillis;

        public TTLCache(long ttlMillis) { this.ttlMillis = ttlMillis; }

        public void put(K key, V value) {
            cache.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
        }

        public V get(K key) {
            CacheEntry<V> entry = cache.get(key);
            if (entry != null && !entry.isExpired(ttlMillis)) return entry.value;
            cache.remove(key);
            return null;
        }

        static class CacheEntry<V> {
            final V value; final long timestamp;
            CacheEntry(V value, long timestamp) { this.value = value; this.timestamp = timestamp; }
            boolean isExpired(long ttl) { return System.currentTimeMillis() - timestamp > ttl; }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Map as Cache Examples ===\n");

        // Simple cache
        System.out.println("--- Simple Cache ---");
        SimpleCache<String, Integer> simple = new SimpleCache<>(String::length);
        System.out.println("Java length: " + simple.get("Java"));
        System.out.println("Python length: " + simple.get("Python"));
        System.out.println("Cache size: " + simple.size());

        // Synchronized cache
        System.out.println("\n--- Synchronized Cache ---");
        SynchronizedCache<String, String> sync = new SynchronizedCache<>(k -> "Value-" + k);
        System.out.println("Get A: " + sync.get("A"));
        System.out.println("Get B: " + sync.get("B"));

        // ConcurrentHashMap cache
        System.out.println("\n--- ConcurrentHashMap Cache ---");
        ConcurrentCache<String, Integer> concurrent = new ConcurrentCache<>(String::length);
        System.out.println("Hi length: " + concurrent.get("Hi"));
        System.out.println("Hello length: " + concurrent.get("Hello"));

        // LRU cache
        System.out.println("\n--- LRU Cache (capacity=3) ---");
        LRUCache<String, Integer> lru = new LRUCache<>(3);
        lru.put("A", 1); lru.put("B", 2); lru.put("C", 3);
        System.out.println("Before: " + lru);
        lru.get("A");  // Access A, moves to end
        lru.put("D", 4);  // Evicts B (least recently used)
        System.out.println("After access A and put D: " + lru);

        // TTL cache
        System.out.println("\n--- TTL Cache (1 second) ---");
        TTLCache<String, String> ttl = new TTLCache<>(1000);
        ttl.put("session", "active");
        System.out.println("Get session: " + ttl.get("session"));
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        System.out.println("After 1.5s: " + ttl.get("session"));
    }
}
