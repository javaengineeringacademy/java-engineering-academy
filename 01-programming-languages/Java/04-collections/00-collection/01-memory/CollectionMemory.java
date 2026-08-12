package academy.javaengineering.collections.memory;

import java.util.*;
import java.lang.ref.*;

public class CollectionMemory {

    public static void main(String[] args) {
        System.out.println("=== Collection Memory Management ===\n");

        // 1. WeakReference for Cache
        System.out.println("--- WeakReference Cache ---");
        Map<String, WeakReference<byte[]>> cache = new HashMap<>();
        String key = "data";
        cache.put(key, new WeakReference<>(new byte[1024 * 1024])); // 1MB
        System.out.println("Cache size: " + cache.size());
        System.out.println("Reference type: WeakReference (GC can collect)");

        // 2. SoftReference for Memory-Sensitive Cache
        System.out.println("\n--- SoftReference Cache ---");
        List<SoftReference<byte[]>> softCache = new ArrayList<>();
        softCache.add(new SoftReference<>(new byte[1024 * 1024]));
        System.out.println("Soft cache size: " + softCache.size());
        System.out.println("SoftReference cleared only when memory low");

        // 3. PhantomReference for Cleanup
        System.out.println("\n--- PhantomReference Cleanup ---");
        ReferenceQueue<byte[]> refQueue = new ReferenceQueue<>();
        Map<Object, PhantomReference<byte[]>> phantomMap = new HashMap<>();
        byte[] data = new byte[1024 * 1024];
        phantomMap.put(data, new PhantomReference<>(data, refQueue));
        data = null; // Allow GC
        System.out.println("PhantomReference queued after GC");

        // 4. ArrayList vs LinkedList Memory
        System.out.println("\n--- ArrayList vs LinkedList Memory ---");
        Runtime rt = Runtime.getRuntime();
        long before = rt.totalMemory() - rt.freeMemory();

        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 100000; i++) arrayList.add(i);
        long afterArray = rt.totalMemory() - rt.freeMemory();
        System.out.println("ArrayList 100K: " + (afterArray - before) + " bytes");

        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 100000; i++) linkedList.add(i);
        long afterLinked = rt.totalMemory() - rt.freeMemory();
        System.out.println("LinkedList 100K: " + (afterLinked - before) + " bytes");

        // 5. Initial Capacity Impact
        System.out.println("\n--- Initial Capacity Impact ---");
        before = rt.totalMemory() - rt.freeMemory();
        List<String> defaultCap = new ArrayList<>();
        for (int i = 0; i < 1000; i++) defaultCap.add("item" + i);
        afterArray = rt.totalMemory() - rt.freeMemory();
        System.out.println("Default capacity (grow): " + (afterArray - before) + " bytes");

        before = rt.totalMemory() - rt.freeMemory();
        List<String> preallocated = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) preallocated.add("item" + i);
        afterArray = rt.totalMemory() - rt.freeMemory();
        System.out.println("Pre-allocated 1000: " + (afterArray - before) + " bytes");

        // 6. Wrapper Class Memory
        System.out.println("\n--- Wrapper Class Memory ---");
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> boxed = new ArrayList<>();
        for (int i = 0; i < 10000; i++) boxed.add(i);
        afterArray = rt.totalMemory() - rt.freeMemory();
        System.out.println("Boxed Integer list: " + (afterArray - before) + " bytes");

        System.out.println("\nMemory overhead per Integer object: ~16 bytes");
        System.out.println("Memory for int[] of 10K: ~40KB vs Integer[]: ~200KB");
    }
}
