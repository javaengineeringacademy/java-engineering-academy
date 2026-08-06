package academy.javaengineering.jvm.memory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Memory Deep Dive
 * Covers Eden, Survivor, Old Generation, Metaspace, Code Cache, TLAB,
 * object memory layout, memory alignment, and padding.
 */
public class MemoryDeepDive {

    private static final AtomicInteger objectCount = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("=== JVM Memory Deep Dive ===\n");

        // 1. Eden Space
        demonstrateEdenSpace();

        // 2. Survivor Spaces
        demonstrateSurvivorSpaces();

        // 3. Old Generation
        demonstrateOldGeneration();

        // 4. Metaspace
        demonstrateMetaspace();

        // 5. Code Cache
        demonstrateCodeCache();

        // 6. TLAB
        demonstrateTLAB();

        // 7. Object Memory Layout
        demonstrateObjectLayout();

        // 8. Memory Alignment & Padding
        demonstrateAlignmentAndPadding();
    }

    /**
     * Eden Space: Where new objects are allocated
     * - Default: ~80% of Young Generation
     * - Thread-Local Allocation Buffers (TLABs) for fast allocation
     * - When Eden fills up → Minor GC
     */
    private static void demonstrateEdenSpace() {
        System.out.println("--- 1. Eden Space (New Object Allocation) ---");

        System.out.println("Eden Space characteristics:");
        System.out.println("  - Default: ~80% of Young Generation");
        System.out.println("  - Thread-Local Allocation Buffers (TLABs)");
        System.out.println("  - When Eden fills → Minor GC");
        System.out.println("  - Surviving objects move to Survivor spaces\n");

        System.out.println("Eden Space configuration:");
        System.out.println("  -XX:NewSize=256m          (initial young gen size)");
        System.out.println("  -XX:MaxNewSize=1g         (max young gen size)");
        System.out.println("  -XX:SurvivorRatio=8       (Eden:Survivor = 8:1:1)\n");

        // Allocate objects in Eden
        System.out.println("Allocating objects in Eden...");
        long startTime = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            Object obj = new Object(); // Allocated in Eden (or TLAB)
            objectCount.incrementAndGet();
        }
        long edenTime = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("  Allocated 100K objects in " + edenTime + " ms\n");

        printMemoryStatus("After Eden allocation");
    }

    /**
     * Survivor Spaces (S0, S1): Hold objects that survived Minor GC
     * - Two equal-sized spaces (S0 and S1)
     * - One is always empty (to-space)
     * - Objects survive N GC cycles → promoted to Old Generation
     */
    private static void demonstrateSurvivorSpaces() {
        System.out.println("--- 2. Survivor Spaces (S0, S1) ---");

        System.out.println("Survivor Space characteristics:");
        System.out.println("  - Two equal-sized spaces: S0 (From) and S1 (To)");
        System.out.println("  - One space is always empty (to-space)");
        System.out.println("  - Objects surviving Minor GC move between spaces");
        System.out.println("  - After N cycles → promoted to Old Generation\n");

        System.out.println("Survivor Space configuration:");
        System.out.println("  -XX:SurvivorRatio=8       (Eden:S0:S1 = 8:1:1)");
        System.out.println("  -XX:MaxTenuringThreshold=15 (max cycles before promotion)");
        System.out.println("  -XX:TargetSurvivorRatio=50  (target survivor occupancy)\n");

        System.out.println("Object lifecycle in Survivor spaces:");
        System.out.println("  1. Object allocated in Eden");
        System.out.println("  2. Minor GC: Eden cleared, survivors → S0");
        System.out.println("  3. Next Minor GC: S0 survivors → S1, Eden survivors → S0");
        System.out.println("  4. After MaxTenuringThreshold cycles → Old Generation\n");

        // Simulate survivor promotion
        List<Object> survivors = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            survivors.add(new byte[1024]); // 1KB objects
        }
        System.out.println("Created " + survivors.size() + " objects for survivor simulation");
        survivors.clear();
        System.out.println("Cleared references (objects eligible for GC)\n");
    }

    /**
     * Old Generation: Long-lived objects
     * - Objects that survive multiple GC cycles
     * - Major GC / Full GC collects this area
     * - Larger space, less frequent collection
     */
    private static void demonstrateOldGeneration() {
        System.out.println("--- 3. Old Generation (Tenuring) ---");

        System.out.println("Old Generation characteristics:");
        System.out.println("  - Stores long-lived objects");
        System.out.println("  - Objects promoted from Young Generation");
        System.out.println("  - Major GC / Full GC collects this area");
        System.out.println("  - Larger space, less frequent collection\n");

        System.out.println("Old Generation configuration:");
        System.out.println("  -Xms2g                   (initial heap size)");
        System.out.println("  -Xmx4g                   (max heap size)");
        System.out.println("  -XX:NewRatio=2            (Old:Young = 2:1)");
        System.out.println("  -XX:MaxTenuringThreshold=15\n");

        System.out.println("Promotion triggers:");
        System.out.println("  1. Object survives MaxTenuringThreshold GC cycles");
        System.out.println("  2. Survivor space is full");
        System.out.println("  3. Object is too large (exceeds PretenureSizeThreshold)");
        System.out.println("  4. Dynamic estimation: objects older than avg survival time\n");

        // Create long-lived objects (will be promoted to Old Gen)
        List<Object> longLived = new ArrayList<>();
        for (int i = 0; i < 50_000; i++) {
            longLived.add(new byte[4096]); // 4KB objects
        }
        System.out.println("Created " + longLived.size() + " long-lived objects (promoted to Old Gen)");
        System.out.println("Total: " + (longLived.size() * 4096 / 1024) + " KB in Old Generation\n");
    }

    /**
     * Metaspace: Class metadata storage
     * - Replaces PermGen (Java 8+)
     * - Stores class metadata, method metadata, constant pool
     * - Grows automatically (can be bounded)
     */
    private static void demonstrateMetaspace() {
        System.out.println("--- 4. Metaspace (Class Metadata) ---");

        System.out.println("Metaspace characteristics:");
        System.out.println("  - Replaces PermGen (Java 8+)");
        System.out.println("  - Stores: class metadata, method metadata, constant pool");
        System.out.println("  - Uses native memory (off-heap)");
        System.out.println("  - Grows automatically (can be bounded)");
        System.out.println("  - Class unloading releases space\n");

        System.out.println("Metaspace configuration:");
        System.out.println("  -XX:MetaspaceSize=256m     (initial size)");
        System.out.println("  -XX:MaxMetaspaceSize=512m  (max size)");
        System.out.println("  -XX:CompressedClassSpaceSize=1g (compressed class pointers)\n");

        System.out.println("What goes in Metaspace:");
        System.out.println("  - Class definitions (InstanceKlass)");
        System.out.println("  - Method metadata (bytecodes, exception tables)");
        System.out.println("  - Constant pool entries");
        System.out.println("  - Field metadata");
        System.out.println("  - Annotation metadata\n");

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();
        System.out.println("Current Metaspace usage:");
        System.out.println("  Used: " + formatMB(nonHeap.getUsed()));
        System.out.println("  Committed: " + formatMB(nonHeap.getCommitted()));
        System.out.println("  Max: " + formatMB(nonHeap.getMax()) + "\n");
    }

    /**
     * Code Cache: JIT-compiled native code storage
     * - Stores compiled native code from JIT compiler
     * - Divided into: Non-method, Profiled, Non-profiled
     * - When full → JIT stops compiling
     */
    private static void demonstrateCodeCache() {
        System.out.println("--- 5. Code Cache (Compiled Code) ---");

        System.out.println("Code Cache characteristics:");
        System.out.println("  - Stores JIT-compiled native code");
        System.out.println("  - Divided into segments:");
        System.out.println("    1. Non-method (8MB): VM internal code");
        System.out.println("    2. Profiled (22MB): C1 compiled with profiling");
        System.out.println("    3. Non-profiled (125MB): C2 compiled code");
        System.out.println("  - When full: JIT stops compiling → performance degradation\n");

        System.out.println("Code Cache configuration:");
        System.out.println("  -XX:InitialCodeCacheSize=256k   (initial size)");
        System.out.println("  -XX:ReservedCodeCacheSize=256m  (max size)");
        System.out.println("  -XX:CodeCacheExpansionSize=64   (expansion size)\n");

        // Trigger JIT compilation to fill code cache
        System.out.println("Triggering JIT compilation...");
        for (int i = 0; i < 100_000; i++) {
            compileTarget(i);
        }
        System.out.println("JIT compilation triggered for demonstration\n");
    }

    private static long compileTarget(int n) {
        return (long) n * n + n * 3 + 7;
    }

    /**
     * TLAB (Thread-Local Allocation Buffer)
     * - Per-thread allocation buffer in Eden
     * - Lock-free allocation for single thread
     * - When TLAB fills → allocate new TLAB or slow path
     */
    private static void demonstrateTLAB() {
        System.out.println("--- 6. TLAB (Thread-Local Allocation Buffer) ---");

        System.out.println("TLAB characteristics:");
        System.out.println("  - Per-thread allocation buffer in Eden");
        System.out.println("  - Lock-free allocation (bump-the-pointer)");
        System.out.println("  - Eliminates contention for object allocation");
        System.out.println("  - When TLAB fills → new TLAB or slow path\n");

        System.out.println("TLAB allocation process:");
        System.out.println("  1. Thread checks if object fits in current TLAB");
        System.out.println("  2. If yes: bump pointer, return object (fast path)");
        System.out.println("  3. If no: allocate new TLAB or use slow path");
        System.out.println("  4. Slow path: Eden allocation with CAS\n");

        System.out.println("TLAB configuration:");
        System.out.println("  -XX:+UseTLAB                (enable, default=true)");
        System.out.println("  -XX:TLABSize=512k           (initial TLAB size)");
        System.out.println("  -XX:MinTLABSize=2k          (minimum TLAB size)");
        System.out.println("  -XX:TLABRefillWasteFraction=64 (refill waste threshold)\n");

        // Demonstrate TLAB allocation
        System.out.println("Multi-threaded TLAB allocation:");
        Thread[] threads = new Thread[4];
        long[] allocTimes = new long[4];

        for (int t = 0; t < threads.length; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                long start = System.nanoTime();
                for (int i = 0; i < 1_000_000; i++) {
                    new Object(); // TLAB allocation
                }
                allocTimes[threadId] = (System.nanoTime() - start) / 1_000_000;
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long totalAlloc = 0;
        for (int t = 0; t < threads.length; t++) {
            System.out.println("  Thread " + t + ": " + allocTimes[t] + " ms");
            totalAlloc += allocTimes[t];
        }
        System.out.println("  Total allocation time: " + totalAlloc + " ms\n");
    }

    /**
     * Object Memory Layout
     * - Object Header (Mark Word + Klass Pointer)
     * - Instance Fields
     * - Padding (alignment to 8 bytes)
     */
    private static void demonstrateObjectLayout() {
        System.out.println("--- 7. Object Memory Layout ---");

        System.out.println("Object layout in memory (64-bit JVM):");
        System.out.println("  ┌─────────────────────────────────────┐");
        System.out.println("  │ Object Header (16 bytes)             │");
        System.out.println("  │  - Mark Word (8 bytes):              │");
        System.out.println("  │    - Hash code (31 bits)             │");
        System.out.println("  │    - GC age (4 bits)                 │");
        System.out.println("  │    - Lock state (2 bits)             │");
        System.out.println("  │    - biased_lock (1 bit)             │");
        System.out.println("  │  - Klass Pointer (4 or 8 bytes):     │");
        System.out.println("  │    - Points to class metadata        │");
        System.out.println("  ├─────────────────────────────────────┤");
        System.out.println("  │ Instance Fields (variable size)      │");
        System.out.println("  │  - Ordered by size (largest first)   │");
        System.out.println("  │  - References: 4 or 8 bytes each     │");
        System.out.println("  ├─────────────────────────────────────┤");
        System.out.println("  │ Padding (0-7 bytes)                  │");
        System.out.println("  │  - Align to 8 bytes                  │");
        System.out.println("  └─────────────────────────────────────┘\n");

        // Show actual object sizes
        System.out.println("Actual object sizes (approximate):");
        System.out.println("  Object: " + estimateObjectSize(new Object()) + " bytes");
        System.out.println("  Integer: " + estimateObjectSize(Integer.valueOf(42)) + " bytes");
        System.out.println("  String: " + estimateObjectSize("Hello") + " bytes");
        System.out.println("  byte[0]: " + estimateObjectSize(new byte[0]) + " bytes");
        System.out.println("  byte[1]: " + estimateObjectSize(new byte[1]) + " bytes");
        System.out.println("  byte[8]: " + estimateObjectSize(new byte[8]) + " bytes");
        System.out.println("  byte[16]: " + estimateObjectSize(new byte[16]) + " bytes\n");
    }

    /**
     * Memory Alignment & Padding
     * - Objects aligned to 8 bytes (64-bit JVM)
     * - Padding added to reach alignment boundary
     * - Compressed oops affect alignment
     */
    private static void demonstrateAlignmentAndPadding() {
        System.out.println("--- 8. Memory Alignment & Padding ---");

        System.out.println("Alignment rules:");
        System.out.println("  - 64-bit JVM: Objects aligned to 8 bytes");
        System.out.println("  - With compressed oops: Aligned to 8 bytes");
        System.out.println("  - Without compressed oops: Aligned to 8 bytes\n");

        System.out.println("Padding examples:");
        System.out.println("  - 1 field (1 byte): 16 header + 1 field + 7 padding = 24 bytes");
        System.out.println("  - 2 fields (2 bytes): 16 header + 2 fields + 6 padding = 24 bytes");
        System.out.println("  - 8 fields (8 bytes): 16 header + 8 fields + 0 padding = 24 bytes");
        System.out.println("  - 9 fields (9 bytes): 16 header + 9 fields + 7 padding = 32 bytes\n");

        System.out.println("Compressed oops (Object Pointers):");
        System.out.println("  -XX:+UseCompressedOops          (enable, default for heaps <32GB)");
        System.out.println("  -XX:+UseCompressedClassPointers (compress class pointers)");
        System.out.println("  - With compressed oops: references are 4 bytes instead of 8");
        System.out.println("  - Requires heap < 32GB\n");

        // Demonstrate alignment with different object sizes
        System.out.println("Alignment demonstration:");
        System.out.println("  sizeof(Object) = " + estimateObjectSize(new Object()) + " bytes");
        System.out.println("  sizeof(int) = " + estimateObjectSize(0) + " bytes (boxed)");
        System.out.println("  sizeof(long) = " + estimateObjectSize(0L) + " bytes (boxed)");
        System.out.println("  sizeof(float) = " + estimateObjectSize(0.0f) + " bytes (boxed)");
        System.out.println("  sizeof(double) = " + estimateObjectSize(0.0) + " bytes (boxed)\n");

        printMemoryStatus("Final memory status");
    }

    private static long estimateObjectSize(Object obj) {
        // Estimate using instrumentation or known sizes
        if (obj instanceof byte[]) {
            return 16 + 4 + ((byte[]) obj).length + padding(((byte[]) obj).length);
        }
        if (obj instanceof Integer) return 16 + 4; // header + int
        if (obj instanceof Long) return 16 + 8;
        if (obj instanceof Double) return 16 + 8;
        if (obj instanceof Float) return 16 + 4;
        if (obj instanceof Boolean) return 16 + 1;
        if (obj instanceof String) return 16 + 4 + 16; // header + ref + String fields
        return 16; // Just header
    }

    private static int padding(int dataSize) {
        int total = 16 + dataSize; // header + data
        return (8 - (total % 8)) % 8;
    }

    private static void printMemoryStatus(String label) {
        System.out.println("--- " + label + " ---");
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();

        System.out.println("Heap:");
        System.out.println("  Used:      " + formatMB(heap.getUsed()));
        System.out.println("  Committed: " + formatMB(heap.getCommitted()));
        System.out.println("  Max:       " + formatMB(heap.getMax()));
        System.out.println("Non-Heap:");
        System.out.println("  Used:      " + formatMB(nonHeap.getUsed()));
        System.out.println("  Committed: " + formatMB(nonHeap.getCommitted()));
        System.out.println("Runtime:");
        System.out.println("  Total:     " + formatMB(runtime.totalMemory()));
        System.out.println("  Free:      " + formatMB(runtime.freeMemory()));
        System.out.println("  Max:       " + formatMB(runtime.maxMemory()));
        System.out.println();
    }

    private static String formatMB(long bytes) {
        return String.format("%8.2f MB", bytes / (1024.0 * 1024.0));
    }
}
