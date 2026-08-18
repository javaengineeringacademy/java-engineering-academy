package academy.javaengineering.concurrency.collections.memory;

import java.util.concurrent.*;

public class CollectionsMemory {
    public static void main(String[] args) {
        System.out.println("Concurrent Collections Memory");
        System.out.println("=============================");

        Runtime rt = Runtime.getRuntime();

        long before = rt.freeMemory();
        ConcurrentHashMap<String, Integer> chm = new ConcurrentHashMap<>();
        long after = rt.freeMemory();
        System.out.println("ConcurrentHashMap: ~" + (before - after) + " bytes");

        before = rt.freeMemory();
        java.util.Map<String, Integer> syncMap = java.util.Collections.synchronizedMap(new java.util.HashMap<>());
        after = rt.freeMemory();
        System.out.println("SynchronizedMap wrapper: ~" + (before - after) + " bytes");

        System.out.println("\nCopyOnWriteArrayList creates a full copy on each write.");
        System.out.println("Best for read-heavy, write-rare scenarios.");
    }
}
