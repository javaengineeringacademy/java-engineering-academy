package academy.javaengineering.jvm.practices;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * JVM Diagnostics Exercises
 * Complete each exercise by implementing the required method.
 * Focus on jcmd, jstack, jmap, jstat, and diagnostic tools.
 */
public class JvmDiagnosticsExercises {

    /**
     * Exercise 1: Create a thread dump analyzer
     * Write code that:
     * 1. Creates threads in various states (RUNNABLE, BLOCKED, WAITING)
     * 2. Programmatically captures thread dump
     * 3. Analyzes thread states and counts
     * 4. Identifies potential issues
     *
     * Output should include:
     * - Total thread count
     * - Count per state
     * - Longest running thread
     * - Any detected deadlocks
     */
    public static void analyzeThreadDump() {
        // TODO: Implement thread dump analyzer
        // HINT: Use Thread.getAllStackTraces()
        // HINT: Use ManagementFactory.getThreadMXBean()

        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        System.out.println("Thread count: " + tmx.getThreadCount());
        System.out.println("Peak thread count: " + tmx.getPeakThreadCount());
        System.out.println("Daemon thread count: " + tmx.getDaemonThreadCount());
        System.out.println("Total started: " + tmx.getTotalStartedThreadCount());

        // TODO: Get all thread dumps and analyze
        Map<Thread, StackTraceElement[]> dumps = Thread.getAllStackTraces();
        // Count by state
        // Find deadlocks
        // Print analysis
    }

    /**
     * Exercise 2: Monitor GC activity in real-time
     * Write code that:
     * 1. Starts a GC monitoring thread
     * 2. Prints GC count, time, and frequency
     * 3. Calculates GC overhead
     * 4. Detects GC pressure
     *
     * Run with: java -Xlog:gc* JvmDiagnosticsExercises
     */
    static volatile boolean monitoring = true;

    public static void monitorGcActivity() {
        // TODO: Implement GC monitoring
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        Thread monitorThread = new Thread(() -> {
            long lastGcCount = 0;
            long lastGcTime = 0;

            while (monitoring) {
                for (GarbageCollectorMXBean gc : gcBeans) {
                    long count = gc.getCollectionCount();
                    long time = gc.getCollectionTime();
                    System.out.printf("[%s] Count: %d, Time: %dms, Frequency: %.2f/sec%n",
                            gc.getName(), count, time,
                            (count - lastGcCount) / 10.0);
                    lastGcCount = count;
                    lastGcTime = time;
                }
                try {
                    Thread.sleep(10000); // 10 seconds
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        monitorThread.setDaemon(true);
        monitorThread.start();

        // Generate some GC activity
        System.out.println("Generating GC activity...");
        List<byte[]> garbage = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            garbage.add(new byte[1024 * 1024]); // 1MB each
            if (i % 10 == 0) {
                garbage.clear();
                System.gc();
            }
        }

        monitoring = false;
    }

    /**
     * Exercise 3: Create a heap dump analyzer
     * Write code that:
     * 1. Creates objects of different types and sizes
     * 2. Holds references to prevent GC
     * 3. Generates object histogram
     * 4. Identifies largest objects
     *
     * Use: jmap -histo <pid> or jcmd <pid> GC.class_histogram
     */
    public static void analyzeHeapDump() {
        // TODO: Implement heap analysis
        // Create objects to analyze
        List<Object> objects = new ArrayList<>();

        // Different object types
        for (int i = 0; i < 1000; i++) {
            objects.add(new byte[1024]);      // 1KB arrays
            objects.add(new String("Object" + i));
            objects.add(new HashMap<>());
            objects.add(new ArrayList<>());
        }

        // Get memory stats
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memBean.getHeapMemoryUsage();
        System.out.println("Heap used: " + (heap.getUsed() / 1024 / 1024) + " MB");
        System.out.println("Heap max: " + (heap.getMax() / 1024 / 1024) + " MB");
        System.out.println("Objects created: " + objects.size());

        System.out.println("\nRun: jcmd " + ProcessHandle.current().pid() + " GC.class_histogram");
    }

    /**
     * Exercise 4: Detect memory leaks using jstat
     * Write code that:
     * 1. Creates a memory leak (static collection)
     * 2. Monitors heap usage over time
     * 3. Detects the leak pattern
     * 4. Prints warning when leak detected
     *
     * Use: jstat -gcutil <pid> 1000
     */
    private static final List<byte[]> leakyCollection = new ArrayList<>();

    public static void detectMemoryLeak() {
        // TODO: Implement memory leak detection
        // Simulate slow leak
        Thread leakThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                leakyCollection.add(new byte[1024 * 100]); // 100KB
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        leakThread.start();

        // Monitor heap usage
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        long[] previousUsage = new long[10];
        int index = 0;

        for (int i = 0; i < 50; i++) {
            MemoryUsage heap = memBean.getHeapMemoryUsage();
            long used = heap.getUsed();
            System.out.printf("Heap used: %d MB (growth: %d KB)%n",
                    used / 1024 / 1024,
                    (used - previousUsage[index]) / 1024);
            previousUsage[index] = used;
            index = (index + 1) % previousUsage.length;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Exercise 5: Create a diagnostic report
     * Write code that generates a comprehensive JVM diagnostic report including:
     * 1. JVM version and configuration
     * 2. Memory statistics
     * 3. Thread information
     * 4. GC statistics
     * 5. Operating system info
     *
     * Output as formatted text report
     */
    public static void generateDiagnosticReport() {
        // TODO: Implement diagnostic report generator
        StringBuilder report = new StringBuilder();

        report.append("=== JVM DIAGNOSTIC REPORT ===\n\n");

        // JVM Info
        report.append("JVM Information:\n");
        report.append("  Version: ").append(System.getProperty("java.version")).append("\n");
        report.append("  VM Name: ").append(System.getProperty("java.vm.name")).append("\n");
        report.append("  VM Version: ").append(System.getProperty("java.vm.version")).append("\n");
        report.append("  VM Vendor: ").append(System.getProperty("java.vm.vendor")).append("\n");

        // Memory
        report.append("\nMemory Statistics:\n");
        Runtime rt = Runtime.getRuntime();
        report.append("  Max Memory: ").append(rt.maxMemory() / 1024 / 1024).append(" MB\n");
        report.append("  Total Memory: ").append(rt.totalMemory() / 1024 / 1024).append(" MB\n");
        report.append("  Free Memory: ").append(rt.freeMemory() / 1024 / 1024).append(" MB\n");

        // GC
        report.append("\nGC Statistics:\n");
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            report.append("  ").append(gc.getName()).append(": ");
            report.append(gc.getCollectionCount()).append(" collections, ");
            report.append(gc.getCollectionTime()).append("ms total\n");
        }

        // Threads
        report.append("\nThread Statistics:\n");
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        report.append("  Thread Count: ").append(tmx.getThreadCount()).append("\n");
        report.append("  Peak Thread Count: ").append(tmx.getPeakThreadCount()).append("\n");
        report.append("  Daemon Thread Count: ").append(tmx.getDaemonThreadCount()).append("\n");

        // OS
        report.append("\nOperating System:\n");
        report.append("  Name: ").append(System.getProperty("os.name")).append("\n");
        report.append("  Arch: ").append(System.getProperty("os.arch")).append("\n");
        report.append("  Processors: ").append(rt.availableProcessors()).append("\n");

        System.out.println(report.toString());
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Diagnostics Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Thread Dump Analysis");
        analyzeThreadDump();

        // Test Exercise 2
        System.out.println("\nExercise 2: GC Monitoring");
        monitorGcActivity();

        // Test Exercise 3
        System.out.println("\nExercise 3: Heap Dump Analysis");
        analyzeHeapDump();

        // Test Exercise 4
        System.out.println("\nExercise 4: Memory Leak Detection");
        detectMemoryLeak();

        // Test Exercise 5
        System.out.println("\nExercise 5: Diagnostic Report");
        generateDiagnosticReport();
    }
}
