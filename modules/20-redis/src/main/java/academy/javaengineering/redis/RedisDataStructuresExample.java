package academy.javaengineering.redis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RedisDataStructuresExample {

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void setString(String key, String value) {
        store.put(key, value);
    }

    public String getString(String key) {
        return (String) store.get(key);
    }

    public void hset(String key, String field, String value) {
        Map<String, String> hash = (Map<String, String>) store.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        hash.put(field, value);
    }

    public String hget(String key, String field) {
        Map<String, String> hash = (Map<String, String>) store.get(key);
        return hash != null ? hash.get(field) : null;
    }

    public Map<String, String> hgetAll(String key) {
        return (Map<String, String>) store.getOrDefault(key, new HashMap<>());
    }

    public void lpush(String key, String value) {
        List<String> list = (List<String>) store.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>()));
        list.add(0, value);
    }

    public void rpush(String key, String value) {
        List<String> list = (List<String>) store.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>()));
        list.add(value);
    }

    public List<String> lrange(String key, long start, long stop) {
        List<String> list = (List<String>) store.getOrDefault(key, Collections.emptyList());
        int end = stop == -1 ? list.size() : (int) stop + 1;
        return list.subList((int) start, Math.min(end, list.size()));
    }

    public void sadd(String key, String... members) {
        Set<String> set = (Set<String>) store.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet());
        Collections.addAll(set, members);
    }

    public Set<String> smembers(String key) {
        return (Set<String>) store.getOrDefault(key, Collections.emptySet());
    }

    public void zadd(String key, double score, String member) {
        TreeMap<Double, Set<String>> zset = (TreeMap<Double, Set<String>>) store.computeIfAbsent(key, k -> new TreeMap<>());
        zset.computeIfAbsent(score, k -> ConcurrentHashMap.newKeySet()).add(member);
    }

    public Set<String> zrange(String key, long start, long stop) {
        TreeMap<Double, Set<String>> zset = (TreeMap<Double, Set<String>>) store.getOrDefault(key, new TreeMap<>());
        Set<String> result = new LinkedHashSet<>();
        long index = 0;
        for (Map.Entry<Double, Set<String>> entry : zset.entrySet()) {
            for (String member : entry.getValue()) {
                if (index >= start && (stop == -1 || index <= stop)) {
                    result.add(member);
                }
                index++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        RedisDataStructuresExample redis = new RedisDataStructuresExample();

        System.out.println("=== Redis Data Structures Demo ===\n");

        System.out.println("--- Strings ---");
        redis.setString("name", "John");
        System.out.println("name: " + redis.getString("name"));

        System.out.println("\n--- Hashes ---");
        redis.hset("user:1", "name", "John");
        redis.hset("user:1", "email", "john@example.com");
        System.out.println("user:1: " + redis.hgetAll("user:1"));

        System.out.println("\n--- Lists ---");
        redis.lpush("queue", "task1");
        redis.lpush("queue", "task2");
        redis.rpush("queue", "task3");
        System.out.println("queue: " + redis.lrange("queue", 0, -1));

        System.out.println("\n--- Sets ---");
        redis.sadd("tags", "java", "redis", "spring");
        System.out.println("tags: " + redis.smembers("tags"));

        System.out.println("\n--- Sorted Sets ---");
        redis.zadd("leaderboard", 100, "player1");
        redis.zadd("leaderboard", 200, "player2");
        redis.zadd("leaderboard", 150, "player3");
        System.out.println("leaderboard: " + redis.zrange("leaderboard", 0, -1));
    }
}
