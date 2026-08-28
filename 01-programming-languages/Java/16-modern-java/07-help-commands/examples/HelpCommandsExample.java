package academy.javaengineering.modern.helpcommands;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * Help commands examples demonstrating Java diagnostic tools.
 */
public class HelpCommandsExample {

    public static void main(String[] args) {
        // Runtime information
        System.out.println("=== Runtime Information ===");
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Architecture: " + System.getProperty("os.arch"));
        System.out.println("Available Processors: " + runtime.availableProcessors());
        System.out.println("Uptime: " + runtime.getUptime() + " ms");

        // Memory information
        System.out.println("\n=== Memory Information ===");
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        System.out.println("Heap Memory Used: " + 
            memory.getHeapMemoryUsage().getUsed() / (1024 * 1024) + " MB");
        System.out.println("Heap Memory Max: " + 
            memory.getHeapMemoryUsage().getMax() / (1024 * 1024) + " MB");
        System.out.println("Non-Heap Memory Used: " + 
            memory.getNonHeapMemoryUsage().getUsed() / (1024 * 1024) + " MB");

        // Thread information
        System.out.println("\n=== Thread Information ===");
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        System.out.println("Thread Count: " + threads.getThreadCount());
        System.out.println("Peak Thread Count: " + threads.getPeakThreadCount());
        System.out.println("Daemon Thread Count: " + threads.getDaemonThreadCount());

        // System properties
        System.out.println("\n=== Important System Properties ===");
        List<String> importantProps = List.of(
            "java.version", "java.vendor", "java.home",
            "os.name", "os.version", "user.dir", "user.home"
        );
        for (String prop : importantProps) {
            System.out.println(prop + ": " + System.getProperty(prop));
        }

        // JVM arguments
        System.out.println("\n=== JVM Arguments ===");
        runtime.getInputArguments().forEach(arg -> System.out.println("  " + arg));

        // Class loading
        System.out.println("\n=== Class Loading ===");
        System.out.println("Classes Loaded: " + ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount());
        System.out.println("Classes Currently Loaded: " + ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
    }
}
