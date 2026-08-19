package academy.javaengineering.jvm.gc;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Solution 2: Memory Leak Fix
 */
public class Solution2 {

    private static final int MAX_CACHE_SIZE = 50;
    private static final Map<Integer, SoftReference<byte[]>> cache = new LinkedHashMap<>();

    public static void main(String[] args) {
        System.out.println("=== Memory Leak Fix ===\n");

        Runtime rt = Runtime.getRuntime();
        System.out.println("--- Fixed Cache (with eviction) ---");
        for (int i = 0; i < 100; i++) {
            addToCache(i);
            if (i % 20 == 0) {
                System.out.printf("  Iteration %d - Free: %d MB, Total: %d MB, Cache size: %d%n",
                    i, rt.freeMemory() / (1024 * 1024),
                    rt.totalMemory() / (1024 * 1024),
                    cache.size());
            }
        }
        System.out.println("Cache size remains bounded.");
    }

    static void addToCache(int key) {
        cache.put(key, new SoftReference<>(new byte[1024 * 10]));
        if (cache.size() > MAX_CACHE_SIZE) {
            // Remove oldest entry
            Integer oldest = cache.keySet().iterator().next();
            cache.remove(oldest);
        }
    }
}
