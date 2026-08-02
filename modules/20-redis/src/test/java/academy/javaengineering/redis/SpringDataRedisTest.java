package academy.javaengineering.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpringDataRedisTest {

    private SpringDataRedisExample redis;

    @BeforeEach
    void setUp() {
        redis = new SpringDataRedisExample();
    }

    @Test
    void testSetAndGet() {
        redis.set("key", "value");
        assertEquals("value", redis.get("key"));
    }

    @Test
    void testDelete() {
        redis.set("key", "value");
        redis.delete("key");
        assertNull(redis.get("key"));
    }

    @Test
    void testHasKey() {
        redis.set("key", "value");
        assertTrue(redis.hasKey("key"));
        assertFalse(redis.hasKey("nonexistent"));
    }

    @Test
    void testIncrement() {
        redis.set("counter", 0L);
        assertEquals(1L, redis.increment("counter"));
        assertEquals(2L, redis.increment("counter"));
    }

    @Test
    void testMultiGet() {
        redis.set("key1", "value1");
        redis.set("key2", "value2");
        List<Object> values = redis.multiGet(List.of("key1", "key2"));
        assertEquals(2, values.size());
    }
}
