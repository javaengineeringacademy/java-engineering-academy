package jvm;

/**
 * JvmOverview - JVM architecture demonstration
 *
 * Covers:
 * - Class Loader Subsystem
 * - Runtime Data Areas (Method Area, Heap, Stack, PC Registers)
 * - Execution Engine (Interpreter, JIT, GC)
 */
public class JvmOverview {

    public static void main(String[] args) {
        System.out.println("=== JVM Architecture Overview ===");
        jvmArchitecture();

        System.out.println("\n=== Class Loading ===");
        classLoading();

        System.out.println("\n=== Memory Areas ===");
        memoryAreas();

        System.out.println("\n=== Runtime Information ===");
        runtimeInfo();
    }

    static void jvmArchitecture() {
        System.out.println("JVM Components:");
        System.out.println("1. Class Loader Subsystem");
        System.out.println("   - Bootstrap ClassLoader");
        System.out.println("   - Extension ClassLoader");
        System.out.println("   - Application ClassLoader");
        System.out.println("2. Runtime Data Areas");
        System.out.println("   - Method Area (Metaspace in Java 8+)");
        System.out.println("   - Heap");
        System.out.println("   - Java Stack");
        System.out.println("   - Program Counter Register");
        System.out.println("   - Native Method Stack");
        System.out.println("3. Execution Engine");
        System.out.println("   - Interpreter");
        System.out.println("   - JIT Compiler");
        System.out.println("   - Garbage Collector");
    }

    static void classLoading() {
        // Demonstrate class loading
        System.out.println("Loading JvmOverview class...");
        System.out.println("ClassLoader: " + JvmOverview.class.getClassLoader());
        System.out.println("Parent: " + JvmOverview.class.getClassLoader().getParent());
        System.out.println("Grandparent: " +
            JvmOverview.class.getClassLoader().getParent().getParent());

        // Load a class dynamically
        try {
            Class<?> stringClass = Class.forName("java.lang.String");
            System.out.println("\nDynamically loaded: " + stringClass.getName());
            System.out.println("ClassLoader: " + stringClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static void memoryAreas() {
        // Heap information
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        System.out.println("=== Heap Memory ===");
        System.out.println("Max Memory: " + (maxMemory / 1024 / 1024) + " MB");
        System.out.println("Total Memory: " + (totalMemory / 1024 / 1024) + " MB");
        System.out.println("Free Memory: " + (freeMemory / 1024 / 1024) + " MB");
        System.out.println("Used Memory: " + ((totalMemory - freeMemory) / 1024 / 1024) + " MB");

        // Stack information
        System.out.println("\n=== Stack Info ===");
        System.out.println("Available processors: " + runtime.availableProcessors());

        // Demonstrate object allocation on heap
        Object obj = new Object();
        System.out.println("\nObject allocated on heap: " + obj.hashCode());
    }

    static void runtimeInfo() {
        System.out.println("=== JVM Runtime Info ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS Arch: " + System.getProperty("os.arch"));
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        System.out.println("Class Path: " + System.getProperty("java.class.path"));

        // JVM arguments
        System.out.println("\n=== JVM Arguments ===");
        System.out.println("Max heap: -Xmx" +
            (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "m");
    }
}