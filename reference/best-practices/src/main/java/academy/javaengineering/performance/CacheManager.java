package academy.javaengineering.performance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates caching strategies.
 */
public class CacheManager {

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final long defaultTtlMs;

    public CacheManager(long defaultTtlMs) {
        this.defaultTtlMs = defaultTtlMs;
    }

    public record CacheEntry(
        Object value,
        long createdAt,
        long ttlMs,
        AtomicLong accessCount
    ) {}

    public void put(String key, Object value) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis(), defaultTtlMs, new AtomicLong(0)));
    }

    public void put(String key, Object value, long ttlMs) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis(), ttlMs, new AtomicLong(0)));
    }

    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) return null;
        
        if (System.currentTimeMillis() - entry.createdAt() > entry.ttlMs()) {
            cache.remove(key);
            return null;
        }
        
        entry.accessCount().incrementAndGet();
        return entry.value();
    }

    public void evict(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }
}
