package academy.javaengineering.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedisFundamentalsTest {

    private RedisFundamentalsExample redis;

    @BeforeEach
    void setUp() {
        redis = new RedisFundamentalsExample();
    }

    @Test
    void testSetAndGet() {
        redis.set("key", "value");
        assertEquals("value", redis.get("key"));
    }

    @Test
    void testGetNonexistent() {
        assertNull(redis.get("nonexistent"));
    }

    @Test
    void testDelete() {
        redis.set("key", "value");
        assertTrue(redis.del("key"));
        assertNull(redis.get("key"));
    }

    @Test
    void testExists() {
        redis.set("key", "value");
        assertTrue(redis.exists("key"));
        assertFalse(redis.exists("nonexistent"));
    }

    @Test
    void testIncrement() {
        assertEquals(1, redis.incr("counter"));
        assertEquals(2, redis.incr("counter"));
    }
}
