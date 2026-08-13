package academy.javaengineering.exceptions.error.internals;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates JVM internals behind Error types.
 *
 * <p>This class shows memory monitoring, stack depth analysis,
 * and class loading mechanics that lead to various Error conditions.
 */
public class ErrorInternals {

    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private static final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

    public static void main(String[] args) {
        System.out.println("=== JVM Error Internals ===");
        System.out.println();

        demonstrateMemoryMonitoring();
        demonstrateStackDepth();
        demonstrateClassLoading();
        demonstrateMemoryRegions();
        demonstrateShutdownHooks();
    }

    // ----------------------------------------------------------------
    // 1. Memory Monitoring
    // ----------------------------------------------------------------

    /**
     * Shows how to monitor JVM memory before OOM occurs.
     */
    static void demonstrateMemoryMonitoring() {
        System.out.println("1. Memory Monitoring");
        System.out.println("-".repeat(40));

        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();

        System.out.println("Heap Memory:");
        System.out.println("  Init:   " + formatBytes(heapUsage.getInit()));
        System.out.println("  Used:   " + formatBytes(heapUsage.getUsed()));
        System.out.println("  Committed: " + formatBytes(heapUsage.getCommitted()));
        System.out.println("  Max:    " + formatBytes(heapUsage.getMax()));
        System.out.println();

        System.out.println("Non-Heap Memory:");
        System.out.println("  Init:   " + formatBytes(nonHeapUsage.getInit()));
        System.out.println("  Used:   " + formatBytes(nonHeapUsage.getUsed()));
        System.out.println("  Committed: " + formatBytes(nonHeapUsage.getCommitted()));
        System.out.println("  Max:    " + formatBytes(nonHeapUsage.getMax()));
        System.out.println();

        double usagePercent = (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
        System.out.printf("Heap usage: %.1f%%%n", usagePercent);
        System.out.println();

        System.out.println("Monitoring these values helps predict OOM before it occurs.");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 2. Stack Depth Analysis
    // ----------------------------------------------------------------

    /**
     * Shows how stack depth increases with recursion depth.
     */
    static void demonstrateStackDepth() {
        System.out.println("2. Stack Depth Analysis");
        System.out.println("-".repeat(40));

        System.out.println("Default thread stack size: ~" +
                getDefaultStackSize() / 1024 + " KB");
        System.out.println();

        // Measure recursion depth without crashing
        int safeDepth = measureSafeRecursionDepth();
        System.out.println("Safe recursion depth: ~" + safeDepth);
        System.out.println("Each frame consumes ~" +
                getDefaultStackSize() / safeDepth + " bytes");
        System.out.println();

        System.out.println("StackOverflowError occurs when:");
        System.out.println("  frame_count * avg_frame_size > thread_stack_size");
        System.out.println();

        System.out.println("Increase stack size with: java -Xss2m Program");
        System.out.println();
    }

    /**
     * Measures how deep recursion can go before hitting stack limits.
     * Uses a conservative approach to avoid actually crashing.
     */
    static int measureSafeRecursionDepth() {
        int depth = 0;
        try {
            depth = safeRecursion(0);
        } catch (StackOverflowError e) {
            depth = countStackFrames(e);
        }
        return depth;
    }

    static int safeRecursion(int depth) {
        // Use a large local variable to consume stack space
        long[] waste = new long[1024];
        waste[depth % 1024] = depth;
        return safeRecursion(depth + 1);
    }

    static int countStackFrames(StackOverflowError e) {
        return e.getStackTrace().length;
    }

    /**
     * Returns the default thread stack size in bytes.
     */
    static long getDefaultStackSize() {
        Thread currentThread = Thread.currentThread();
        // Approximate: the JVM doesn't expose stack size directly
        // Use the thread's stack trace to estimate
        StackTraceElement[] trace = new Throwable().getStackTrace();
        return Runtime.getRuntime().totalMemory() / trace.length;
    }

    // ----------------------------------------------------------------
    // 3. Class Loading
    // ----------------------------------------------------------------

    /**
     * Shows how class loading works and where NoClassDefFoundError originates.
     */
    static void demonstrateClassLoading() {
        System.out.println("3. Class Loading Internals");
        System.out.println("-".repeat(40));

        ClassLoader appClassLoader = ErrorInternals.class.getClassLoader();
        ClassLoader platformClassLoader = appClassLoader.getParent();
        ClassLoader bootstrapClassLoader = platformClassLoader.getParent();

        System.out.println("Class Loader Hierarchy:");
        System.out.println("  Bootstrap:    " + bootstrapClassLoader);
        System.out.println("  Platform:     " + platformClassLoader);
        System.out.println("  Application:  " + appClassLoader);
        System.out.println();

        System.out.println("Class Loading Process:");
        System.out.println("  1. Check if class is already loaded");
        System.out.println("  2. Delegate to parent classloader");
        System.out.println("  3. Parent searches for class");
        System.out.println("  4. If not found, try to load it ourselves");
        System.out.println("  5. Find .class file, parse bytecode");
        System.out.println("  6. Verify bytecode (may throw ClassFormatError)");
        System.out.println("  7. Link: verify, prepare, resolve");
        System.out.println("  8. Initialize: run static initializer");
        System.out.println();

        System.out.println("NoClassDefFoundError causes:");
        System.out.println("  - Missing JAR on classpath");
        System.out.println("  - Static initializer failure");
        System.out.println("  - Class file corruption");
        System.out.println("  - Incorrect delegation in custom classloaders");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 4. Memory Regions
    // ----------------------------------------------------------------

    /**
     * Shows the different memory regions and their OOM conditions.
     */
    static void demonstrateMemoryRegions() {
        System.out.println("4. Memory Regions and OOM Conditions");
        System.out.println("-".repeat(40));

        System.out.println("Heap:");
        System.out.println("  - Stores objects and arrays");
        System.out.println("  - Managed by garbage collector");
        System.out.println("  - OOM message: \"Java heap space\"");
        System.out.println("  - Configure with: -Xmx<size>");
        System.out.println();

        System.out.println("Metaspace (JDK 8+):");
        System.out.println("  - Stores class metadata");
        System.out.println("  - Uses native memory");
        System.out.println("  - OOM message: \"Metaspace\"");
        System.out.println("  - Configure with: -XX:MaxMetaspaceSize=<size>");
        System.out.println();

        System.out.println("Thread Stacks:");
        System.out.println("  - Per-thread stack frames");
        System.out.println("  - Uses native memory");
        System.out.println("  - OOM message: \"unable to create new native thread\"");
        System.out.println("  - Configure with: -Xss<size>");
        System.out.println();

        System.out.println("Direct Buffers:");
        System.out.println("  - NIO direct byte buffers");
        System.out.println("  - Uses native memory");
        System.out.println("  - OOM message: \"Direct buffer memory\"");
        System.out.println("  - Configure with: -XX:MaxDirectMemorySize=<size>");
        System.out.println();

        List<byte[]> heap = new ArrayList<>();
        while (true) {
            heap.add(new byte[1024 * 1024]);
        }
    }

    // ----------------------------------------------------------------
    // 5. Shutdown Hooks
    // ----------------------------------------------------------------

    /**
     * Demonstrates shutdown hook mechanism for graceful error handling.
     */
    static void demonstrateShutdownHooks() {
        System.out.println("5. Shutdown Hooks");
        System.out.println("-".repeat(40));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook: Cleaning up resources...");
            System.out.println("Shutdown hook: Closing connections...");
            System.out.println("Shutdown hook: Flushing buffers...");
            System.out.println("Shutdown hook: Done.");
        }));

        System.out.println("Shutdown hook registered.");
        System.out.println("It will run when:");
        System.out.println("  - System.exit() is called");
        System.out.println("  - All non-daemon threads terminate");
        System.out.println("  - User presses Ctrl+C (SIGINT)");
        System.out.println("  - OS sends SIGTERM");
        System.out.println();

        System.out.println("Shutdown hooks do NOT run when:");
        System.out.println("  - System.halt() is called");
        System.out.println("  - JVM crashes (SIGSEGV, SIGABRT)");
        System.out.println("  - OS kills the JVM (SIGKILL)");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Utility Methods
    // ----------------------------------------------------------------

    /**
     * Formats bytes into human-readable form.
     */
    static String formatBytes(long bytes) {
        if (bytes < 0) return "unlimited";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
}