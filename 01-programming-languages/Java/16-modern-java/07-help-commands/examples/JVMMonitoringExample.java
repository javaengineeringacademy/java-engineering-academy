package academy.javaengineering.modern.helpcommands;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Map;

/**
 * JVM monitoring and diagnostics examples.
 */
public class JVMMonitoringExample {

    public static void main(String[] args) {
        // Memory monitoring
        System.out.println("=== Memory Monitoring ===");
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();

        System.out.println("Heap Memory:");
        System.out.println("  Init: " + formatMemory(heapUsage.getInit()));
        System.out.println("  Used: " + formatMemory(heapUsage.getUsed()));
        System.out.println("  Committed: " + formatMemory(heapUsage.getCommitted()));
        System.out.println("  Max: " + formatMemory(heapUsage.getMax()));

        System.out.println("\nNon-Heap Memory:");
        System.out.println("  Init: " + formatMemory(nonHeapUsage.getInit()));
        System.out.println("  Used: " + formatMemory(nonHeapUsage.getUsed()));
        System.out.println("  Committed: " + formatMemory(nonHeapUsage.getCommitted()));

        // Thread monitoring
        System.out.println("\n=== Thread Monitoring ===");
        var threadBean = ManagementFactory.getThreadMXBean();
        System.out.println("Thread Count: " + threadBean.getThreadCount());
        System.out.println("Peak Thread Count: " + threadBean.getPeakThreadCount());
        System.out.println("Daemon Thread Count: " + threadBean.getDaemonThreadCount());

        // Garbage collection
        System.out.println("\n=== Garbage Collection ===");
        var gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (var gcBean : gcBeans) {
            System.out.println("GC Name: " + gcBean.getName());
            System.out.println("  Collection Count: " + gcBean.getCollectionCount());
            System.out.println("  Collection Time: " + gcBean.getCollectionTime() + " ms");
        }

        // Operating system
        System.out.println("\n=== Operating System ===");
        var osBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println("OS Name: " + osBean.getName());
        System.out.println("OS Version: " + osBean.getVersion());
        System.out.println("Available Processors: " + osBean.getAvailableProcessors());
        System.out.println("System Load Average: " + osBean.getSystemLoadAverage());

        // Runtime
        System.out.println("\n=== Runtime ===");
        var runtimeBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("Uptime: " + formatDuration(runtimeBean.getUptime()));
        System.out.println("VM Name: " + runtimeBean.getVmName());
        System.out.println("VM Version: " + runtimeBean.getVmVersion());
        System.out.println("VM Vendor: " + runtimeBean.getVmVendor());
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
}
