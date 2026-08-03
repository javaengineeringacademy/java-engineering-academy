package academy.javaengineering.performance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Performance Engineering Tests")
class PerformanceTest {

    @Test
    @DisplayName("PerformanceProfiler should measure execution time")
    void testProfiler() {
        var profiler = new PerformanceProfiler();
        var result = profiler.measure("test", () -> {
            int sum = 0;
            for (int i = 0; i < 1000; i++) sum += i;
        });
        
        assertNotNull(result);
        assertEquals("test", result.operationName());
        assertTrue(result.durationMs() >= 0);
    }

    @Test
    @DisplayName("CacheManager should store and retrieve values")
    void testCache() {
        var cache = new CacheManager(60000);
        cache.put("key1", "value1");
        cache.put("key2", 42);
        
        assertEquals("value1", cache.get("key1"));
        assertEquals(42, cache.get("key2"));
        assertEquals(2, cache.size());
    }

    @Test
    @DisplayName("CacheManager should evict expired entries")
    void testCacheEviction() {
        var cache = new CacheManager(1); // 1ms TTL
        cache.put("key", "value");
        
        try { Thread.sleep(10); } catch (InterruptedException e) { }
        
        assertNull(cache.get("key"));
        assertEquals(0, cache.size());
    }
}
