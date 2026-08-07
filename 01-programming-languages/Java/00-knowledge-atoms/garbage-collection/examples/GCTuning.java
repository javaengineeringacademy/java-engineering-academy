import java.lang.management.*;
import java.util.*;

/**
 * GC Tuning Demo
 * Demonstrates GC tuning options and monitoring
 */
public class GCTuning {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== GC Tuning Demo ===\n");

        // 1. Display current GC settings
        System.out.println("--- Current GC Configuration ---");
        displayGCInfo();

        // 2. Show memory pools
        System.out.println("\n--- Memory Pools ---");
        displayMemoryPools();

        // 3. Show GC beans
        System.out.println("\n--- Garbage Collectors ---");
        displayGCStats();

        // 4. Simulate workload and monitor GC
        System.out.println("\n--- Simulating Workload ---");
        simulateWorkload();

        // 5. Display tuning recommendations
        System.out.println("\n--- GC Tuning Flags Reference ---");
        displayTuningFlags();

        System.out.println("\n=== End of GC Tuning Demo ===");
    }

    private static void displayGCInfo() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        List<String> vmArgs = runtime.getInputArguments();
        System.out.println("VM Arguments:");
        for (String arg : vmArgs) {
            if (arg.contains("gc") || arg.contains("Heap") || arg.contains("Xms") ||
                arg.contains("Xmx") || arg.contains("Use")) {
                System.out.println("  " + arg);
            }
        }

        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        System.out.println("\nHeap Memory:");
        MemoryUsage heap = memory.getHeapMemoryUsage();
        System.out.println("  Init:     " + formatBytes(heap.getInit()));
        System.out.println("  Used:     " + formatBytes(heap.getUsed()));
        System.out.println("  Committed:" + formatBytes(heap.getCommitted()));
        System.out.println("  Max:      " + formatBytes(heap.getMax()));

        System.out.println("\nNon-Heap Memory:");
        MemoryUsage nonHeap = memory.getNonHeapMemoryUsage();
        System.out.println("  Init:     " + formatBytes(nonHeap.getInit()));
        System.out.println("  Used:     " + formatBytes(nonHeap.getUsed()));
        System.out.println("  Committed:" + formatBytes(nonHeap.getCommitted()));
        System.out.println("  Max:      " + formatBytes(nonHeap.getMax()));
    }

    private static void displayMemoryPools() {
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            MemoryUsage usage = pool.getUsage();
            System.out.printf("  %-30s Used: %s, Max: %s%n",
                pool.getName(),
                formatBytes(usage.getUsed()),
                formatBytes(usage.getMax()));
        }
    }

    private static void displayGCStats() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcBeans) {
            System.out.printf("  %-20s Collections: %d, Time: %dms%n",
                gc.getName(),
                gc.getCollectionCount(),
                gc.getCollectionTime());
            System.out.printf("    Pool names: %s%n", Arrays.toString(gc.getPoolNames()));
        }
    }

    private static void simulateWorkload() throws InterruptedException {
        List<Object> workload = new ArrayList<>();
        Runtime runtime = Runtime.getRuntime();

        System.out.println("Creating objects to trigger GC...");
        for (int i = 0; i < 10; i++) {
            long before = runtime.freeMemory();
            for (int j = 0; j < 10000; j++) {
                workload.add(new byte[1024]);
            }
            long after = runtime.freeMemory();
            System.out.printf("  Batch %d: freeMemory changed by %d KB%n",
                i + 1, (after - before) / 1024);

            if (i % 3 == 0) {
                workload.clear();
                System.gc();
                Thread.sleep(50);
            }
        }

        // Final stats
        System.out.println("\nAfter workload:");
        displayGCStats();
    }

    private static void displayTuningFlags() {
        System.out.println("Common GC tuning flags:");
        System.out.println("  -Xms4g -Xmx4g              Heap size (set equal)");
        System.out.println("  -XX:+UseG1GC                Use G1 (default Java 9+)");
        System.out.println("  -XX:+UseZGC                 Use ZGC (Java 15+)");
        System.out.println("  -XX:MaxGCPauseMillis=200    Target max pause");
        System.out.println("  -XX:NewRatio=2              Old:Young ratio");
        System.out.println("  -XX:SurvivorRatio=8         Eden:Survivor ratio");
        System.out.println("  -XX:ParallelGCThreads=8     GC thread count");
        System.out.println("  -XX:ConcGCThreads=2         Concurrent GC threads");
        System.out.println();
        System.out.println("GC logging (Java 11+):");
        System.out.println("  -Xlog:gc*:file=gc.log:time,uptime,level,tags");
        System.out.println();
        System.out.println("Monitoring commands:");
        System.out.println("  jstat -gcutil <pid> 1000   GC stats every 1s");
        System.out.println("  jcmd <pid> GC.heap_info   Heap details");
        System.out.println("  jmap -dump:live,format=b,file=heap.hprof <pid>");
    }

    private static String formatBytes(long bytes) {
        if (bytes < 0) return "N/A";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)) + " MB";
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
}

/*
Expected Output (approximate):
=== GC Tuning Demo ===

--- Current GC Configuration ---
VM Arguments:

Heap Memory:
  Init:     256 MB
  Used:     X MB
  Committed:256 MB
  Max:      4096 MB

Non-Heap Memory:
  Init:     8 MB
  Used:     X MB
  Committed:Y MB
  Max:      -1 B

--- Memory Pools ---
  G1 Eden Space                  Used: X KB, Max: Y MB
  G1 Survivor Space             Used: X KB, Max: Y MB
  G1 Old Gen                    Used: X MB, Max: Y MB
  Metaspace                     Used: X MB, Max: -1 B

--- Garbage Collectors ---
  G1 Young GC      Collections: X, Time: Xms
    Pool names: [G1 Eden Space, G1 Survivor Space]
  G1 Old GC        Collections: X, Time: Xms
    Pool names: [G1 Old Gen]

--- Simulating Workload ---
Creating objects to trigger GC...
  Batch 1: freeMemory changed by X KB
  ...
After workload:
  G1 Young GC      Collections: X, Time: Xms
  G1 Old GC        Collections: X, Time: Xms

--- GC Tuning Flags Reference ---
Common GC tuning flags:
  -Xms4g -Xmx4g              Heap size (set equal)
  -XX:+UseG1GC                Use G1 (default Java 9+)
  ...

=== End of GC Tuning Demo ===
*/
