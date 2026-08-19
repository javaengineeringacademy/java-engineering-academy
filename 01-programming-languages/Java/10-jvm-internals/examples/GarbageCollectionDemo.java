package academy.javaengineering.jvm.examples;

import java.lang.ref.*;
import java.util.*;

/**
 * Garbage Collection Demo
 * Demonstrates GC concepts, generational model, GC roots,
 * reference types, and collector comparison.
 */
public class GarbageCollectionDemo {

    // Strong reference - prevents GC
    private static Object strongRef;

    // Weak reference - GC can reclaim object
    private static WeakReference<Object> weakRef;

    // Soft reference - GC reclaims before OutOfMemoryError
    private static SoftReference<Object> softRef;

    // Phantom reference - cleanup after finalization
    private static PhantomReference<Object> phantomRef;

    // Reference queue for tracking GC events
    private static ReferenceQueue<Object> refQueue;

    /**
     * DEMO 1: Generational Memory Model
     */
    public static void demonstrateGenerationalModel() {
        System.out.println("=== Generational Memory Model ===");
        System.out.println("Heap divided into generations based on object lifetime:");
        System.out.println();
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│              YOUNG GENERATION           │");
        System.out.println("│  ┌──────────┬───────────┬──────────┐   │");
        System.out.println("│  │  Eden    │ Survivor  │ Survivor │   │");
        System.out.println("│  │ (80%)    │   From    │   To     │   │");
        System.out.println("│  │          │  (10%)    │  (10%)   │   │");
        System.out.println("│  └──────────┴───────────┴──────────┘   │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│              OLD GENERATION             │");
        System.out.println("│           (Long-lived objects)          │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│             METASPACE                   │");
        System.out.println("│        (Class metadata, off-heap)       │");
        System.out.println("└─────────────────────────────────────────┘");

        System.out.println("\nDefault ratios (can be tuned):");
        System.out.println("  Young:Old = 1:2 (NewRatio=2)");
        System.out.println("  Eden:Survivor = 8:1:1 (SurvivorRatio=8)");
        System.out.println("  Use -XX:NewRatio=N and -XX:SurvivorRatio=N to adjust");
    }

    /**
     * DEMO 2: Object Lifecycle
     */
    public static void demonstrateObjectLifecycle() {
        System.out.println("\n=== Object Lifecycle ===");
        System.out.println("1. Allocation: new Object() -> Eden space");
        System.out.println("2. First GC: Survives -> moved to Survivor From");
        System.out.println("3. Subsequent GC: Copies between Survivors");
        System.out.println("4. Age threshold: Survives N GCs -> promoted to Old");
        System.out.println("5. Major GC: Old space collected");
        System.out.println("6. Finalization: finalize() called (deprecated)");
        System.out.println("7. Reclamation: memory returned to free list");

        System.out.println("\nDefault MaxTenuringThreshold: 15");
        System.out.println("Use -XX:MaxTenuringThreshold=N to adjust");
    }

    /**
     * DEMO 3: GC Roots
     */
    public static void demonstrateGcRoots() {
        System.out.println("\n=== GC Roots ===");
        System.out.println("Objects reachable from GC roots are NOT collected:");
        System.out.println();
        System.out.println("Types of GC Roots:");
        System.out.println("  1. Local variables in stack frames");
        System.out.println("  2. Active threads");
        System.out.println("  3. Static fields of loaded classes");
        System.out.println("  4. JNI references (native method calls)");
        System.out.println("  5. Monitors (locked objects)");
        System.out.println("  6. Objects used for synchronization");
        System.out.println("  7. System class loader loaded classes");
        System.out.println("  8. JVM internal references");

        // Demonstrate some roots
        Object localVar = new Object(); // Local variable root
        strongRef = new Object();       // Static field root
        Thread current = Thread.currentThread(); // Active thread root
        System.out.println("\nExamples in this code:");
        System.out.println("  localVar (local variable): " + localVar);
        System.out.println("  strongRef (static field): " + strongRef);
        System.out.println("  current thread: " + current.getName());
    }

    /**
     * DEMO 4: Reference Types
     */
    public static void demonstrateReferenceTypes() {
        System.out.println("\n=== Reference Types ===");

        refQueue = new ReferenceQueue<>();

        // Strong Reference
        Object strong = new Object();
        strongRef = strong;
        System.out.println("Strong Reference: " + strong);
        System.out.println("  - Never collected while reachable");
        System.out.println("  - Strongly reachable from GC roots");

        // Soft Reference
        Object softObj = new Object();
        softRef = new SoftReference<>(softObj, refQueue);
        System.out.println("\nSoft Reference: " + softRef.get());
        System.out.println("  - Collected only when memory is low");
        System.out.println("  - Good for caches");
        System.out.println("  - Cleared before OutOfMemoryError");

        // Weak Reference
        Object weakObj = new Object();
        weakRef = new WeakReference<>(weakObj, refQueue);
        System.out.println("\nWeak Reference: " + weakRef.get());
        System.out.println("  - Collected at next GC cycle");
        System.out.println("  - Used in WeakHashMap, ThreadLocal");
        System.out.println("  - Weakly reachable");

        // Phantom Reference
        Object phantomObj = new Object();
        phantomRef = new PhantomReference<>(phantomObj, refQueue);
        System.out.println("\nPhantom Reference: " + phantomRef.get());
        System.out.println("  - get() always returns null");
        System.out.println("  - Enqueued after object is finalized");
        System.out.println("  - Used for cleanup (replacing finalize())");

        // Demonstrate reference clearing
        strong = null; // Allow GC of soft reference
        System.out.println("\nAfter setting strong=null:");
        System.out.println("  Soft reference value: " + softRef.get());
        System.out.println("  Weak reference value: " + weakRef.get());
    }

    /**
     * DEMO 5: Finalization (Deprecated)
     */
    public static void demonstrateFinalization() {
        System.out.println("\n=== Finalization (Deprecated in Java 9+) ===");
        System.out.println("Old pattern (DO NOT USE):");
        System.out.println("  @Override");
        System.out.println("  protected void finalize() throws Throwable {");
        System.out.println("    // cleanup resources");
        System.out.println("  }");
        System.out.println("\nProblems with finalization:");
        System.out.println("  1. Unpredictable timing");
        System.out.println("  2. Performance overhead (finalizer thread)");
        System.out.println("  3. Resurrection (finalizer can re-reach 'this')");
        System.out.println("  4. Resource leaks if finalizer throws exception");
        System.out.println("\nModern alternative: Cleaner / try-with-resources");
    }

    /**
     * DEMO 6: GC Algorithm Comparison
     */
    public static void demonstrateGcComparison() {
        System.out.println("\n=== GC Algorithm Comparison ===");
        System.out.println("┌──────────────┬────────────┬──────────┬──────────┬───────────┐");
        System.out.println("│ Algorithm    │ Pause Time │ Throughput│ Memory   │ Best For  │");
        System.out.println("├──────────────┼────────────┼──────────┼──────────┼───────────┤");
        System.out.println("│ Serial       │ Longest    │ Highest  │ Lowest   │ Single CPU│");
        System.out.println("│ Parallel     │ Short      │ High     │ Low      │ Batch     │");
        System.out.println("│ CMS          │ Short      │ Medium   │ Medium   │ Latency  │");
        System.out.println("│ G1           │ Tunable    │ Medium   │ Medium   │ General  │");
        System.out.println("│ ZGC          │ <1ms       │ Medium   │ Higher   │ Low pause│");
        System.out.println("│ Shenandoah   │ <10ms      │ Medium   │ Higher   │ Low pause│");
        System.out.println("└──────────────┴────────────┴──────────┴──────────┴───────────┘");

        System.out.println("\nSelection guidelines:");
        System.out.println("  -XX:+UseSerialGC       -> Small apps, single core");
        System.out.println("  -XX:+UseParallelGC     -> Throughput-oriented, batch");
        System.out.println("  -XX:+UseG1GC           -> General purpose (default since JDK 9)");
        System.out.println("  -XX:+UseZGC            -> Ultra-low latency (<10ms pauses)");
        System.out.println("  -XX:+UseShenandoahGC   -> Low pause, open-source (Red Hat)");
    }

    /**
     * DEMO 7: Memory Allocation Strategies
     */
    public static void demonstrateAllocation() {
        System.out.println("\n=== Memory Allocation Strategies ===");
        System.out.println("1. TLAB (Thread-Local Allocation Buffers):");
        System.out.println("   - Each thread gets a private Eden region");
        System.out.println("   - Lock-free allocation");
        System.out.println("   - Enabled by default (-XX:+UseTLAB)");
        System.out.println("\n2. Bump-the-pointer:");
        System.out.println("   - Simple pointer increment for allocation");
        System.out.println("   - Fast for sequential allocations");
        System.out.println("\n3. Risky allocation (large objects):");
        System.out.println("   - Objects > RegionSize/2 go directly to Old");
        System.out.println("   - G1: -XX:G1HeapRegionSize=N");
    }

    /**
     * DEMO 8: Forcing GC (for demo purposes only)
     */
    public static void demonstrateForcingGC() {
        System.out.println("\n=== Forcing GC ===");
        System.out.println("Note: System.gc() is only a HINT to the JVM");
        System.out.println("The JVM may ignore it entirely.");
        System.out.println("Use -XX:+DisableExplicitGC to prevent System.gc() calls");

        long before = Runtime.getRuntime().freeMemory();
        System.gc();
        try {
            Thread.sleep(100); // Give GC time to run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long after = Runtime.getRuntime().freeMemory();
        System.out.println("Free memory before GC: " + (before / 1024) + " KB");
        System.out.println("Free memory after GC: " + (after / 1024) + " KB");
        System.out.println("Difference: " + ((after - before) / 1024) + " KB");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   GARBAGE COLLECTION CONCEPTS DEMO  ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateGenerationalModel();
        demonstrateObjectLifecycle();
        demonstrateGcRoots();
        demonstrateReferenceTypes();
        demonstrateFinalization();
        demonstrateGcComparison();
        demonstrateAllocation();
        demonstrateForcingGC();

        System.out.println("\n=== Runtime Memory Stats ===");
        Runtime rt = Runtime.getRuntime();
        System.out.println("Max Heap: " + (rt.maxMemory() / 1024 / 1024) + " MB");
        System.out.println("Total Heap: " + (rt.totalMemory() / 1024 / 1024) + " MB");
        System.out.println("Free Heap: " + (rt.freeMemory() / 1024 / 1024) + " MB");
        System.out.println("Used Heap: " + ((rt.totalMemory() - rt.freeMemory()) / 1024 / 1024) + " MB");
    }
}
