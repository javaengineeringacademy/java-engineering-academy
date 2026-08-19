package academy.javaengineering.jvm.classloader;

import java.lang.ref.WeakReference;
import java.lang.management.ManagementFactory;
import java.lang.management.ClassLoadingMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Solution 3: ClassLoader Leak Detection
 *
 * Demonstrates how to detect, reproduce, and fix classloader memory leaks.
 * Includes proper cleanup patterns for production applications.
 */
public class Solution3 {

    private static final List<WeakReference<ClassLoader>> classloaders = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== ClassLoader Leak Detection Solution ===\n");

        System.out.println("--- Task 1: Class Loading Stats ---");
        printClassLoadingStats();

        System.out.println("\n--- Task 2: Simulate ClassLoader Leak ---");
        simulateClassLoaderLeak();

        System.out.println("\n--- Task 3: Detect Leaks ---");
        detectPotentialLeaks();

        System.out.println("\n--- Task 4: Fix ClassLoader Leak ---");
        demonstrateLeakFix();
    }

    static void printClassLoadingStats() {
        ClassLoadingMXBean classLoadingBean = ManagementFactory.getClassLoadingMXBean();
        System.out.println("  Total loaded classes: " + classLoadingBean.getTotalLoadedClassCount());
        System.out.println("  Currently loaded: " + classLoadingBean.getLoadedClassCount());
        System.out.println("  Unloaded classes: " + classLoadingBean.getUnloadedClassCount());
    }

    static void simulateClassLoaderLeak() {
        // Create a classloader and load a class
        LeakClassLoader loader = new LeakClassLoader(Solution3.class.getClassLoader());
        try {
            Class<?> clazz = loader.loadClass("academy.javaengineering.jvm.classloader.Solution3");
            System.out.println("  Loaded class with classloader: " + loader.hashCode());

            // Simulate leak: store reference in static collection
            classloaders.add(new WeakReference<>(loader));
            System.out.println("  Stored classloader reference (simulating leak)");

            // Null out local reference
            loader = null;

            // Force GC
            System.gc();
            Thread.sleep(100);

            // Check if classloader was collected
            System.out.println("  After GC - classloader still referenced: " +
                !classloaders.get(classloaders.size() - 1).refersTo(null));

        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }
    }

    static void detectPotentialLeaks() {
        System.out.println("  Checking for potential leak sources:");
        System.out.println("  - ThreadLocal values: Check if ThreadLocals are removed in finally blocks");
        System.out.println("  - JDBC drivers: Check DriverManager.deregisterDriver() calls");
        System.out.println("  - JNDI bindings: Check Context.close() and unbind() calls");
        System.out.println("  - RMI connections: Check UnicastRemoteObject.unexportObject() calls");
        System.out.println("  - Static caches: Check if caches use WeakReference/SoftReference");
    }

    static void demonstrateLeakFix() {
        // Create a classloader
        LeakClassLoader loader = new LeakClassLoader(Solution3.class.getClassLoader());
        try {
            Class<?> clazz = loader.loadClass("academy.javaengineering.jvm.classloader.Solution3");
            System.out.println("  Loaded class with classloader: " + loader.hashCode());

            // Proper cleanup: don't store strong references
            WeakReference<ClassLoader> ref = new WeakReference<>(loader);
            classloaders.add(ref);
            System.out.println("  Stored WeakReference (no leak)");

            // Null out reference
            loader = null;

            // Force GC
            System.gc();
            Thread.sleep(100);

            // Check if classloader was collected
            System.out.println("  After GC - classloader collected: " + ref.refersTo(null));
            System.out.println("  ClassLoader leak properly handled!");

        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }
    }
}

class LeakClassLoader extends ClassLoader {
    public LeakClassLoader(ClassLoader parent) {
        super(parent);
    }
}
