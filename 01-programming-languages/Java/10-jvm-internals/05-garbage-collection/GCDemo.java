package jvm;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * GCDemo - GC algorithms and tuning flags
 *
 * Covers:
 * - GC types (Serial, Parallel, G1, ZGC)
 * - GC tuning flags
 * - WeakReference and SoftReference
 * - GC best practices
 */
public class GCDemo {

    public static void main(String[] args) {
        System.out.println("=== GC Algorithms ===");
        gcAlgorithms();

        System.out.println("\n=== GC Tuning Flags ===");
        gcTuningFlags();

        System.out.println("\n=== Reference Types ===");
        referenceTypes();

        System.out.println("\n=== GC Best Practices ===");
        gcBestPractices();
    }

    static void gcAlgorithms() {
        System.out.println("GC Algorithms in Java:");
        System.out.println("1. Serial GC (-XX:+UseSerialGC)");
        System.out.println("   - Single-threaded, stop-the-world");
        System.out.println("   - Good for single-core systems");
        System.out.println();
        System.out.println("2. Parallel GC (-XX:+UseParallelGC)");
        System.out.println("   - Multi-threaded, throughput-focused");
        System.out.println("   - Default in Java 8");
        System.out.println();
        System.out.println("3. G1 GC (-XX:+UseG1GC)");
        System.out.println("   - Region-based, balanced");
        System.out.println("   - Default in Java 9+");
        System.out.println();
        System.out.println("4. ZGC (-XX:+UseZGC)");
        System.out.println("   - Ultra-low latency (<10ms)");
        System.out.println("   - Java 15+ (production ready)");
        System.out.println();
        System.out.println("5. Shenandoah (-XX:+UseShenandoahGC)");
        System.out.println("   - Low-pause-time concurrent GC");
    }

    static void gcTuningFlags() {
        System.out.println("Common GC Tuning Flags:");
        System.out.println();
        System.out.println("Heap Size:");
        System.out.println("  -Xms512m    Initial heap size");
        System.out.println("  -Xmx2g      Maximum heap size");
        System.out.println("  -Xmn256m    Young generation size");
        System.out.println();
        System.out.println("G1 GC Specific:");
        System.out.println("  -XX:MaxGCPauseMillis=200  Target pause time");
        System.out.println("  -XX:G1HeapRegionSize=16m   Region size");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=45  Trigger GC");
        System.out.println();
        System.out.println("ZGC Specific:");
        System.out.println("  -XX:+UseZGC              Enable ZGC");
        System.out.println("  -XX:SoftMaxHeapSize=1g   Soft heap limit");
        System.out.println();
        System.out.println("Monitoring:");
        System.out.println("  -verbose:gc               Print GC logs");
        System.out.println("  -XX:+PrintGCDetails       Detailed GC info");
        System.out.println("  -Xlog:gc*                 Unified logging (Java 9+)");
    }

    static void referenceTypes() {
        System.out.println("Reference Types in Java:");
        System.out.println();

        // Strong Reference
        Object strong = new Object();
        System.out.println("Strong Reference: Always retained until GC");

        // WeakReference - collected on next GC
        WeakReference<Object> weak = new WeakReference<>(new Object());
        System.out.println("WeakReference: Collected when no strong refs");
        System.out.println("  Before GC: " + (weak.get() != null));

        // Force GC
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  After GC: " + (weak.get() != null));

        // SoftReference - collected when memory is low
        SoftReference<byte[]> soft = new SoftReference<>(new byte[1024 * 1024]);
        System.out.println("SoftReference: Collected under memory pressure");
        System.out.println("  Value present: " + (soft.get() != null));

        // Demonstrate GC
        System.out.println("\nTriggering GC...");
        List<Object> leak = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            leak.add(new byte[1024 * 1024]);
        }
        leak.clear();
        System.gc();
    }

    static void gcBestPractices() {
        System.out.println("GC Best Practices:");
        System.out.println();
        System.out.println("1. Set appropriate heap sizes");
        System.out.println("   -Xms = -Xmx (avoid resizing)");
        System.out.println();
        System.out.println("2. Choose right GC for your use case");
        System.out.println("   - Throughput: Parallel GC");
        System.out.println("   - Latency: G1, ZGC, Shenandoah");
        System.out.println();
        System.out.println("3. Monitor GC logs");
        System.out.println("   - Use -Xlog:gc* for Java 9+");
        System.out.println("   - Look for long pauses");
        System.out.println();
        System.out.println("4. Avoid finalizers");
        System.out.println("   - Use try-with-resources instead");
        System.out.println();
        System.out.println("5. Minimize object creation in hot loops");
        System.out.println("   - Reuse objects when possible");
        System.out.println();
        System.out.println("6. Use appropriate data structures");
        System.out.println("   - ArrayList vs LinkedList");
        System.out.println("   - HashMap vs TreeMap");
    }
}