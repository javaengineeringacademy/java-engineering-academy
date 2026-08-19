package academy.javaengineering.jvm.classloader;

import java.lang.management.ManagementFactory;
import java.lang.management.ClassLoadingMXBean;

/**
 * Exercise 3: ClassLoader Leak Detection
 *
 * Task: Implement methods to detect classloader leaks and demonstrate
 * common leak patterns. This exercise simulates a hot-deploy scenario
 * where classloaders are created but not properly cleaned up.
 *
 * Understanding classloader leaks is critical for:
 * - Application server development (Tomcat, WildFly)
 * - Hot deployment tools (Spring Boot DevTools)
 * - Long-running applications that load/unload classes
 */
public class Exercise3 {

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Leak Detection ===\n");

        // Task 1: Monitor class loading statistics
        System.out.println("--- Task 1: Class Loading Stats ---");
        printClassLoadingStats();

        // Task 2: Simulate classloader leak
        System.out.println("\n--- Task 2: Simulate ClassLoader Leak ---");
        simulateClassLoaderLeak();

        // Task 3: Detect potential leaks
        System.out.println("\n--- Task 3: Detect Leaks ---");
        detectPotentialLeaks();

        // Task 4: Fix the leak
        System.out.println("\n--- Task 4: Fix ClassLoader Leak ---");
        demonstrateLeakFix();
    }

    /**
     * TODO: Implement this method to print class loading statistics.
     *
     * Requirements:
     * 1. Get ClassLoadingMXBean from ManagementFactory
     * 2. Print total loaded classes
     * 3. Print currently loaded classes
     * 4. Print unloaded classes
     * 5. Calculate and print the class loading rate
     */
    static void printClassLoadingStats() {
        // TODO: Implement this method
        System.out.println("  [TODO: Implement printClassLoadingStats]");
    }

    /**
     * TODO: Implement this method to simulate a classloader leak.
     *
     * Requirements:
     * 1. Create a custom classloader
     * 2. Load a class using that classloader
     * 3. Store a reference to the loaded class in a static collection (simulating a leak)
     * 4. Null out the classloader reference
     * 5. Attempt garbage collection
     * 6. Show that the classloader cannot be collected (leak)
     */
    static void simulateClassLoaderLeak() {
        // TODO: Implement this method
        System.out.println("  [TODO: Implement simulateClassLoaderLeak]");
    }

    /**
     * TODO: Implement this method to detect potential classloader leaks.
     *
     * Requirements:
     * 1. Check for ThreadLocal values that might hold classloader references
     * 2. Check for JDBC driver registrations
     * 3. Check for RMI connections
     * 4. Print warnings for potential leak sources
     */
    static void detectPotentialLeaks() {
        // TODO: Implement this method
        System.out.println("  [TODO: Implement detectPotentialLeaks]");
    }

    /**
     * TODO: Implement this method to demonstrate the fix for a classloader leak.
     *
     * Requirements:
     * 1. Create a custom classloader
     * 2. Load a class and create an instance
     * 3. Properly clean up all references
     * 4. Show that the classloader CAN be garbage collected
     * 5. Compare with the leak scenario
     */
    static void demonstrateLeakFix() {
        // TODO: Implement this method
        System.out.println("  [TODO: Implement demonstrateLeakFix]");
    }
}

/**
 * TODO: Implement a classloader that tracks its own lifecycle for leak detection.
 *
 * Requirements:
 * 1. Extend ClassLoader
 * 2. Track whether close() has been called
 * 3. Override close() to release resources
 * 4. Provide methods to check if resources were properly released
 */
class TrackableClassLoader extends ClassLoader {

    private boolean closed = false;

    public TrackableClassLoader(ClassLoader parent) {
        super(parent);
    }

    // TODO: Override close() method
    // @Override
    // public void close() throws IOException {
    //     // 1. Set closed flag
    //     // 2. Call super.close()
    //     // 3. Log that classloader was closed
    // }

    // TODO: Implement isClosed() method
    // public boolean isClosed() {
    //     return closed;
    // }
}
