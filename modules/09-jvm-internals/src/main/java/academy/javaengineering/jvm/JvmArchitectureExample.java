package academy.javaengineering.jvm;

/**
 * Demonstrates JVM architecture including ClassLoader and Runtime Data Areas.
 *
 * <p>This class provides examples of how the JVM loads classes and manages
 * different memory areas during program execution.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>ClassLoader hierarchy (Bootstrap, Platform, Application)</li>
 *   <li>Runtime Data Areas (Heap, Stack, Method Area)</li>
 *   <li>Memory management basics</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class JvmArchitectureExample {

    /**
     * Demonstrates ClassLoader hierarchy and delegation model.
     */
    public static class ClassLoaderDemo {
        /**
         * Prints the ClassLoader hierarchy for the current class.
         */
        public void printClassLoaderInfo() {
            ClassLoader appLoader = ClassLoaderDemo.class.getClassLoader();
            ClassLoader platformLoader = appLoader.getParent();
            ClassLoader bootstrapLoader = platformLoader != null ? platformLoader.getParent() : null;
            System.out.println("Application ClassLoader: " + appLoader);
            System.out.println("Platform ClassLoader: " + platformLoader);
            System.out.println("Bootstrap ClassLoader: " + bootstrapLoader);
        }
    }

    /**
     * Demonstrates different Runtime Data Areas in JVM.
     */
    public static class RuntimeDataAreas {
        private static int staticVariable = 10;
        private int instanceVariable = 20;

        /**
         * Demonstrates stack frame with different variable types.
         *
         * @param param the method parameter
         */
        public void demonstrateStackFrame(int param) {
            int localVar = 30;
            System.out.println("Static: " + staticVariable);
            System.out.println("Instance: " + instanceVariable);
            System.out.println("Local: " + localVar);
            System.out.println("Parameter: " + param);
        }
    }

    /**
     * Demonstrates JVM architecture concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== JVM Architecture Demo ===");
        new ClassLoaderDemo().printClassLoaderInfo();
        new RuntimeDataAreas().demonstrateStackFrame(40);
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");
        System.out.println("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
    }
}
