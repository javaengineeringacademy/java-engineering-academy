package academy.javaengineering.senior.dataaccess;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CachingDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Caffeine Cache Setup ===");
        Cache<String, String> cache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(Duration.ofMinutes(5))
            .recordStats()
            .build();

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println("Cached key1: " + cache.getIfPresent("key1"));
        System.out.println("Cache size: " + cache.estimatedSize());

        System.out.println();
        System.out.println("=== Cache-Aside Pattern ===");
        Cache<String, User> userCache = Caffeine.newBuilder()
            .maximumSize(50)
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();

        User user1 = cacheAsideGet(userCache, "user_1");
        System.out.println("User from cache: " + user1);

        User user2 = cacheAsideGet(userCache, "user_1");
        System.out.println("User from cache (2nd time): " + user2);
        System.out.println("Same object? " + (user1 == user2));

        System.out.println();
        System.out.println("=== Loading Cache (Auto-load) ===");
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(Duration.ofMinutes(5))
            .build(key -> {
                System.out.println("Loading value for: " + key);
                return "loaded_" + key;
            });

        String val1 = loadingCache.get("auto_key");
        String val2 = loadingCache.get("auto_key");
        System.out.println("Auto-loaded: " + val1);
        System.out.println("From cache: " + val2);

        System.out.println();
        System.out.println("=== Eviction Policies ===");
        Cache<String, String> evictionCache = Caffeine.newBuilder()
            .maximumSize(3)
            .removalListener((String key, String value, RemovalCause cause) ->
                System.out.println("Evicted: " + key + " (" + cause + ")"))
            .build();

        evictionCache.put("a", "1");
        evictionCache.put("b", "2");
        evictionCache.put("c", "3");
        System.out.println("Cache full, adding one more...");
        evictionCache.put("d", "4");

        System.out.println();
        System.out.println("=== Time-based Expiration ===");
        Cache<String, String> timeCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();

        timeCache.put("temp", "data");
        System.out.println("Before expiration: " + timeCache.getIfPresent("temp"));
        Thread.sleep(1500);
        System.out.println("After expiration: " + timeCache.getIfPresent("temp"));

        System.out.println();
        System.out.println("=== Cache Statistics ===");
        System.out.println("Hit count: " + cache.stats().hitCount());
        System.out.println("Miss count: " + cache.stats().missCount());
        System.out.println("Hit rate: " + String.format("%.2f%%", cache.stats().hitRate() * 100));
    }

    private static User cacheAsideGet(Cache<String, User> cache, String userId) {
        User user = cache.getIfPresent(userId);
        if (user == null) {
            System.out.println("Cache miss, loading from database...");
            user = loadUserFromDatabase(userId);
            cache.put(userId, user);
        } else {
            System.out.println("Cache hit!");
        }
        return user;
    }

    private static User loadUserFromDatabase(String userId) {
        return new User(userId, "User " + userId, "user@example.com");
    }

    static class User {
        private final String id;
        private final String name;
        private final String email;

        public User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }

        @Override
        public String toString() {
            return String.format("User{id='%s', name='%s'}", id, name);
        }
    }
}
