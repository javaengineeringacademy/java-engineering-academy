package academy.javaengineering.knowledgeatoms.autoboxing;

import java.util.ArrayList;
import java.util.List;

public class AutoboxingMemory {

    public static void main(String[] args) {
        System.out.println("=== Autoboxing Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object overhead for wrapper types
        System.out.println("--- Wrapper Object Overhead ---");
        System.out.println("int (primitive):        4 bytes");
        System.out.println("Integer (wrapper):     16 bytes (object header 12 + int 4 + alignment)");
        System.out.println("Overhead per value:    12 bytes (400% more)");
        System.out.println("long (primitive):       8 bytes");
        System.out.println("Long (wrapper):        16 bytes (object header 12 + long 8 + padding)");
        System.out.println("Overhead per value:     8 bytes (100% more)");

        // 2. Memory impact in collections
        System.out.println("\n--- Collection Memory Impact ---");
        int count = 100_000;

        // Measure Integer list memory
        rt.gc();
        long beforeInt = getUsedMemory();
        List<Integer> intList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            intList.add(i);
        }
        long afterInt = getUsedMemory();

        // Measure Long list memory
        rt.gc();
        long beforeLong = getUsedMemory();
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            longList.add((long) i);
        }
        long afterLong = getUsedMemory();

        System.out.println("List<Integer> (" + count + " elements): ~" + (afterInt - beforeInt) / 1024 + " KB");
        System.out.println("List<Long> (" + count + " elements):    ~" + (afterLong - beforeLong) / 1024 + " KB");
        System.out.println("Equivalent int[] would be:        ~" + (count * 4L) / 1024 + " KB");
        System.out.println("Equivalent long[] would be:       ~" + (count * 8L) / 1024 + " KB");

        // 3. Cache memory retention
        System.out.println("\n--- Cache Memory Retention ---");
        System.out.println("Cached Integers (-128 to 127) are permanently in memory");
        System.out.println("256 cached objects * ~16 bytes = ~4 KB baseline per Integer cache");
        System.out.println("Cache is loaded on first Integer autoboxing and never collected");

        // 4. Memory per element breakdown
        System.out.println("\n--- Per-Element Breakdown (List<Integer>) ---");
        System.out.println("ArrayList object header: 16 bytes");
        System.out.println("Internal Object[] ref:    8 bytes");
        System.out.println("Per Integer element:");
        System.out.println("  - Reference in array:   8 bytes (compressed oops)");
        System.out.println("  - Integer object:      16 bytes");
        System.out.println("  - Total per element:   24 bytes");
        System.out.println("For 100K elements: ~2.3 MB vs 380 KB for int[]");

        // 5. Autoboxing creates temporary garbage
        System.out.println("\n--- GC Pressure from Autoboxing ---");
        System.out.println("Loop with autoboxing: creates N Integer objects per iteration");
        System.out.println("These become garbage immediately, increasing GC frequency");
        System.out.println("Primitive loops: zero object creation, zero GC pressure");
    }

    private static long getUsedMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }
}
