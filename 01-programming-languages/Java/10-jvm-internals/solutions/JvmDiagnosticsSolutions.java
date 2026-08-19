package academy.javaengineering.jvm.solutions;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * JVM Diagnostics Solutions - Complete implementations
 */
public class JvmDiagnosticsSolutions {

    /**
     * Exercise 1 Solution: Thread dump analyzer
     */
    public static void analyzeThreadDump() {
        System.out.println("=== Thread Dump Analyzer ===\n");

        // Create threads in various states
        final Object lock = new Object();
        final Object lock2 = new Object();

        // RUNNABLE thread
        Thread t1 = new Thread(() -> {
            long sum = 0;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sum += i;
            }
        }, "RUNNABLE-THREAD-1");

        // BLOCKED thread
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                // Hold lock, prevent other thread from acquiring
                try { Thread.sleep(10000); } catch (InterruptedException e) {}
            }
        }, "HOLDING-LOCK-THREAD");

        Thread t3 = new Thread(() -> {
            synchronized (lock) { // This will block
                System.out.println("Acquired lock");
            }
        }, "BLOCKED-THREAD");

        // WAITING thread
        Thread t4 = new Thread(() -> {
            try {
                lock2.wait();
            } catch (InterruptedException e) {}
        }, "WAITING-THREAD");

        // TIMED_WAITING thread
        Thread t5 = new Thread(() -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {}
        }, "SLEEPING-THREAD");

        // Start threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Analyze thread dump
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = tmx.dumpAllThreads(true, true);

        Map<Thread.State, Integer> stateCounts = new HashMap<>();
        for (Thread.State state : Thread.State.values()) {
            stateCounts.put(state, 0);
        }

        System.out.println("Thread Dump Analysis:");
        System.out.println(String.format("%-30s %-20s %-10s", "Name", "State", "Blocked Count"));
        System.out.println("-".repeat(60));

        for (ThreadInfo info : threadInfos) {
            Thread.State state = info.getThreadState();
            stateCounts.merge(state, 1, Integer::sum);

            System.out.println(String.format("%-30s %-20s %-10d",
                    info.getThreadName(), state, info.getBlockedCount()));
        }

        System.out.println("\nState Summary:");
        stateCounts.forEach((state, count) -> {
            if (count > 0) {
                System.out.println("  " + state + ": " + count);
            }
        });

        // Detect deadlocks
        long[] deadlockedThreads = tmx.findDeadlockedThreads();
        if (deadlockedThreads != null) {
            ThreadInfo[] deadlockInfo = tmx.getThreadInfo(deadlockedThreads, true, true);
            System.out.println("\nDEADLOCK DETECTED!");
            for (ThreadInfo info : deadlockInfo) {
                System.out.println("  Thread: " + info.getThreadName());
                System.out.println("  Waiting to lock: " + info.getLockName());
                System.out.println("  Held by: " + info.getLockOwnerName());
            }
        } else {
            System.out.println("\nNo deadlocks detected");
        }

        // Cleanup
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
        t4.interrupt();
        t5.interrupt();
    }

    /**
     * Exercise 2 Solution: GC activity monitor
     */
    public static void monitorGcActivity() {
        System.out.println("=== GC Activity Monitor ===\n");

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        // Record baseline
        Map<String, long[]> baseline = new HashMap<>();
        for (GarbageCollectorMXBean gc : gcBeans) {
            baseline.put(gc.getName(), new long[]{gc.getCollectionCount(), gc.getCollectionTime()});
        }

        // Generate GC activity
        System.out.println("Generating GC activity...");
        List<byte[]> garbage = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            garbage.add(new byte[1024 * 100]); // 100KB
            if (i % 10 == 0) {
                garbage.clear();
                System.gc();
            }
        }

        // Report results
        System.out.println("\nGC Statistics:");
        for (GarbageCollectorMXBean gc : gcBeans) {
            long[] base = baseline.get(gc.getName());
            if (base != null) {
                long countDiff = gc.getCollectionCount() - base[0];
                long timeDiff = gc.getCollectionTime() - base[1];
                System.out.printf("  %s: %d collections, %dms total%n",
                        gc.getName(), countDiff, timeDiff);
                if (countDiff > 0) {
                    System.out.printf("    Average pause: %.2fms%n",
                            (double) timeDiff / countDiff);
                }
            }
        }
    }

    /**
     * Exercise 3 Solution: Heap dump analyzer
     */
    public static void analyzeHeapDump() {
        System.out.println("=== Heap Dump Analyzer ===\n");

        // Create objects of different types
        List<Object> objects = new ArrayList<>();

        // Different object types and sizes
        for (int i = 0; i < 1000; i++) {
            objects.add(new byte[1024]);      // 1KB arrays
            objects.add(new String("Object-" + i));
            objects.add(new HashMap<>());
            objects.add(new ArrayList<>());
            objects.add(new LinkedList<>());
        }

        // Get memory stats
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memBean.getHeapMemoryUsage();

        System.out.println("Heap Memory Analysis:");
        System.out.println("  Used: " + (heap.getUsed() / 1024 / 1024) + " MB");
        System.out.println("  Committed: " + (heap.getCommitted() / 1024 / 1024) + " MB");
        System.out.println("  Max: " + (heap.getMax() / 1024 / 1024) + " MB");
        System.out.println("  Usage: " + (heap.getUsed() * 100 / heap.getMax()) + "%");

        // Memory pool details
        System.out.println("\nMemory Pools:");
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            MemoryUsage usage = pool.getUsage();
            System.out.printf("  %-20s: %dMB / %dMB (%d%%)%n",
                    pool.getName(),
                    usage.getUsed() / 1024 / 1024,
                    usage.getMax() / 1024 / 1024,
                    usage.getUsed() * 100 / usage.getMax());
        }

        System.out.println("\nObjects created: " + objects.size());
        System.out.println("\nFor detailed analysis, run:");
        System.out.println("  jcmd " + ProcessHandle.current().pid() + " GC.class_histogram");
        System.out.println("  jcmd " + ProcessHandle.current().pid() + " GC.heap_dump /tmp/heap.hprof");

        objects.clear();
    }

    /**
     * Exercise 4 Solution: Memory leak detector
     */
    private static final List<byte[]> leakyCollection = new ArrayList<>();

    public static void detectMemoryLeak() {
        System.out.println("=== Memory Leak Detector ===\n");

        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        List<Long> usageHistory = new ArrayList<>();

        // Simulate slow leak
        Thread leakThread = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                leakyCollection.add(new byte[1024 * 100]); // 100KB
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
            }
        });
        leakThread.start();

        // Monitor heap usage
        System.out.println("Monitoring heap usage...");
        for (int i = 0; i < 30; i++) {
            MemoryUsage heap = memBean.getHeapMemoryUsage();
            long used = heap.getUsed();
            usageHistory.add(used);

            // Calculate growth rate
            double growthRate = 0;
            if (usageHistory.size() > 5) {
                long oldUsage = usageHistory.get(usageHistory.size() - 5);
                growthRate = (used - oldUsage) / 1024.0;
            }

            System.out.printf("  [%2d] Heap: %dMB, Growth: %.1fKB/interval%n",
                    i, used / 1024 / 1024, growthRate);

            if (i > 10 && growthRate > 100) {
                System.out.println("  WARNING: Possible memory leak detected!");
            }

            try { Thread.sleep(200); } catch (InterruptedException e) { break; }
        }

        // Cleanup
        leakyCollection.clear();
        System.gc();
    }

    /**
     * Exercise 5 Solution: Diagnostic report generator
     */
    public static void generateDiagnosticReport() {
        System.out.println("=== JVM Diagnostic Report ===\n");

        StringBuilder report = new StringBuilder();
        Runtime rt = Runtime.getRuntime();

        // JVM Info
        report.append("JVM INFORMATION\n");
        report.append("  Version: ").append(System.getProperty("java.version")).append("\n");
        report.append("  VM Name: ").append(System.getProperty("java.vm.name")).append("\n");
        report.append("  VM Version: ").append(System.getProperty("java.vm.version")).append("\n");
        report.append("  VM Vendor: ").append(System.getProperty("java.vm.vendor")).append("\n");
        report.append("  Java Home: ").append(System.getProperty("java.home")).append("\n\n");

        // Memory
        report.append("MEMORY\n");
        report.append(String.format("  Max Heap:     %d MB%n", rt.maxMemory() / 1024 / 1024));
        report.append(String.format("  Total Heap:   %d MB%n", rt.totalMemory() / 1024 / 1024));
        report.append(String.format("  Free Heap:    %d MB%n", rt.freeMemory() / 1024 / 1024));
        report.append(String.format("  Used Heap:    %d MB%n",
                (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024));
        report.append(String.format("  Heap Usage:   %.1f%%%n",
                (rt.totalMemory() - rt.freeMemory()) * 100.0 / rt.maxMemory()));

        // Non-heap memory
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();
        report.append(String.format("  Non-heap:     %d MB%n", nonHeap.getUsed() / 1024 / 1024));

        // GC
        report.append("\nGARBAGE COLLECTION\n");
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            report.append(String.format("  %s:%n", gc.getName()));
            report.append(String.format("    Collections: %d%n", gc.getCollectionCount()));
            report.append(String.format("    Total Time:  %dms%n", gc.getCollectionTime()));
            if (gc.getCollectionCount() > 0) {
                report.append(String.format("    Avg Pause:   %.2fms%n",
                        (double) gc.getCollectionTime() / gc.getCollectionCount()));
            }
        }

        // Threads
        report.append("\nTHREADS\n");
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        report.append(String.format("  Current:     %d%n", tmx.getThreadCount()));
        report.append(String.format("  Peak:        %d%n", tmx.getPeakThreadCount()));
        report.append(String.format("  Daemon:      %d%n", tmx.getDaemonThreadCount()));
        report.append(String.format("  Total Started: %d%n", tmx.getTotalStartedThreadCount()));

        // OS
        report.append("\nOPERATING SYSTEM\n");
        report.append("  Name:    ").append(System.getProperty("os.name")).append("\n");
        report.append("  Arch:    ").append(System.getProperty("os.arch")).append("\n");
        report.append("  Version: ").append(System.getProperty("os.version")).append("\n");
        report.append("  Processors: ").append(rt.availableProcessors()).append("\n");

        // Process
        report.append("\nPROCESS\n");
        report.append("  PID:     ").append(ProcessHandle.current().pid()).append("\n");
        report.append("  User:    ").append(System.getProperty("user.name")).append("\n");
        report.append("  Dir:     ").append(System.getProperty("user.dir")).append("\n");

        System.out.println(report.toString());
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Diagnostics Solutions ===\n");

        // Exercise 1
        System.out.println("Exercise 1: Thread Dump Analyzer");
        analyzeThreadDump();

        // Exercise 2
        System.out.println("\n---");
        System.out.println("Exercise 2: GC Activity Monitor");
        monitorGcActivity();

        // Exercise 3
        System.out.println("\n---");
        System.out.println("Exercise 3: Heap Dump Analyzer");
        analyzeHeapDump();

        // Exercise 4
        System.out.println("\n---");
        System.out.println("Exercise 4: Memory Leak Detector");
        detectMemoryLeak();

        // Exercise 5
        System.out.println("\n---");
        System.out.println("Exercise 5: Diagnostic Report");
        generateDiagnosticReport();
    }
}
