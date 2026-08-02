package academy.javaengineering.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RedisCachingTest {

    private RedisCachingExample cache;

    @BeforeEach
    void setUp() {
        cache = new RedisCachingExample();
    }

    @Test
    void testPutAndGet() {
        cache.put("key", "value", TimeUnit.MINUTES.toMillis(30));
        assertEquals("value", cache.get("key"));
    }

    @Test
    void testCacheMiss() {
        assertNull(cache.get("nonexistent"));
    }

    @Test
    void testEvict() {
        cache.put("key", "value", TimeUnit.MINUTES.toMillis(30));
        cache.evict("key");
        assertNull(cache.get("key"));
    }

    @Test
    void testClear() {
        cache.put("key1", "value1", TimeUnit.MINUTES.toMillis(30));
        cache.put("key2", "value2", TimeUnit.MINUTES.toMillis(30));
        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    void testLRUEviction() {
        cache.put("key1", "value1", TimeUnit.MINUTES.toMillis(30));
        cache.put("key2", "value2", TimeUnit.MINUTES.toMillis(30));
        cache.get("key1");
        cache.evictLeastRecentlyUsed(1);
        assertEquals(1, cache.size());
    }
}
