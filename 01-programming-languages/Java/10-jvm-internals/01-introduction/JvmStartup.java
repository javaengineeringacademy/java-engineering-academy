package academy.javaengineering.jvm.introduction;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;

/**
 * JVM Startup & Bootstrap Deep Dive
 * Demonstrates the complete JVM lifecycle from command invocation to shutdown.
 */
public class JvmStartup {

    // Static initializer - runs during class initialization phase
    static {
        System.out.println("[Phase 4] Static initializer executed");
        System.out.println("  Thread: " + Thread.currentThread().getName());
    }

    // Instance initializer - runs during object construction
    {
        System.out.println("[Phase 5] Instance initializer executed");
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Startup & Bootstrap Deep Dive ===\n");

        // Phase 1: Demonstrate JVM startup sequence
        demonstrateStartupSequence();

        // Phase 2: Show JVM lifecycle states
        demonstrateLifecycle();

        // Phase 3: Show runtime information
        demonstrateRuntimeInfo();

        // Phase 4: Show shutdown hooks
        demonstrateShutdownHooks();

        // Phase 5: Show class initialization order
        demonstrateClassInitOrder();
    }

    /**
     * Phase 1: JVM Startup Sequence
     * The sequence from java command to main method execution:
     * 1. OS creates JVM process
     * 2. JVM loads and initializes
     * 3. Bootstrap classloader loads core classes
     * 4. Extension/Platform classloader loads extension classes
     * 5. Application classloader loads application classes
     * 6. Static initializers run
     * 7. main() method is invoked
     */
    private static void demonstrateStartupSequence() {
        System.out.println("--- Phase 1: JVM Startup Sequence ---");

        System.out.println("Step 1: OS creates JVM process (java command)");
        System.out.println("Step 2: JVM initialization (memory, threads, system properties)");
        System.out.println("Step 3: Bootstrap classloader loads rt.jar / java.base module");
        System.out.println("Step 4: Platform classloader loads platform modules");
        System.out.println("Step 5: Application classloader loads application classes");
        System.out.println("Step 6: Static initializers execute (<clinit>)");
        System.out.println("Step 7: main(String[] args) method invoked\n");

        // Show which classloader loaded this class
        ClassLoader loader = JvmStartup.class.getClassLoader();
        System.out.println("This class loaded by: " + loader.getClass().getName());
        System.out.println("Parent classloader: " + (loader.getParent() != null ?
            loader.getParent().getClass().getName() : "null (Bootstrap)\n"));
    }

    /**
     * Phase 2: JVM Lifecycle States
     * The JVM goes through these states:
     * - Created: JVM instance is created
     * - Initialized: JVM is initialized
     * - Running: Application is executing
     * - Shutdown: shutdown() called or non-daemon threads terminated
     * - Terminated: JVM exits
     */
    private static void demonstrateLifecycle() {
        System.out.println("--- Phase 2: JVM Lifecycle States ---");

        System.out.println("State 1: CREATED - JVM instance created by OS");
        System.out.println("State 2: INITIALIZED - JVM initializes internal structures");
        System.out.println("State 3: RUNNING - Application code executing (current state)");
        System.out.println("State 4: SHUTDOWN - Shutdown initiated (hook or exit)");
        System.out.println("State 5: TERMINATED - JVM exits with status code\n");

        // Show JVM uptime (time in RUNNING state)
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long uptimeMs = runtimeBean.getUptime();
        System.out.println("JVM Uptime: " + formatDuration(uptimeMs));
        System.out.println("Start Time: " + runtimeBean.getStartTime());
        System.out.println("Input Arguments: " + runtimeBean.getInputArguments() + "\n");
    }

    /**
     * Phase 3: Runtime Information
     * After startup, the JVM provides extensive runtime information
     */
    private static void demonstrateRuntimeInfo() {
        System.out.println("--- Phase 3: Runtime Information ---");

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();

        System.out.println("VM Name: " + runtimeBean.getVmName());
        System.out.println("VM Version: " + runtimeBean.getVmVersion());
        System.out.println("VM Vendor: " + runtimeBean.getVmVendor());
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Arch: " + System.getProperty("os.arch"));
        System.out.println("Available Processors: " + runtime.availableProcessors());
        System.out.println("Max Memory: " + formatMemory(runtime.maxMemory()));
        System.out.println("Total Memory: " + formatMemory(runtime.totalMemory()));
        System.out.println("Free Memory: " + formatMemory(runtime.freeMemory()));
        System.out.println("Class Path: " + System.getProperty("java.class.path"));
        System.out.println("Library Path: " + System.getProperty("java.library.path"));
        System.out.println("File Encoding: " + System.getProperty("file.encoding"));
        System.out.println();
    }

    /**
     * Phase 4: Shutdown Hooks
     * The JVM supports shutdown hooks for cleanup before termination
     */
    private static void demonstrateShutdownHooks() {
        System.out.println("--- Phase 4: Shutdown Hooks ---");

        // Register shutdown hooks
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[Shutdown Hook 1] Cleaning up resources...");
        }));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[Shutdown Hook 2] Flushing buffers...");
        }));

        System.out.println("Two shutdown hooks registered.");
        System.out.println("They will execute when JVM begins shutdown.\n");

        System.out.println("Shutdown triggers:");
        System.out.println("1. All non-daemon threads terminate");
        System.out.println("2. System.exit(status) called");
        System.out.println("3. Ctrl+C (SIGINT) received");
        System.out.println("4. SIGTERM received");
        System.out.println("5. Uncaught exception in non-daemon thread\n");
    }

    /**
     * Phase 5: Class Initialization Order
     * Demonstrates the order of static and instance initialization
     */
    private static void demonstrateClassInitOrder() {
        System.out.println("--- Phase 5: Class Initialization Order ---");

        System.out.println("Creating new JvmStartup instance...");
        System.out.println("(Watch for static and instance initializer output above)\n");

        JvmStartup instance = new JvmStartup();

        System.out.println("Instance created successfully.");
        System.out.println("\n=== JVM Startup Complete ===");
    }

    /**
     * Demonstrates System.exit() behavior
     */
    public static void demonstrateSystemExit() {
        System.out.println("--- System.exit() Behavior ---");
        System.out.println("System.exit(0) - Normal termination");
        System.out.println("System.exit(1) - Abnormal termination");
        System.out.println("Runtime.halt() - Forced termination (no shutdown hooks)");
        System.out.println();
    }

    /**
     * Demonstrates JVM shutdown sequence
     */
    public static void demonstrateShutdownSequence() {
        System.out.println("--- JVM Shutdown Sequence ---");
        System.out.println("1. All non-daemon threads are stopped");
        System.out.println("2. Any remaining daemon threads are stopped");
        System.out.println("3. Shutdown hooks execute (in reverse registration order)");
        System.out.println("4. Finalizers run (if enabled)");
        System.out.println("5. JVM exits\n");
    }

    /**
     * Demonstrates JVM arguments parsing
     */
    public static void demonstrateJvmArguments() {
        System.out.println("--- JVM Arguments ---");

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeBean.getInputArguments();

        System.out.println("JVM Arguments (" + jvmArgs.size() + " total):");
        for (String arg : jvmArgs) {
            System.out.println("  " + arg);
        }
        System.out.println();
    }

    /**
     * Demonstrates system properties
     */
    public static void demonstrateSystemProperties() {
        System.out.println("--- Important System Properties ---");

        String[] importantProps = {
            "java.version", "java.vendor", "java.home",
            "os.name", "os.version", "os.arch",
            "file.encoding", "user.dir", "user.home",
            "java.class.path", "java.library.path"
        };

        for (String prop : importantProps) {
            System.out.printf("  %-25s = %s%n", prop, System.getProperty(prop));
        }
        System.out.println();
    }

    private static String formatDuration(long ms) {
        long hours = ms / (1000 * 60 * 60);
        long minutes = (ms % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (ms % (1000 * 60)) / 1000;
        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }

    private static String formatMemory(long bytes) {
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
}
