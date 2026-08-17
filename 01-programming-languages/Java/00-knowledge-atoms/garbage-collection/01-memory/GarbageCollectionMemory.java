package academy.javaengineering.knowledgeatoms.garbagecollection;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class GarbageCollectionMemory {

    public static void main(String[] args) {
        System.out.println("=== Garbage Collection Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Heap memory overview
        System.out.println("--- Heap Memory Overview ---");
        long total = rt.totalMemory();
        long free = rt.freeMemory();
        long used = total - free;
        System.out.println("Total heap:  " + total / 1024 + " KB");
        System.out.println("Free heap:   " + free / 1024 + " KB");
        System.out.println("Used heap:   " + used / 1024 + " KB");

        // 2. Object allocation and GC
        System.out.println("\n--- Allocation and GC ---");
        List<byte[]> list = new ArrayList<>();
        int iterations = 1000;
        long beforeUsed = rt.totalMemory() - rt.freeMemory();

        for (int i = 0; i < iterations; i++) {
            list.add(new byte[1024]); // 1KB each
        }

        long afterUsed = rt.totalMemory() - rt.freeMemory();
        System.out.println("Allocated " + iterations + " x 1KB objects");
        System.out.println("Memory before: " + beforeUsed / 1024 + " KB");
        System.out.println("Memory after:  " + afterUsed / 1024 + " KB");
        System.out.println("Increase:      " + (afterUsed - beforeUsed) / 1024 + " KB");

        // 3. GC reclaiming memory
        System.out.println("\n--- GC Memory Reclamation ---");
        list.clear();
        rt.gc();
        long afterGC = rt.totalMemory() - rt.freeMemory();
        System.out.println("After clear() + GC:");
        System.out.println("Memory used: " + afterGC / 1024 + " KB");
        System.out.println("Memory freed: " + (afterUsed - afterGC) / 1024 + " KB");

        // 4. Soft reference memory behavior
        System.out.println("\n--- Soft Reference Memory ---");
        System.out.println("Soft references are held until memory is low");
        System.out.println("Useful for caches that should survive GC but not cause OOM");
        List<SoftReference<byte[]>> softList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            softList.add(new SoftReference<>(new byte[1024]));
        }
        long alive = softList.stream().filter(sr -> sr.get() != null).count();
        System.out.println("Soft references alive before GC: " + alive);

        // 5. Memory footprint per object type
        System.out.println("\n--- Object Memory Footprints ---");
        System.out.println("Object header:      12 bytes (mark + klass pointer)");
        System.out.println("int field:           4 bytes");
        System.out.println("long field:          8 bytes");
        System.out.println("Reference field:     8 bytes (compressed oops)");
        System.out.println("Alignment: padded to 8-byte boundary");
        System.out.println("Empty object:       16 bytes minimum");
    }
}
