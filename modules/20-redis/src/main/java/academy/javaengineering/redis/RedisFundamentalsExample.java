package academy.javaengineering.redis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RedisFundamentalsExample {

    private final Map<String, String> store = new ConcurrentHashMap<>();
    private final Map<String, Long> expiry = new ConcurrentHashMap<>();

    public void set(String key, String value) {
        store.put(key, value);
        System.out.println("SET " + key + " " + value);
    }

    public void set(String key, String value, long ttlSeconds) {
        store.put(key, value);
        expiry.put(key, System.currentTimeMillis() + (ttlSeconds * 1000));
        System.out.println("SET " + key + " " + value + " with TTL " + ttlSeconds + "s");
    }

    public String get(String key) {
        if (expiry.containsKey(key) && System.currentTimeMillis() > expiry.get(key)) {
            store.remove(key);
            expiry.remove(key);
            return null;
        }
        return store.get(key);
    }

    public boolean del(String key) {
        boolean removed = store.remove(key) != null;
        expiry.remove(key);
        return removed;
    }

    public boolean exists(String key) {
        return store.containsKey(key);
    }

    public long incr(String key) {
        long value = Long.parseLong(store.getOrDefault(key, "0"));
        value++;
        store.put(key, String.valueOf(value));
        return value;
    }

    public Map<String, String> info() {
        Map<String, String> info = new HashMap<>();
        info.put("keys", String.valueOf(store.size()));
        info.put("version", "7.0.0");
        return info;
    }

    public static void main(String[] args) {
        RedisFundamentalsExample redis = new RedisFundamentalsExample();

        System.out.println("=== Redis Fundamentals Demo ===\n");

        redis.set("name", "John");
        redis.set("age", "30");
        redis.set("session", "abc123", 3600);

        System.out.println("\n--- GET ---");
        System.out.println("name: " + redis.get("name"));
        System.out.println("age: " + redis.get("age"));
        System.out.println("session: " + redis.get("session"));

        System.out.println("\n--- EXISTS ---");
        System.out.println("name exists: " + redis.exists("name"));

        System.out.println("\n--- INCR ---");
        System.out.println("counter: " + redis.incr("counter"));
        System.out.println("counter: " + redis.incr("counter"));

        System.out.println("\n--- DEL ---");
        redis.del("age");
        System.out.println("age after delete: " + redis.get("age"));

        System.out.println("\n--- INFO ---");
        System.out.println(redis.info());
    }
}
