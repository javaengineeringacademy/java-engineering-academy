package academy.javaengineering.modern.helpcommands;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

/**
 * Solutions for Help Commands practice exercises.
 */
public class HelpCommandsSolutions {

    // Exercise 1: System Info Tool
    public static void displaySystemInfo() {
        System.out.println("=== System Information ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Max Memory: " + formatMemory(Runtime.getRuntime().maxMemory()));
        System.out.println("User Home: " + System.getProperty("user.home"));
    }

    // Exercise 2: Memory Monitor
    public static void displayMemoryInfo() {
        System.out.println("=== Memory Monitor ===");
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();

        long used = heapUsage.getUsed();
        long max = heapUsage.getMax();
        double percentage = (double) used / max * 100;

        System.out.println("Heap Used: " + formatMemory(used));
        System.out.println("Heap Max: " + formatMemory(max));
        System.out.printf("Usage: %.1f%%%n", percentage);

        System.out.println("\nRunning GC...");
        System.gc();

        MemoryUsage afterGC = memoryBean.getHeapMemoryUsage();
        System.out.println("After GC:");
        System.out.println("Heap Used: " + formatMemory(afterGC.getUsed()));
        System.out.printf("Freed: %s%n", formatMemory(used - afterGC.getUsed()));
    }

    // Exercise 3: Thread Dumper
    public static void displayThreadInfo() {
        System.out.println("=== Thread Information ===");
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        System.out.println("Total Threads: " + threadBean.getThreadCount());
        System.out.println("Peak Threads: " + threadBean.getPeakThreadCount());
        System.out.println("Daemon Threads: " + threadBean.getDaemonThreadCount());
        System.out.println("User Threads: " + (threadBean.getThreadCount() - threadBean.getDaemonThreadCount()));

        System.out.println("\nThread States:");
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread thread : threads) {
            if (thread != null) {
                System.out.printf("  %s: %s (Priority: %d, Daemon: %s)%n",
                    thread.getName(),
                    thread.getState(),
                    thread.getPriority(),
                    thread.isDaemon());
            }
        }
    }

    // Exercise 4: Process Monitor
    public static void displayProcessInfo() {
        System.out.println("=== Process Monitor ===");
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        System.out.println("Uptime: " + formatDuration(runtimeBean.getUptime()));
        System.out.println("VM Name: " + runtimeBean.getVmName());
        System.out.println("VM Version: " + runtimeBean.getVmVersion());
        System.out.println("VM Vendor: " + runtimeBean.getVmVendor());
        System.out.println("Available Processors: " + osBean.getAvailableProcessors());
        System.out.println("System Load Average: " + osBean.getSystemLoadAverage());
        System.out.println("OS Name: " + osBean.getName());
        System.out.println("OS Version: " + osBean.getVersion());
    }

    static String formatMemory(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    static String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: System Info ---");
        displaySystemInfo();

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Memory Monitor ---");
        displayMemoryInfo();

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Thread Dumper ---");
        displayThreadInfo();

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Process Monitor ---");
        displayProcessInfo();
    }
}
