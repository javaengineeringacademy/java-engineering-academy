package academy.javaengineering.redis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpringDataRedisExample {

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void set(String key, Object value) {
        store.put(key, value);
        System.out.println("RedisTemplate.set: " + key);
    }

    public Object get(String key) {
        return store.get(key);
    }

    public void delete(String key) {
        store.remove(key);
        System.out.println("RedisTemplate.delete: " + key);
    }

    public boolean hasKey(String key) {
        return store.containsKey(key);
    }

    public void setExpire(String key, long timeout) {
        System.out.println("RedisTemplate.expire: " + key + " with timeout " + timeout);
    }

    public Long increment(String key) {
        Object value = store.get(key);
        long newValue = (value instanceof Long) ? (Long) value + 1 : 1;
        store.put(key, newValue);
        return newValue;
    }

    public List<Object> multiGet(Collection<String> keys) {
        List<Object> values = new ArrayList<>();
        for (String key : keys) {
            values.add(store.get(key));
        }
        return values;
    }

    public void executePipeline(List<String> operations) {
        System.out.println("Executing pipeline with " + operations.size() + " operations");
        for (String op : operations) {
            System.out.println("  " + op);
        }
    }

    public static void main(String[] args) {
        SpringDataRedisExample redis = new SpringDataRedisExample();

        System.out.println("=== Spring Data Redis Demo ===\n");

        redis.set("user:1", Map.of("name", "John", "email", "john@example.com"));
        redis.set("user:2", Map.of("name", "Jane", "email", "jane@example.com"));

        System.out.println("\n--- GET ---");
        System.out.println("user:1: " + redis.get("user:1"));

        System.out.println("\n--- EXISTS ---");
        System.out.println("user:1 exists: " + redis.hasKey("user:1"));

        System.out.println("\n--- INCREMENT ---");
        redis.set("counter", 0L);
        System.out.println("counter: " + redis.increment("counter"));
        System.out.println("counter: " + redis.increment("counter"));

        System.out.println("\n--- MULTI-GET ---");
        List<Object> users = redis.multiGet(List.of("user:1", "user:2"));
        System.out.println("users: " + users);

        System.out.println("\n--- DELETE ---");
        redis.delete("user:2");
        System.out.println("user:2 after delete: " + redis.get("user:2"));
    }
}
