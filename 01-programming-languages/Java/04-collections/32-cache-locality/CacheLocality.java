import java.util.*;

/**
 * CPU Cache Locality in Collections
 * Cache lines, array vs LinkedList performance, false sharing.
 */
public class CacheLocality {

    // --- Cache line basics ---
    static void cacheLineBasics() {
        System.out.println("--- Cache Line Basics ---");

        System.out.println("CPU cache lines are typically 64 bytes:");
        System.out.println("  - L1 cache: ~64 bytes per line");
        System.out.println("  - L2 cache: ~64 bytes per line");
        System.out.println("  - L3 cache: ~64 bytes per line");

        System.out.println("\nMemory access patterns:");
        System.out.println("  Sequential access: CPU prefetches next cache line");
        System.out.println("  Random access: Cache misses, must fetch from RAM");

        System.out.println("\nRAM vs Cache latency:");
        System.out.println("  L1 cache: ~1 ns (1 cycle)");
        System.out.println("  L2 cache: ~3 ns (3-10 cycles)");
        System.out.println("  L3 cache: ~10 ns (10-30 cycles)");
        System.out.println("  Main RAM: ~100 ns (100-300 cycles)");

        System.out.println();
    }

    // --- Array cache performance ---
    static void arrayCachePerformance() {
        System.out.println("--- Array Cache Performance ---");

        int size = 1_000_000;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        // Sequential access - good cache locality
        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += array[i];
        }
        long sequentialTime = System.nanoTime() - start;
        System.out.println("Sequential array access: " + (sequentialTime / 1_000_000) + "ms");

        // Random access - poor cache locality
        Random random = new Random(42);
        int[] indices = new int[size];
        for (int i = 0; i < size; i++) {
            indices[i] = random.nextInt(size);
        }

        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < size; i++) {
            sum += array[indices[i]];
        }
        long randomTime = System.nanoTime() - start;
        System.out.println("Random array access: " + (randomTime / 1_000_000) + "ms");
        System.out.println("Ratio: " + String.format("%.1fx slower",
            (double) randomTime / sequentialTime));

        System.out.println();
    }

    // --- LinkedList cache performance ---
    static void linkedListCachePerformance() {
        System.out.println("--- LinkedList Cache Performance ---");

        int size = 1_000_000;
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            linkedList.add(i);
        }

        // Sequential access
        long start = System.nanoTime();
        long sum = 0;
        for (int val : linkedList) {
            sum += val;
        }
        long linkedListTime = System.nanoTime() - start;

        // ArrayList for comparison
        ArrayList<Integer> arrayList = new ArrayList<>(linkedList);
        start = System.nanoTime();
        sum = 0;
        for (int val : arrayList) {
            sum += val;
        }
        long arrayListTime = System.nanoTime() - start;

        System.out.println("Sequential iteration (" + size + " elements):");
        System.out.println("  ArrayList: " + (arrayListTime / 1_000_000) + "ms");
        System.out.println("  LinkedList: " + (linkedListTime / 1_000_000) + "ms");
        System.out.println("  Ratio: " + String.format("%.1fx slower",
            (double) linkedListTime / arrayListTime));

        System.out.println("\nWhy?");
        System.out.println("  ArrayList: contiguous memory, prefetch works");
        System.out.println("  LinkedList: scattered nodes, cache misses");

        System.out.println();
    }

    // --- False sharing ---
    static void falseSharingDemo() {
        System.out.println("--- False Sharing in Collections ---");

        System.out.println("False sharing occurs when:");
        System.out.println("  - Different threads modify different variables");
        System.out.println("  - But those variables share the same cache line");
        System.out.println("  - CPU invalidates entire cache line for both threads");

        // Example: array elements sharing cache line
        long[] counters = new long[2]; // false sharing!
        // Both elements likely on same 64-byte cache line

        // Fix: padding to separate cache lines
        @Contended
        long[] paddedCounters = new long[2]; // No false sharing

        System.out.println("\nExample: Two threads updating adjacent counters");
        System.out.println("  Without padding: Cache line bouncing");
        System.out.println("  With padding: Independent cache lines");

        // Java 8+ @sun.misc.Contended
        System.out.println("\n@sun.misc.Contended annotation:");
        System.out.println("  - Adds 128-byte padding around field");
        System.out.println("  - Ensures field on its own cache line");
        System.out.println("  - Requires -XX:-RestrictContended flag");

        System.out.println();
    }

    // --- Memory layout impact ---
    static void memoryLayoutImpact() {
        System.out.println("--- Memory Layout Impact on Performance ---");

        // Object layout in memory
        System.out.println("Typical object layout (64-bit JVM):");
        System.out.println("  - Object header: 12 bytes");
        System.out.println("  - Padding to 8-byte boundary");
        System.out.println("  - Fields in order (may be reordered)");

        System.out.println("\nArrayList internal array:");
        System.out.println("  - Object[] stores references (4 bytes each)");
        System.out.println("  - References point to heap objects");
        System.out.println("  - Objects scattered in memory");

        System.out.println("\nPrimitive arrays (int[], long[]):");
        System.out.println("  - Values stored directly in array");
        System.out.println("  - No pointer chasing");
        System.out.println("  - Excellent cache performance");

        // Performance example
        int size = 10_000_000;

        // Integer ArrayList
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }

        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += arrayList.get(i);
        }
        long arrayListTime = System.nanoTime() - start;

        // Primitive array
        int[] primitiveArray = new int[size];
        for (int i = 0; i < size; i++) {
            primitiveArray[i] = i;
        }

        start = System.nanoTime();
        sum = 0;
        for (int i = 0; i < size; i++) {
            sum += primitiveArray[i];
        }
        long primitiveTime = System.nanoTime() - start;

        System.out.println("\nComparison (" + size + " elements):");
        System.out.println("  ArrayList<Integer>: " + (arrayListTime / 1_000_000) + "ms");
        System.out.println("  int[]: " + (primitiveTime / 1_000_000) + "ms");
        System.out.println("  Array is " +
            String.format("%.1fx faster", (double) arrayListTime / primitiveTime));

        System.out.println("\n=== Complete ===");
    }

    @interface Contended {
    }

    public static void main(String[] args) {
        System.out.println("=== CPU Cache Locality in Collections ===\n");

        cacheLineBasics();
        arrayCachePerformance();
        linkedListCachePerformance();
        falseSharingDemo();
        memoryLayoutImpact();
    }
}
