package academy.javaengineering.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RedisDataStructuresTest {

    private RedisDataStructuresExample redis;

    @BeforeEach
    void setUp() {
        redis = new RedisDataStructuresExample();
    }

    @Test
    void testStringOperations() {
        redis.setString("name", "John");
        assertEquals("John", redis.getString("name"));
    }

    @Test
    void testHashOperations() {
        redis.hset("user:1", "name", "John");
        redis.hset("user:1", "email", "john@example.com");
        assertEquals("John", redis.hget("user:1", "name"));
        Map<String, String> all = redis.hgetAll("user:1");
        assertEquals(2, all.size());
    }

    @Test
    void testListOperations() {
        redis.lpush("queue", "task1");
        redis.rpush("queue", "task2");
        List<String> list = redis.lrange("queue", 0, -1);
        assertEquals(2, list.size());
    }

    @Test
    void testSetOperations() {
        redis.sadd("tags", "java", "redis", "spring");
        Set<String> members = redis.smembers("tags");
        assertEquals(3, members.size());
    }

    @Test
    void testSortedSetOperations() {
        redis.zadd("leaderboard", 100, "player1");
        redis.zadd("leaderboard", 200, "player2");
        Set<String> range = redis.zrange("leaderboard", 0, -1);
        assertEquals(2, range.size());
    }
}
