package jvm;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * VisualVmExample - VisualVM profiling and monitoring example
 *
 * Demonstrates:
 * - JMX beans for VisualVM monitoring
 * - CPU sampling simulation
 * - Memory profiling data
 * - Thread monitoring
 * - Heap dump triggers
 * - VisualVM connection methods
 * - MBean registration for custom monitoring
 *
 * Connect with VisualVM:
 *   1. Start VisualVM: visualvm
 *   2. The application appears in Local tab
 *   3. Double-click to open monitoring views
 *
 * For remote connections:
 *   java -Dcom.sun.management.jmxremote \
 *        -Dcom.sun.management.jmxremote.port=9010 \
 *        -Dcom.sun.management.jmxremote.authenticate=false \
 *        -Dcom.sun.management.jmxremote.ssl=false \
 *        VisualVmExample
 */
public class VisualVmExample {

    public static void main(String[] args) throws Exception {
        System.out.println("=== VisualVM Profiling Example ===\n");

        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Open VisualVM and connect to this process.\n");

        // Set up JMX for remote monitoring
        printJmxConfiguration();

        // Show available management beans
        printManagementBeans();

        // Run workload for profiling
        System.out.println("=== Running Profilable Workload ===\n");
        runWorkload();

        // Show VisualVM features
        printVisualVmFeatures();
    }

    static void printJmxConfiguration() {
        System.out.println("=== JMX Remote Configuration ===\n");
        System.out.println("Enable JMX remote access:");
        System.out.println("  -Dcom.sun.management.jmxremote");
        System.out.println("  -Dcom.sun.management.jmxremote.port=9010");
        System.out.println("  -Dcom.sun.management.jmxremote.authenticate=false");
        System.out.println("  -Dcom.sun.management.jmxremote.ssl=false");
        System.out.println();
        System.out.println("VisualVM connection steps:");
        System.out.println("  1. File -> Add JMX Connection");
        System.out.println("  2. Enter: <hostname>:9010");
        System.out.println("  3. Check 'Use JMX Service' checkbox");
        System.out.println("  4. Click OK");
        System.out.println();
    }

    static void printManagementBeans() {
        System.out.println("=== Available Management Beans ===\n");

        // Memory beans
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();

        System.out.println("MemoryMXBean:");
        System.out.printf("  Heap: used=%dMB, committed=%dMB, max=%dMB%n",
                heap.getUsed() / (1024 * 1024),
                heap.getCommitted() / (1024 * 1024),
                heap.getMax() / (1024 * 1024));
        System.out.printf("  Non-Heap: used=%dMB, committed=%dMB%n",
                nonHeap.getUsed() / (1024 * 1024),
                nonHeap.getCommitted() / (1024 * 1024));
        System.out.println();

        // Thread beans
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        System.out.println("ThreadMXBean:");
        System.out.println("  Thread count: " + threadBean.getThreadCount());
        System.out.println("  Peak thread count: " + threadBean.getPeakThreadCount());
        System.out.println("  Daemon thread count: " + threadBean.getDaemonThreadCount());
        System.out.println("  Total started: " + threadBean.getTotalStartedThreadCount());
        System.out.println();

        // Runtime beans
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("RuntimeMXBean:");
        System.out.println("  Uptime: " + runtimeBean.getUptime() + " ms");
        System.out.println("  VM Name: " + runtimeBean.getVmName());
        System.out.println("  VM Version: " + runtimeBean.getVmVersion());
        System.out.println("  Input args: " + runtimeBean.getInputArguments());
        System.out.println();

        // GC beans
        System.out.println("GarbageCollectorMXBeans:");
        for (GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.printf("  %s: collections=%d, time=%d ms%n",
                    gcBean.getName(),
                    gcBean.getCollectionCount(),
                    gcBean.getCollectionTime());
        }
        System.out.println();

        // OS beans
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println("OperatingSystemMXBean:");
        System.out.println("  Available processors: " + osBean.getAvailableProcessors());
        System.out.println("  System load average: " + osBean.getSystemLoadAverage());
        System.out.println();

        // Compilation beans
        CompilationMXBean compBean = ManagementFactory.getCompilationMXBean();
        if (compBean != null) {
            System.out.println("CompilationMXBean:");
            System.out.println("  Compiler: " + compBean.getName());
            System.out.println("  Total compilation time: " + compBean.getTotalCompilationTime() + " ms");
        }
        System.out.println();
    }

    static void runWorkload() throws InterruptedException {
        // CPU work
        System.out.println("Starting CPU-intensive work...");
        long start = System.currentTimeMillis();
        double result = 0;
        for (int i = 0; i < 2_000_000; i++) {
            result += Math.sin(i * 0.001);
        }
        System.out.printf("  CPU work done: %.2f (%d ms)%n", result, System.currentTimeMillis() - start);

        // Allocation work
        System.out.println("Starting allocation-intensive work...");
        start = System.currentTimeMillis();
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < 500_000; i++) {
            objects.add(new byte[256]);
        }
        System.out.printf("  Allocated 500K objects (%d ms)%n", System.currentTimeMillis() - start);

        // Thread work
        System.out.println("Starting multi-threaded work...");
        start = System.currentTimeMillis();
        Thread[] threads = new Thread[8];
        for (int i = 0; i < threads.length; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    Math.sin(threadNum + j);
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.printf("  Multi-threaded work done (%d ms)%n", System.currentTimeMillis() - start);

        // Clean up
        objects.clear();
        objects = null;
        System.gc();
        System.out.println("\nWorkload complete. Use VisualVM to inspect:");
        System.out.println("  - CPU tab: view CPU usage over time");
        System.out.println("  - Sampler tab: CPU and memory sampling");
        System.out.println("  - Profiler tab: detailed method-level profiling");
        System.out.println("  - Threads tab: thread states and deadlock detection");
        System.out.println("  - Visual GC tab: garbage collection visualization");
    }

    static void printVisualVmFeatures() {
        System.out.println("\n=== VisualVM Features ===\n");
        System.out.println("Monitor Tab:");
        System.out.println("  - CPU usage over time");
        System.out.println("  - Memory usage (heap and metaspace)");
        System.out.println("  - Thread count and states");
        System.out.println("  - Classes loaded count");
        System.out.println("  - GC activity");
        System.out.println();
        System.out.println("Sampler Tab (no overhead when not sampling):");
        System.out.println("  - CPU Sampler: periodic call stack sampling");
        System.out.println("  - Memory Sampler: periodic heap allocation tracking");
        System.out.println("  - Start/Stop/Reset buttons");
        System.out.println();
        System.out.println("Profiler Tab (higher overhead, more detail):");
        System.out.println("  - CPU Profiler: method-level timing");
        System.out.println("  - Memory Profiler: allocation tracking");
        System.out.println("  - Configurable settings (CPU profiling depth)");
        System.out.println();
        System.out.println("Threads Tab:");
        System.out.println("  - Thread state visualization (timeline)");
        System.out.println("  - Thread dump capture");
        System.out.println("  - Deadlock detection");
        System.out.println();
        System.out.println("Plugins:");
        System.out.println("  - Visual GC: GC visualization");
        System.out.println("  - Sampler: extended profiling");
        System.out.println("  - BTrace: dynamic tracing");
    }
}
