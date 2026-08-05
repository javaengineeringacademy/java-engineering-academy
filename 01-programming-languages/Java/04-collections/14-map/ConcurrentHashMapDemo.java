import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Arrays;

/**
 * Demonstrates ConcurrentHashMap operations for thread-safe concurrent access.
 * ConcurrentHashMap provides better performance than Collections.synchronizedMap().
 */
public class ConcurrentHashMapDemo {

    public static void main(String[] args) throws InterruptedException {
        demonstrateBasicOperations();
        demonstrateAtomicOperations();
        demonstrateConcurrentAccess();
        demonstrateAdvancedPatterns();
    }

    /**
     * Demonstrates basic ConcurrentHashMap operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== ConcurrentHashMap Basic Operations ===");

        // Creation
        ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();

        // Thread-safe operations
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        scores.putIfAbsent("Diana", 88);

        System.out.println("Map: " + scores);
        System.out.println("Size: " + scores.size());

        // Access (non-blocking)
        System.out.println("Alice's score: " + scores.get("Alice"));

        // Atomic updates
        scores.compute("Alice", (key, value) -> value + 5);
        scores.merge("Charlie", 3, Integer::sum);

        System.out.println("After atomic operations: " + scores);
        System.out.println();
    }

    /**
     * Demonstrates atomic operations in ConcurrentHashMap.
     */
    private static void demonstrateAtomicOperations() {
        System.out.println("=== Atomic Operations ===");

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // computeIfAbsent - atomic lazy initialization
        map.computeIfAbsent("counter", k -> 0);
        map.compute("counter", (k, v) -> v + 1);
        System.out.println("Counter after compute: " + map.get("counter"));

        // merge - atomic accumulation
        map.merge("counter", 5, Integer::sum);
        System.out.println("Counter after merge: " + map.get("counter"));

        // computeIfPresent - atomic update if present
        map.computeIfPresent("counter", (k, v) -> v * 2);
        System.out.println("Counter after double: " + map.get("counter"));

        // replace - conditional replacement
        map.replace("counter", 6, 12);
        System.out.println("Counter after replace: " + map.get("counter"));
        System.out.println();
    }

    /**
     * Demonstrates thread-safe concurrent access.
     */
    private static void demonstrateConcurrentAccess() throws InterruptedException {
        System.out.println("=== Concurrent Access ===");

        ConcurrentHashMap<Integer, Integer> counter = new ConcurrentHashMap<>();
        int threadCount = 10;
        int incrementsPerThread = 1000;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.merge(j, 1, Integer::sum);
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Counter size: " + counter.size());
        System.out.println("Sample values:");
        counter.entrySet().stream()
                .limit(5)
                .forEach(entry ->
                        System.out.println("  Key: " + entry.getKey() + ", Value: " + entry.getValue())
                );
        System.out.println();
    }

    /**
     * Demonstrates advanced ConcurrentHashMap patterns.
     */
    private static void demonstrateAdvancedPatterns() {
        System.out.println("=== Advanced Patterns ===");

        // Pattern 1: Thread-safe word counter
        ConcurrentHashMap<String, AtomicLong> wordCount = new ConcurrentHashMap<>();
        String[] words = {"java", "is", "great", "java", "is", "fun", "java"};

        Arrays.stream(words).parallel().forEach(word ->
                wordCount.computeIfAbsent(word, k -> new AtomicLong()).incrementAndGet()
        );

        System.out.println("Word counts:");
        wordCount.forEach((word, count) ->
                System.out.println("  " + word + ": " + count.get())
        );

        // Pattern 2: Thread-safe cache with TTL
        System.out.println("\nTTL Cache:");
        TTLCache<String, String> cache = new TTLCache<>(1000, 5);
        cache.put("key1", "value1");
        System.out.println("Get key1: " + cache.get("key1"));

        // Pattern 3: Nested ConcurrentHashMap
        ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> nestedMap = new ConcurrentHashMap<>();
        nestedMap.computeIfAbsent("group1", k -> new ConcurrentHashMap<>()).put("item1", 100);
        nestedMap.computeIfAbsent("group1", k -> new ConcurrentHashMap<>()).put("item2", 200);
        nestedMap.computeIfAbsent("group2", k -> new ConcurrentHashMap<>()).put("item1", 300);

        System.out.println("\nNested map:");
        nestedMap.forEach((group, items) -> {
            System.out.println("  " + group + ":");
            items.forEach((item, value) ->
                    System.out.println("    " + item + ": " + value)
            );
        });
    }

    /**
     * Thread-safe cache with TTL (Time-To-Live).
     */
    static class TTLCache<K, V> {
        private final ConcurrentHashMap<K, CacheEntry<V>> cache;
        private final long ttlMillis;

        public TTLCache(long ttlMillis, int initialCapacity) {
            this.cache = new ConcurrentHashMap<>(initialCapacity);
            this.ttlMillis = ttlMillis;
        }

        public void put(K key, V value) {
            cache.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
        }

        public V get(K key) {
            CacheEntry<V> entry = cache.get(key);
            if (entry == null) return null;
            if (System.currentTimeMillis() - entry.timestamp > ttlMillis) {
                cache.remove(key);
                return null;
            }
            return entry.value;
        }

        static class CacheEntry<V> {
            final V value;
            final long timestamp;

            CacheEntry(V value, long timestamp) {
                this.value = value;
                this.timestamp = timestamp;
            }
        }
    }
}
