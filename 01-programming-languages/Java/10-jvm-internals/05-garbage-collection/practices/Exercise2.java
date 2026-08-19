package academy.javaengineering.jvm.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: Memory Leak Identification
 *
 * Task: Find and fix the memory leak in the Cache class.
 * The cache grows indefinitely because entries are never removed.
 */
public class Exercise2 {

    private static final List<byte[]> cache = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Memory Leak ===\n");

        System.out.println("--- Leaky Cache ---");
        Runtime rt = Runtime.getRuntime();
        for (int i = 0; i < 100; i++) {
            addToCache(i);
            if (i % 20 == 0) {
                System.out.printf("  Iteration %d - Free: %d MB, Total: %d MB%n",
                    i, rt.freeMemory() / (1024 * 1024),
                    rt.totalMemory() / (1024 * 1024));
            }
        }

        // TODO: Identify the leak and fix it
        // TODO: Show that after fix, memory is properly reclaimed
    }

    static void addToCache(int key) {
        // TODO: This is the leaky code - find and fix it
        cache.add(new byte[1024 * 10]); // 10KB per entry, never removed
    }
}
