package academy.javaengineering.redis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RedisCachingExample {

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final Map<String, Long> accessOrder = new ConcurrentHashMap<>();
    private long accessCounter = 0;

    private static class CacheEntry {
        final Object value;
        final long createdAt;
        final long ttlMs;

        CacheEntry(Object value, long ttlMs) {
            this.value = value;
            this.createdAt = System.currentTimeMillis();
            this.ttlMs = ttlMs;
        }

        boolean isExpired() {
            return ttlMs > 0 && System.currentTimeMillis() - createdAt > ttlMs;
        }
    }

    public void put(String key, Object value, long ttlMs) {
        cache.put(key, new CacheEntry(value, ttlMs));
        System.out.println("Cache PUT: " + key);
    }

    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            System.out.println("Cache MISS: " + key);
            return null;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            System.out.println("Cache EXPIRED: " + key);
            return null;
        }
        accessOrder.put(key, accessCounter++);
        System.out.println("Cache HIT: " + key);
        return entry.value;
    }

    public void evict(String key) {
        cache.remove(key);
        accessOrder.remove(key);
        System.out.println("Cache EVICT: " + key);
    }

    public void clear() {
        cache.clear();
        accessOrder.clear();
        System.out.println("Cache CLEAR");
    }

    public void evictLeastRecentlyUsed(int count) {
        accessOrder.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(count)
                .forEach(entry -> {
                    cache.remove(entry.getKey());
                    accessOrder.remove(entry.getKey());
                    System.out.println("LRU EVICT: " + entry.getKey());
                });
    }

    public int size() {
        return cache.size();
    }

    public static void main(String[] args) {
        RedisCachingExample cache = new RedisCachingExample();

        System.out.println("=== Redis Caching Demo ===\n");

        cache.put("user:1", "John", TimeUnit.MINUTES.toMillis(30));
        cache.put("user:2", "Jane", TimeUnit.MINUTES.toMillis(30));
        cache.put("user:3", "Bob", TimeUnit.MINUTES.toMillis(30));

        System.out.println("\n--- Cache Hits ---");
        cache.get("user:1");
        cache.get("user:2");

        System.out.println("\n--- Cache Miss ---");
        cache.get("user:999");

        System.out.println("\n--- LRU Eviction ---");
        cache.evictLeastRecentlyUsed(1);

        System.out.println("\n--- Cache Size ---");
        System.out.println("Size: " + cache.size());
    }
}
